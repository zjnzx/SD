package com.energyfuture.symphony.m3.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;


/**
 * 网络处理公共类
 * @author 王超
 * 2015-4-16
 */
public class Network {

    /**
     * 网络是否可用
     * @param ctx 上下文
     * @return 网络是否可用
     */
    public static boolean isNetworkConnected(Context ctx) {
        if (ctx != null) {
            ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo i = conMgr.getActiveNetworkInfo();
            if (i == null)
                return false;
            else if (!i.isAvailable())
                return false;
            else if (!i.isConnected())
                return false;
            return true;
        }else
            return false;
    }

    /**
     * WIFI网络是否可用
     * @param ctx 上下文
     * @return 网络是否可用
     */
    public static boolean isWifiConnected(Context ctx) {
        if (ctx != null) {
            ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo i = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (i == null)
                return false;
            else if (!i.isAvailable())
                return false;
            else if (!i.isConnected())
                return false;
            return true;
        }else
            return false;
    }

    /**
     * 移动网络是否可用
     * @param ctx 上下文
     * @return 网络是否可用
     */
    public static boolean isMobileConnected(Context ctx) {
        if (ctx != null) {
            ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo i = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (i == null)
                return false;
            else if (!i.isAvailable())
                return false;
            else if (!i.isConnected())
                return false;
            return true;
        }else
            return false;
    }
    /**
     * 判断移动网络是否是4GorWifi
     *
     *
     */
    public static boolean isMobile4GorWifi(Context ctx) {
//
//        1、判断是否wifi
//           获取wifi sid名称，判断是否efwifi（获取wifi sid，转换为小写）
//           返回真
//
//        2、判断是否4G
//            返回真
        if (ctx != null) {
            //判断网络类型
            ConnectivityManager cmanager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cmanager.getActiveNetworkInfo();
            //获取wifi名称
            WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && !wifiInfo.getSSID().contains("flashair")&& !wifiInfo.getSSID().contains("efwifi")){
                    return true;
                }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                    int networktype = networkInfo.getSubtype();
                    switch (networktype){
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return true;
                    }
                }
            }
            return false;
        } else
            return false;
    }

    /**
     * 判断是否连接SD卡wifi
     *
     * @return:
     *   false:连接
     *   true:未连接
     */
    public static boolean isconnectFlashairWifi(Context ctx){
        if (ctx != null) {
            //判断网络类型
            ConnectivityManager cmanager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cmanager.getActiveNetworkInfo();
            //获取wifi名称
            WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && (wifiInfo.getSSID().contains("flashair")||wifiInfo.getSSID().contains("efwifi"))){
                    return false;
                }
            }
        }
        return  true;
    }

    public static String getWifiName(Context ctx){  //获得当前连接wifi的SSID
        ConnectivityManager cmanager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmanager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getSSID();
        }else{
            return "";
        }
    }

    public static String getWifiId(Context ctx){  //获得当前连接wifi的NetworkId
        ConnectivityManager cmanager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmanager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return String.valueOf(wifiInfo.getNetworkId());
        }else{
            return "";
        }
    }

    public static boolean closeWIFI(Context ctx){  //连接记录的wifi
        if (ctx != null) {
            if(!Network.isconnectFlashairWifi(ctx)){
                connected(ctx);
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是3G
     * @param context
     * @return
     */
    public static boolean is3GConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 是否是4G
     * @param context
     * @return
     */
    public static boolean is4GConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null){
            int networkType = networkINfo.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return true;
            }
        }
        return false;
    }

    /**
     * 是否连接3G,4G,或wifi
     * @return
     */
    public static boolean isNetConnected(Context ctx){
        if(isWifiConnected(ctx)){ //wifi
            return true;
        }else if(is3GConnected(ctx)){ //3G
            return true;
        }else if(is4GConnected(ctx)){ //4G
            return true;
        }
        return false;
    }
    public static void connected(Context ctx){  //根据记录的wifi的Networkid连接wifi
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
       /* WifiConfiguration wifiConfiguration=new WifiConfiguration();
        wifiConfiguration.SSID=Constants.wifiname;
        int id=wifiManager.addNetwork(wifiConfiguration);
        wifiManager.enableNetwork(id,true);*/
        wifiManager.enableNetwork(Integer.parseInt(Constants.wifiid),true);
    }
}
