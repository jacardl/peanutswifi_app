package com.peanutswifi;

import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.TestSuite;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Jac on 2016/1/19.
 */

public class MyTestRunner extends InstrumentationTestRunner {

    public static String SSID;
    public static String KEY;
    public static String URL;
    String TAG = "===MyTestRunner===";

    public void onCreate(Bundle arguments) {

        if (null != arguments) {
            final String SSID_URL = (String) arguments.get("ssid");
            final String KEY_URL = (String) arguments.get("key");
            final String URL_URL = (String) arguments.get("url");
            if (SSID_URL != null) {
                SSID = decodeString(SSID_URL);
            } else {
                SSID = null;
            }
            if (KEY_URL != null) {
                KEY = decodeString(KEY_URL);
            } else {
                KEY = null;
            }
            if (URL_URL != null) {
                URL = decodeString(URL_URL);
            } else {
                URL = null;
            }

        }
        super.onCreate(arguments);
    }

    @Override
    public TestSuite getAllTests()
    {
        return new TestSuiteBuilder(MyTestRunner.class).includeAllPackagesUnderHere().build();
    }
    @Override
    public ClassLoader getLoader()
    {
        return MyTestRunner.class.getClassLoader();
    }

    public String decodeString(String URL) {

        String urlString=null;
        try {
            urlString = URLDecoder.decode(URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block

        }
        return urlString;
    }

}
