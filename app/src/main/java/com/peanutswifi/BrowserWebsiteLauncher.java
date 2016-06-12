package com.peanutswifi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


//http://www.cnblogs.com/mengdd/p/3142442.html

/**
 * Created by jac-pc2 on 2016/6/12.
 */
public class BrowserWebsiteLauncher extends Activity {
    private String TAG = "http";
    private EditText mURLEdit = null;

    private Button mBrowserButton = null;
    private TextView mResultText = null;

    private String URL = null;

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
        mResultText = (TextView) findViewById(R.id.resultText);

        mBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mBrowserButton.setEnabled(false);
                Log.i(TAG, "GET request");
//                获取要访问的url
                URL = mURLEdit.getText().toString();
                // 生成请求对象
                if (URL != "") {
                    HttpGet httpGet = new HttpGet(URL);
                    HttpClient httpClient = new DefaultHttpClient();
                    // 发送请求
                    try {

                        HttpResponse response = httpClient.execute(httpGet);

                        // 显示响应
                        showResponseResult(response);// 一个私有方法，将响应结果显示出来

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 显示响应结果到命令行和TextView
     * @param response
     */
    private void showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return;
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;

            }

            System.out.println(result);
            mResultText.setText("Response Content from server: " + result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
