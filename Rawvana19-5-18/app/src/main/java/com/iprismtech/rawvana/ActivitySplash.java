package com.iprismtech.rawvana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.iprismtech.rawvana.others.Values;

import java.security.MessageDigest;

public class ActivitySplash extends FragmentActivity {
    private Activity actSplash;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Values.RECIPEID="1";
        Values.RECIPENAME="Breakfast";
        context=getApplicationContext();
       // printHashKey(context);
        actSplash = ActivitySplash.this;
        try {
            Handler splash_time_handler = new Handler(Looper.getMainLooper());
            splash_time_handler.postDelayed(new Runnable() {
                public void run() {
                    start();;
                }//run()
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void start() {
        Intent intent =new Intent(getApplicationContext(),ActivityMain.class);
        intent.putExtra("Conditions","splash");
        startActivity(intent);
    }
}
