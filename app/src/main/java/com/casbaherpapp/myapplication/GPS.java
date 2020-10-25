package com.casbaherpapp.myapplication;



import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.content.Intent;

import android.Manifest;
import android.location.Location;
import android.content.pm.PackageManager;
import android.app.Service;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

import static java.security.AccessController.getContext;


public class GPS extends Service {
    public int id=2;
    public int exit=0;

    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";

    private FusedLocationProviderClient client;
    private LocationCallback mLocationCallback ;

    @Override
    public int onStartCommand(Intent data, int startId, int flags) {
        id = Integer.parseInt(data.getExtras().getString("id"));



        return START_NOT_STICKY;

    }

    public IBinder onBind(Intent intent) {


        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();



        requestLocationUpdates();
        buildNotification();
        //buildNotification();
        // loginToFirebase();
    }

    public static boolean isServiceRunning(Context context, Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onDestroy(){

        super.onDestroy();
       exit=1;
        if(stopReceiver!=null)
            try{
                unregisterReceiver(stopReceiver);
                client.removeLocationUpdates(mLocationCallback);
                stopSelf();

            } catch (Exception e) {
                e.printStackTrace();
            }



    }



    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

// Create the persistent notification//
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Localisation activée")

//Make this notification ongoing so it can’t be dismissed by the user//

                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.tracking_enabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, builder.build());

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.casbaherpapp.myapplication";
        String channelName = "Hand to hand";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.tracking_enabled)
                .setContentTitle("Localisation activée")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    public  BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//Unregister the BroadcastReceiver when the notification is tapped//

        }
    };



//Initiate the request to track the device's location//

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

     request.setInterval((3/2)*60*1000);
        request.setFastestInterval(1*60*1000);


//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
         client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request,  mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    //Log.e( "HANIIIII","P");

//Get a reference to the database, so your app can perform read and write operations//
if(exit==0){
                    final Location location = locationResult.getLastLocation();
                    if (location != null) {
                            AndroidNetworking.post(DATA_INSERT_URL)
                                    .addBodyParameter("action","location")
                                    .addBodyParameter("lat", String.valueOf(location.getLatitude()))
                                    .addBodyParameter("long", String.valueOf(location.getLongitude()))
                                    .addBodyParameter("id", id+"")
                                    .setTag("test")
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                       public void onResponse(JSONArray response) {

                                            if(response != null)
                                                try {

                                                    //SHOW RESPONSE FROM SERVER

                                                    String responseString= response.get(0).toString();




                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                        }

                                        //ERROR
                                        @Override
                                        public void onError(ANError anError) {
                                           BDD bd;
                                            bd= new BDD(getApplicationContext());
                                           bd.open();
                                            Calendar rightNow = Calendar.getInstance();
                                            String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                            bd.InsertLocation(id,String.valueOf(location.getLongitude()),String.valueOf(location.getLatitude()),date,rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE));

                                            bd.close();

                                        }


                                    });



                    }
    BDD bd = new BDD(GPS.this);

    bd.open();
    bd.synchronisationpventee();
    bd.close();


                }
else{
    client.removeLocationUpdates(this);

}
}

            }, null);
        }
    }



}

