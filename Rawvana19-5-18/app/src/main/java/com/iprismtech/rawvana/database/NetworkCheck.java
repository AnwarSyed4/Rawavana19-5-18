package com.iprismtech.rawvana.database;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Srikanth on 7/4/2016.
 */
public class NetworkCheck {
    public boolean isConnected(Context context) {
        String ServerUrl = "http://www.google.com";
        try{
            /*ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                URL myUrl = new URL(ServerUrl);
                //Network is available but check if we can get access from the network.
                HttpURLConnection url = (HttpURLConnection) myUrl.openConnection();
                url.setRequestProperty("Connection", "close");
                url.setConnectTimeout(2000); // Timeout 2 seconds.
                try {
                    url.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (url.getResponseCode() == 200)  //Successful response.
                {
                    return true;
                }
            }*/
            // get Connectivity Manager object to check connection
            ConnectivityManager connec = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // Check for network connections
            if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
                return true;
            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
                return false;
            }
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}