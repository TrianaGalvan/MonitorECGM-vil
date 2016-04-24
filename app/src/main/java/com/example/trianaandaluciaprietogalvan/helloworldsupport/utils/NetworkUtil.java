package com.example.trianaandaluciaprietogalvan.helloworldsupport.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by trianaandaluciaprietogalvan on 10/04/16.
 */
public class NetworkUtil {
    public static  boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean isConnctedToMobileNet(Context context){
        boolean net_mobile = false;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
            net_mobile = true;
        }
        return net_mobile;
    }
}
