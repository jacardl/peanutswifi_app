package com.peanutswifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;


/**
 * Created by Jac on 2015/4/8.
 */
public class Wifi {

    private static final String TAG = Wifi.class.getSimpleName();

    public static boolean connectToNewNetwork(final WifiManager wifiMgr, final String ssid, final String encryp, final String passwd, final String bssid, boolean reassociate){

//        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
//        for(final WifiConfiguration configTmp : configurations) {
//            wifiMgr.removeNetwork(configTmp.networkId);
//        }
//        wifiMgr.saveConfiguration();

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = StringUtils.convertToQuotedString(ssid);
        config.BSSID = bssid;
        setupSecurity(config, encryp, passwd);

        int id = wifiMgr.addNetwork(config);

        if(!wifiMgr.saveConfiguration()){
            return false;
        }

//        final List<WifiConfiguration> configurations2 = wifiMgr.getConfiguredNetworks();

        if(!wifiMgr.enableNetwork(id, true)){
            return false;
        }

        final boolean connect = reassociate ? wifiMgr.reassociate() : wifiMgr.reconnect();

        if(!connect) {
            return false;
        }

        return true;

    }

    static private void setupSecurity(WifiConfiguration config, String encryp, final String passwd){
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        if (encryp.equals("NONE")){
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        } else if (encryp.equals("WPA-AES-PSK")){
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

            if (!TextUtils.isEmpty(passwd)){
                config.preSharedKey = StringUtils.convertToQuotedString(passwd);
            }

        } else if (encryp.equals("WPA-TKIP-PSK")){
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

            if (!TextUtils.isEmpty(passwd)){
                config.preSharedKey = StringUtils.convertToQuotedString(passwd);
            }
        } else if (encryp.equals("WPA2-AES-PSK")){
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

            if (!TextUtils.isEmpty(passwd)){
                config.preSharedKey = StringUtils.convertToQuotedString(passwd);
            }
        } else if (encryp.equals("WPA2-TKIP-PSK")){
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

            if (!TextUtils.isEmpty(passwd)){
                config.preSharedKey = StringUtils.convertToQuotedString(passwd);
            }
        }
    }


}
