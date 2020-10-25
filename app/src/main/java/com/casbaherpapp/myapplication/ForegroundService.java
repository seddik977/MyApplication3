package com.casbaherpapp.myapplication;



import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.imad.Entities.Historique;
import com.casbaherpapp.myapplication.imad.Entities.VersementItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.content.Intent;

import android.Manifest;
import android.location.Location;
import android.content.pm.PackageManager;
import android.app.Service;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.leolin.shortcutbadger.ShortcutBadger;

import static java.security.AccessController.getContext;


public class ForegroundService extends Service {
    public int id=2;

    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    private Thread mThread;
    ScheduledExecutorService worker;
    private static final int SYNC_TIME =60* 1000;

    @Override
    public int onStartCommand(Intent data, int startId, int flags) {
        BDD bd=new BDD(ForegroundService.this);
        bd.open();
        id = bd.getID();
        bd.close();
        requestLocationUpdates();
        return Service.START_STICKY;

    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);


        PendingIntent service = PendingIntent.getService(
                getApplicationContext(),
                1001,
                new Intent(getApplicationContext(), ForegroundService.class),
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
    }
    public IBinder onBind(Intent intent) {


        return null;
    }
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.d("Exception", "Uncaught exception start!");
            ex.printStackTrace();

            //Same as done in onTaskRemoved()
            PendingIntent service = PendingIntent.getService(
                    getApplicationContext(),
                    1001,
                    new Intent(getApplicationContext(), ForegroundService.class),
                    PendingIntent.FLAG_ONE_SHOT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
            System.exit(2);
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();



        //buildNotification();
        //buildNotification();
        // loginToFirebase();
    }



    public void onDestroy(){
        super.onDestroy();
        stopThread();


    }
    private void stopThread() {
        worker = null;
        if (mThread != null && mThread.isAlive()) mThread.interrupt();
    }
//Create the persistent notification//







//Initiate the request to track the device's location//

    private void requestLocationUpdates() {
        if (worker == null) worker = Executors.newSingleThreadScheduledExecutor();
        if (mThread == null || !mThread.isAlive()) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {





                    AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action", "get notification")

                    .addBodyParameter("id", id + "")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response != null)
                                try {
                                    if(response.length()>0) {
                                        BDD bd = new BDD(ForegroundService.this);
                                        bd.open();
                                        bd.addnoti(response.length());
                                        int count = bd.getnoti();
                                        bd.close();
                                        ShortcutBadger.applyCount(ForegroundService.this, count);
                                    }
                                    //SHOW RESPONSE FROM SERVER

                                    for (int i = 0; i < response.length(); i++) {

                                        String pvente = response.getJSONObject(i).getString("pvente");
                                        String id_pvente = response.getJSONObject(i).getString("id_pvente");

                                        String heure = response.getJSONObject(i).getString("heure");
                                        String date = response.getJSONObject(i).getString("date");

                                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
                                        String datee = "";
                                        try {
                                            datee = format2.format(format1.parse(date));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        notifyThis("Vous avez une commande de la part :", "N°: " + id_pvente + " " + pvente + " Date & heure :" + datee + "_" + heure, date, response.getJSONObject(i).getInt("id") + 2);


                                    }

                                } catch (JSONException e) {
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {


                        }


                    });


                    AndroidNetworking.post("http://www.casbahdz.com/adm/CommandeLivreur/commande_livreur_crud.php")
                            .addBodyParameter("action","10").addBodyParameter("data", String.valueOf(id))
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {


                                @Override
                                public void onResponse(JSONArray response) {
                                            Log.e("not", String.valueOf(response.length()));
                                            if(response.length()!=0) {
                                                for (int i = 0; i < response.length(); i++) {

                                                    try {
                                                        VersementItem versementItem = new VersementItem();
                                                        int id = response.getJSONObject(i).getInt("id");
                                                        String comptableFirstName = response.getJSONObject(i).getString("firstname");
                                                        String comptableLastName = response.getJSONObject(i).getString("lastname");
                                                        String action = response.getJSONObject(i).getString("action");
                                                        String createdDate = response.getJSONObject(i).getString("created_date");
                                                        String updatedDate = response.getJSONObject(i).getString("updated_date");
                                                        double ancientCredit = response.getJSONObject(i).getDouble("credit_ancient");
                                                        double noveauCredit = response.getJSONObject(i).getDouble("credit_noveau");
                                                        double dernierVersement = response.getJSONObject(i).getDouble("versement");
                                                        double versementTotal = response.getJSONObject(i).getDouble("versement_total");

                                                       if(action.equals("versement")){
                                                           notifyVersement("Votre versement a ete bien validé par:",comptableFirstName+" "+comptableLastName+" le "+ createdDate,createdDate, id);
                                                       }else  {notifyVersement("Votre credit a ete bien modifié par:",comptableFirstName+" "+comptableLastName+" le "+ createdDate,createdDate, id);}

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }


                                            }

                                }





                                @Override
                                public void onError(ANError anError) {

                                }
                            });





                    if (worker != null) {
                        worker.schedule(this, SYNC_TIME, TimeUnit.MILLISECONDS);
                    }
                }
            });
            mThread.start();
        }

//Start





    }

    public void notifyVersement(String title, String message, String date, int id) {
        Intent intent = new Intent(this, Historique.class);
        intent.putExtra("holla",date);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        /**Creates an explicit intent for an Activity in your app**/


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_money)
                .setTicker("Vous avez un evenement de versement")
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("INFO")
                .setOngoing(true)
                .setContentIntent(resultPendingIntent);

        NotificationManager  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "com.casbaherpapp.myapplication";
        String channelName = "Hand to hand";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Hand to Hand", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(id /* Request Code */, mBuilder.build());

    }


    public void notifyThis(String title, String message, String date, int id) {
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("holla",date);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        /**Creates an explicit intent for an Activity in your app**/


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.commande)
                .setTicker("Vous avez une commande")
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("INFO")
                .setOngoing(true)
                .setContentIntent(resultPendingIntent);

        NotificationManager  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "com.casbaherpapp.myapplication";
        String channelName = "Hand to hand";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Hand to Hand", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(id /* Request Code */, mBuilder.build());

    }

}

