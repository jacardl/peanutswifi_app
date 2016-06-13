package com.peanutswifi;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


//http://www.cnblogs.com/mengdd/p/3142442.html

/**
 * Created by jac-pc2 on 2016/6/12.
 */
public class BrowserWebsiteLauncher extends Activity {
    private String TAG = "http";
    private EditText mURLEdit = null;

    public Button mBrowserButton = null;
    private TextView mHeaderText = null;
    private TextView mContentsText = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        bindListeners();
    }
    /**
     * Setup event handlers and bind variables to values from xml
     */
    private void bindListeners() {
        mBrowserButton = (Button) findViewById(R.id.browserBtn);
        mURLEdit = (EditText) findViewById(R.id.urlEdit);
        mHeaderText = (TextView) findViewById(R.id.headerText);
        mContentsText = (TextView) findViewById(R.id.contentsText);

        mBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mBrowserButton.setEnabled(false);
                Log.d(TAG, "GET request");
                //                获取要访问的url
                // Gets the URL from the UI's text field.
                String stringUrl = mURLEdit.getText().toString();
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);
                } else {
                    mHeaderText.setText("No network connection available.");
                }
                mBrowserButton.setEnabled(true);
            }
        });
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                String[] error = {"Unable to retrieve web page. URL may be invalid.",
                        "null"};
                return error;
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String[] result) {
            mHeaderText.setText(result[0]);
            mContentsText.setText(result[1]);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String[] downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        String resultHeader = "==============header=============\n";
        String resultContents = "\n==============contents=============\n";
        String[] resultAll = new String[2];

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);

            Map<String, List<String>> headerMap = conn.getHeaderFields();
            resultHeader = resultHeader + headerMap.toString();

            is = conn.getInputStream();
            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            resultContents = resultContents + contentAsString;

            resultAll[0] = resultHeader;
            resultAll[1] = resultContents;
            return resultAll;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[4096];
        StringBuilder builder = new StringBuilder();
        int len;
        while ((len = reader.read(buffer)) > 0) {
            builder.append(buffer, 0, len);
        }
        return builder.toString();
    }
}
