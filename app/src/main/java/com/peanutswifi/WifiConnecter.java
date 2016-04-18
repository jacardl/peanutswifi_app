package com.peanutswifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.net.wifi.WifiInfo;

import java.util.List;

/**
 * Created by Jac on 2015/4/7.
 */
public class WifiConnecter{

    // Combo scans can take 5-6s to complete
    private static final int WIFI_RESCAN_INTERVAL_MS = 5 * 1000;

    private static final String TAG = WifiConnecter.class.getSimpleName();
    public static final int MAX_TRY_COUNT = 3;

    private Context mContext;
    private WifiManager mWifiManager;

    private final IntentFilter mFilter;
    private final BroadcastReceiver mReceiver;
    private final ScannerHandler mScanner;
    private ActionListener mListener;
    private String mSsid;
    private String mPasswd;
    private String mEncryp;

    private boolean isRegistered;
    private boolean isActiveScan;

    public WifiConnecter(Context context){
        this.mContext = context;
        mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        mFilter = new IntentFilter();
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleEvent(context, intent);
            }
        };

        context.registerReceiver(mReceiver, mFilter);
        isRegistered = true;
        mScanner = new ScannerHandler();

    }

    public void connect(String ssid, String encryption, String password, ActionListener listener){
        this.mListener = listener;
        this.mSsid = ssid;
        this.mPasswd = password;
        this.mEncryp = encryption;

        if(listener != null){
            listener.onStarted(ssid);
        }

        if (!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }

        if(!Wifi.connectToNewNetwork(mWifiManager, mSsid, mEncryp, mPasswd,true)){
            if(mListener != null){
                mListener.onFailure("Connect to AP is failed!");
                mListener.onFinished(false);
            }
            onPause();
        }
    }

    public void scanSpecifiedSSID(String ssid, ActionListener listener){
        this.mListener = listener;
        this.mSsid = ssid;
        if(listener != null) {
            listener.onScan(ssid);
        }
        mScanner.forceScan();
    }

    public void clearConnect(ActionListener listener){
        //   clear config and shutdown wifi
        if (listener != null){
            this.mListener = listener;

            onResume();

            listener.onClearConfig();
            final List<WifiConfiguration> configurations = mWifiManager.getConfiguredNetworks();
            if (configurations != null) {
                for (final WifiConfiguration configTmp : configurations) {
                    mWifiManager.removeNetwork(configTmp.networkId);
                }
                mWifiManager.saveConfiguration();
            }
            listener.onShutDownWifi();
            if (mWifiManager.isWifiEnabled()){
                mWifiManager.setWifiEnabled(false);
            }

        }
    }

    public void clearConnect2(){
//   clear without toast text
        onResume();

        final List<WifiConfiguration> configurations = mWifiManager.getConfiguredNetworks();
        if (configurations != null) {
            for (final WifiConfiguration configTmp : configurations) {
                mWifiManager.removeNetwork(configTmp.networkId);
            }
            mWifiManager.saveConfiguration();
        }

        WifiInfo info = mWifiManager.getConnectionInfo();
        String curSsid = info.getSSID();
        if (curSsid != "0x") {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public void clearConnect3(ActionListener listener) {
//   clear config and disconnect with ap
        if (listener != null) {
            this.mListener = listener;

            final List<WifiConfiguration> configurations = mWifiManager.getConfiguredNetworks();
            if (configurations != null) {
                for (final WifiConfiguration configTmp : configurations) {
                    mWifiManager.removeNetwork(configTmp.networkId);
                }
                mWifiManager.saveConfiguration();
            }
            listener.onClearConfig();

            onResume();
            WifiInfo info = mWifiManager.getConnectionInfo();
            String curSsid = info.getSSID();
            if (curSsid != "0x") {
                mWifiManager.disconnect();
            }
            listener.onShutDownWifi();

        }
    }

    public void shutDownWifi(){
//   shutdown wifi
        onResume();

        while (mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);

        }

    }

    public void turnOnWifi(){
//        turn on wifi
        onResume();

        while (!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }

    private void handleEvent(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action) && isActiveScan){
            boolean flag = false;
            List<ScanResult> results = mWifiManager.getScanResults();
            for (ScanResult result: results){
                boolean ssidEquals = this.mSsid.equals(result.SSID);
                if (ssidEquals){
                    flag = true;
                    mScanner.pause();
                    break;
                }
            }
            if(mListener != null && flag == false) {
                mListener.onFailure("Cannot find specified SSID!");
            }
            if (mListener != null && flag == true) {
                mListener.onScanSuccess(this.mSsid);
                mListener.onFinished(true);
            }

        }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            NetworkInfo mInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (mInfo.isConnected()){
                WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
                String getSSID = mWifiInfo.getSSID();
                if (mWifiInfo != null && mInfo.isConnected() && getSSID != null) {
                    String quotedString = StringUtils.convertToQuotedString(mSsid);
                    boolean ssidEquals = quotedString.equals(getSSID);
                    if (ssidEquals) {
                        if (mListener != null) {
                            mListener.onSuccess(mWifiInfo);
                            mListener.onFinished(true);
                        }
                        onPause();
                    }
                }
            }
        }
    }

    public void onPause(){
        if(isRegistered){
            mContext.unregisterReceiver(mReceiver);
            isRegistered = false;
        }
        mScanner.pause();
    }

    public void onResume(){
        if(!isRegistered){
            mContext.registerReceiver(mReceiver, mFilter);
            isRegistered = true;
        }
//        mScanner.resume();
    }

    @SuppressLint("HandlerLeak")
    private class ScannerHandler extends Handler {
        private int mRetry = 0;

        void resume(){
            if(!hasMessages(0)){
                sendEmptyMessage(0);
            }
        }

        void forceScan(){
            removeMessages(0);
            sendEmptyMessage(0);
        }

        void pause(){
            mRetry = 0;
            isActiveScan = false;
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message message){
            if(mRetry < MAX_TRY_COUNT){
                mRetry++;
                isActiveScan = true;
                if (!mWifiManager.isWifiEnabled()){
                    mWifiManager.setWifiEnabled(true);
                }

                boolean startScan = mWifiManager.startScan();
                Log.d(TAG, "StarScan:" + startScan);

                if (!startScan) {
                    if(mListener != null) {
                        mListener.onFailure("Scan failed, try later!");
                        mListener.onFinished(false);
                    }
                    onPause();
                    return;
                }
            }else{
                mRetry = 0;
                isActiveScan = false;
                if(mListener != null){
                    mListener.onFailure("Cannot find specified SSID, scan countdown is over!");
                    mListener.onFinished(false);
                }
                onPause();
                return;
            }
            sendEmptyMessageDelayed(0, WIFI_RESCAN_INTERVAL_MS);
        }
    }

    public interface ActionListener {

        /**
         * The operation started
         *
         * @param ssid
         */
        public void onStarted(String ssid);

        /**
         * The operation succeeded
         *
         * @param ssid
         */

        public void onScan(String ssid);
        /**
         * The operation succeeded
         *
         * @param
         */

        public void onScanSuccess(String ssid);

        public void onSuccess(WifiInfo info);

        /**
         * The operation failed
         */
        public void onFailure(String reason);

        /**
         * The operation finished
         *
         * @param isSuccessed
         */
        public void onFinished(boolean isSuccessed);

        public void onClearConfig();

        public void onShutDownWifi();
    }
}
