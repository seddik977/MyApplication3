package com.casbaherpapp.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.casbaherpapp.myapplication.GPS;

import static com.casbaherpapp.myapplication.Main.isServiceRunning;

public class ActivityStackManager {

    private static final ActivityStackManager mActivityStack = new ActivityStackManager();

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        return mActivityStack;
    }

    public void stopLocationService(Context context) {
        if (isServiceRunning(context, GPS.class)) {
            context.stopService(new Intent(context, GPS.class));
            Log.e("StackManager", "TrackingServiceStop");
        }
    }

    public void startLocationService(Context context){
        Intent serviceIntent = new Intent(context, GPS.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}