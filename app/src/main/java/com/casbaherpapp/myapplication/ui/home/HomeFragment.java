package com.casbaherpapp.myapplication.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.DirectionsJSONParser;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.MainActivity;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.TouchyWebView;
import com.casbaherpapp.myapplication.liv;
import com.casbaherpapp.myapplication.pv;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.collections.GroundOverlayManager;
import com.google.maps.android.collections.MarkerManager;
import com.google.maps.android.collections.PolygonManager;
import com.google.maps.android.collections.PolylineManager;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.kml.KmlLayer;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.parseFloat;

public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private static final String DATA_INSERT_URL = "http://www.casbahdz.com/CRUD.php";
    private int id = -1;
    private View v;
    private Calendar cal;
    private String auj;
    private ProgressDialog progress;
    private BDD bd;
    private String[] id_client,heure,client,id_clientn,heuren,clientn,longg,lat,adresse;
    private FusedLocationProviderClient fusedLocationClient;

    private boolean shouldRefreshOnResume = false;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker mCurrLocation;
    private Polyline mPolyline;

    public void onResume() {


        super.onResume();
        //update your fragment
        if (shouldRefreshOnResume) {
            if (mGoogleApiClient != null) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            get_magg();
        }
    }

    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        bd = new BDD(root.getContext());
        progress = new ProgressDialog(getContext());
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        progress.show();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH) + 1;
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        auj = mYear + "-" + mMonth + "-" + mDay;

     /*   Button selectDate = root.findViewById(R.id.date2);
        Button D = root.findViewById(R.id.date1);
        Button p = root.findViewById(R.id.date);*/

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

     /*   D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month++;
                               String d=year+"-"+month+"-"+day;

                                setVisits(d);
                                setSuccess(d);
                                setDrop(d);
                                setSk(d);
                            }
                        }, year, month, dayOfMonth);

            datePickerDialog.show();}
        });




        p.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                       if(firstDate!=null && secondDate!=null){
                        Date date = secondDate.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        firstDate.set(Calendar.HOUR_OF_DAY, hours);
                        firstDate.set(Calendar.MINUTE, minutes);
                        secondDate.set(Calendar.HOUR_OF_DAY, hours);
                        secondDate.set(Calendar.MINUTE, minutes);
                        String d1 = dateFormat.format(firstDate.getTime());
                        String d2=dateFormat.format(secondDate.getTime());

                        setVisits(d1,d2);
                        setSuccess(d1,d2);
                        setDrop(d1,d2);
                        setSk(d1,d2);

                    }}
                };
               new SlyCalendarDialog()

                        .setSingle(false)
                        .setCallback(callback)
                        .show(getFragmentManager(), "TAG_SLYCALENDAR");



            }
        });



        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setVisits();
                setSuccess();
                setDrop();
            }
        });
        final Button pvv =root.findViewById(R.id.pventess);

        pvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getContext(), pv.class);



                myIntent.putExtra("branche", ((Main)getContext()).getB()+"");

                startActivity(myIntent);


            }
        });


        setVisits();
        setSuccess();
        setDrop();
        setSk();*/
        v = root;
        id = ((Main) getContext()).getData();
        FloatingActionButton fab = ((Main) getContext()).findViewById(R.id.fab);
        fab.show();
        verif();

        setzone();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


get_mag();

        return root;
    }


    private List<String> getDirectionsUrl(ArrayList<LatLng> markerPoints) {
        List<String> mUrls = new ArrayList<>();
        if (markerPoints.size() > 1) {
            String str_origin = markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;
            String str_dest = markerPoints.get(1).latitude + "," + markerPoints.get(1).longitude;

            String key = "key=" + getString(R.string.map_key);
            String sensor = "sensor=false";
            String parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" + key+"&"+sensor;
            String output = "json";

            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

            mUrls.add(url);
            for (int i = 2; i < markerPoints.size(); i++)//loop starts from 2 because 0 and 1 are already printed
            {
                str_origin = str_dest;
                str_dest = markerPoints.get(i).latitude + "," + markerPoints.get(i).longitude;
                parameters = "origin=" + str_origin + "&destination=" + str_dest + "&" + key;
                url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                mUrls.add(url);
            }
        }

        return mUrls;
    }
    public void verif() {
        bd.open();
        String user = bd.getuser();
        String pass = bd.getpassword();

        bd.close();
        if (user != "" && pass != "") {
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action", "login")
                    .addBodyParameter("user", user)
                    .addBodyParameter("password", pass)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response != null)
                                try {

                                    id = response.getJSONObject(0).getInt("id");


                                    // String responseString= response.get(0).toString();


                                } catch (JSONException e) {
                                    bd.open();
                                    bd.dec();
                                    bd.close();
                                    Intent intent1 = new Intent(v.getContext(), MainActivity.class);
                                    startActivity(intent1);
                                    getActivity().finish();

                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();

                        }


                    });
        }

    }

    public void setzone() {
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action", "get zone")
                .addBodyParameter("id", id + "")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null)
                            try {
                                //SHOW RESPONSE FROM SERVER
                                TextView nn = ((Main) getActivity()).findViewById(R.id.zone);
                                nn.setVisibility(View.VISIBLE);
                                nn.setText("Zone d'aujourd'hui : " + response.getJSONObject(0).getString("zone"));

                                progress.dismiss();


                            } catch (JSONException e) {
                                progress.dismiss();


                                e.printStackTrace();
                                Toast.makeText(getContext(), "Erreur ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    public void onError(ANError anError) {

                        progress.dismiss();
                    }


                });
    }
    public  void get_mag(){

        bd.open();
       int id=bd.getID();
        bd.close();
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get routes")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                int j=0,k=0;

                                for(int i=0;i<response.length();i++) {
                                if(response.getJSONObject(i).getString("longg").equals("") || response.getJSONObject(i).getString("lat").equals("") )
                                    k++;
                                            else
                                                j++;
                                }
                                longg= new String[j];
                                lat= new String[j];
                                client= new String[j];
                                heure= new String[j];
                                id_client= new String[j];

                                clientn= new String[k];
                                heuren= new String[k];
                                id_clientn= new String[k];
                                adresse=new String[k];
                                //SHOW RESPONSE FROM SERVER
                                j=0;
                                k=0;
                                for(int i=0;i<response.length();i++) {
                                    if(!response.getJSONObject(i).getString("longg").equals("") && !response.getJSONObject(i).getString("lat").equals("") ) {

                                        longg[j] = response.getJSONObject(i).getString("longg");
                                        lat[j] = response.getJSONObject(i).getString("lat");
                                        client[j] = response.getJSONObject(i).getString("nom");
                                        heure[j] = response.getJSONObject(i).getString("heure");
                                        id_client[j] = response.getJSONObject(i).getString("id");

                                        j++;
                                    }
                                    else{
                                        clientn[k] = response.getJSONObject(i).getString("nom");
                                        heuren[k] = response.getJSONObject(i).getString("heure");
                                        id_clientn[k] = response.getJSONObject(i).getString("id");
                                        adresse[k]=response.getJSONObject(i).getString("lieu")+", "+response.getJSONObject(i).getString("zone")+", "+response.getJSONObject(i).getString("wilaya");
                                        k++;

                                    }



                                }
                                if(clientn.length>0){
                                    Button b = v.findViewById(R.id.clients);
                                    b.setVisibility(View.VISIBLE);
                                    b.setText("("+clientn.length+ ") Client(s) sans localisations");
                                    b.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    final Dialog dialog = new Dialog(getContext());
                                                    dialog.setContentView(R.layout.window);
                                                    dialog.setTitle("Clients sans localisation");

                                                    // set the custom dialog components - text, image and button
                                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                                    String x="";
                                                    for(int i=0;i<clientn.length;i++){
                                                        x=x+(i+1)+"- ID :"+id_clientn[i]+"|| Nom :"+clientn[i]+"|| Adresse :"+adresse[i]+"\n";

                                                    }
                                                    text.setText(x);

                                                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                                                    // if button is clicked, close the custom dialog
                                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                    dialog.show();

                                                }
                                            });
                                }
                               // getallpventes();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();




                    }


                });

    }

    public  void get_magg(){

        bd.open();
        int id=bd.getID();
        bd.close();
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get routes")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                int j=0,k=0;

                                for(int i=0;i<response.length();i++) {
                                    if(response.getJSONObject(i).getString("longg").equals("") || response.getJSONObject(i).getString("lat").equals("") )
                                        k++;
                                    else
                                        j++;
                                }
                                longg= new String[j];
                                lat= new String[j];
                                client= new String[j];
                                heure= new String[j];
                                id_client= new String[j];
                                clientn= new String[k];
                                heuren= new String[k];
                                id_clientn= new String[k];
                                adresse= new String[k];

                                //SHOW RESPONSE FROM SERVER
                                j=0;
                                k=0;
                                for(int i=0;i<response.length();i++) {
                                    if(!response.getJSONObject(i).getString("longg").equals("") && !response.getJSONObject(i).getString("lat").equals("") ) {
                                        longg[j] = response.getJSONObject(i).getString("longg");
                                        lat[j] = response.getJSONObject(i).getString("lat");
                                        client[j] = response.getJSONObject(i).getString("nom");
                                        heure[j] = response.getJSONObject(i).getString("heure");
                                        id_client[j] = response.getJSONObject(i).getString("id");
                                        j++;
                                    }
                                    else{
                                        clientn[k] = response.getJSONObject(i).getString("nom");
                                        heuren[k] = response.getJSONObject(i).getString("heure");
                                        id_clientn[k] = response.getJSONObject(i).getString("id");
                                        adresse[k]=response.getJSONObject(i).getString("lieu")+", "+response.getJSONObject(i).getString("zone")+", "+response.getJSONObject(i).getString("wilaya");

                                        k++;

                                    }



                                }
                                if(clientn.length>0){
                                Button b = v.findViewById(R.id.clients);
                                b.setVisibility(View.VISIBLE);
                                b.setText("("+clientn.length+ ") Client(s) sans localisations");
                                    b.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    final Dialog dialog = new Dialog(getContext());
                                                    dialog.setContentView(R.layout.window);
                                                    dialog.setTitle("Clients sans localisation");

                                                    // set the custom dialog components - text, image and button
                                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                                    String x="";
                                                    for(int i=0;i<clientn.length;i++){
                                                        x=x+(i+1)+"- ID :"+id_clientn[i]+"|| Nom :"+clientn[i]+"|| Adresse :"+adresse[i]+"\n";

                                                    }
                                                    text.setText(x);

                                                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                                                    // if button is clicked, close the custom dialog
                                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                    dialog.show();

                                                }
                                            });

                                }
                                drawRoute();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();




                    }


                });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();

    }

    @Override
    public void onPause() {
        super.onPause();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    private void getallpventes(){
        bd.open();
        Cursor c = bd.getAllpventes();
        while (c.moveToNext()) {
int ver=0;

            String longg= c.getString(c.getColumnIndex("longg"));
            String lat= c.getString(c.getColumnIndex("lat"));
            String client= c.getString(c.getColumnIndex("nom"));
            LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(longg));
for (int j=0;j<id_client.length;j++){
    if(Integer.parseInt(id_client[j])==c.getInt(c.getColumnIndex("id")))
        ver=1;
}
if(ver==0) {
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(point);
    markerOptions.title("Client :" + client);
    markerOptions.icon(BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

    mGoogleMap.addMarker(markerOptions);
}

            }
        bd.close();
    }
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private void drawRoute(){

        // Getting URL to the Google Directions API

        if(lat!=null) {
            float[] Distances= new float[lat.length];
            ArrayList<LatLng> mDestination = new ArrayList<LatLng>();
            if (lat.length > 0) {
                LatLng p = new LatLng(latLng.latitude, latLng.longitude);
                mDestination.add(p);

                for (int i = 0; i < lat.length; i++) {
              if(lat[i]!=null) {
                  LatLng point = new LatLng(Double.parseDouble(lat[i]), Double.parseDouble(longg[i]));
                  MarkerOptions mMarkerOptions = new MarkerOptions().position(point).title("Nom : " + client[i] + " Heure : " + heure[i]);
                  mGoogleMap.addMarker(mMarkerOptions);

                  Distances[i] = getDistance(latLng, point);
              }
                }

                int index =getMinIndex(Distances);
                    LatLng point = new LatLng(Double.parseDouble(lat[index]), Double.parseDouble(longg[index]));


                    mDestination.add(point);



                List<String> urls = getDirectionsUrl(mDestination);
                String url = urls.get(0);


                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
          /*  if (urls.size() > 1) {
            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i);


                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
       */
            }
        }}

    public static float getMin(float[] inputArray){
        float minValue = inputArray[0];
        for(int i=1;i<inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = inputArray[i];
            }
        }
        return minValue;
    }

    public static int getMinIndex(float[] inputArray){
        int minValue = 0;
        for(int i=1;i<inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = i;
            }
        }
        return minValue;
    }

    public float getDistance(LatLng p1,LatLng p2) {
                Location loc1 = new Location("");

                loc1.setLatitude(p1.latitude);
                loc1.setLongitude(p1.longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(p2.latitude);
                loc2.setLongitude(p2.longitude);

                float distanceInMeters = loc1.distanceTo(loc2);

                return distanceInMeters;}
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                      if(point!=null & point.get("lat")!=null){
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);}
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mGoogleMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }





    @Override
    public void onConnected(Bundle bundle) {

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
           // mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
          MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Votre position");
            Drawable loc = getResources().getDrawable(R.drawable.loc);
            markerOptions.icon(getMarkerIconFromDrawable( loc));
            mCurrLocation = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(50*1000); //5 seconds
        mLocationRequest.setFastestInterval(25*1000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        if(latLng!=null)
        drawRoute();
    }
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        //remove previous current location marker and add new one at current position
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Votre position");
        Drawable loc = getResources().getDrawable(R.drawable.loc);
        markerOptions.icon(getMarkerIconFromDrawable( loc));
        mCurrLocation = mGoogleMap.addMarker(markerOptions);

        if(latLng!=null)
            drawRoute();
        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

/*
    public void setVisits(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get visits")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                ColorfulRingProgressView crpv = (ColorfulRingProgressView) v.findViewById(R.id.progressBar5);
                                crpv.setPercent(result);
                                TextView t= (TextView) v.findViewById(R.id.tvPercent);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Erreur ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setSuccess(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get success")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                ColorfulRingProgressView crpv = (ColorfulRingProgressView) v.findViewById(R.id.progressBar6);
                                crpv.setPercent(result);
                                TextView t= (TextView) v.findViewById(R.id.tvPercent1);
                                t.setText(((int)result)+"");



                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setDrop(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get drop")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                TextView t = (TextView) v.findViewById(R.id.tvPercent2);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}

    public void setSk(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get sk")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                TextView t = (TextView) v.findViewById(R.id.tvPercent3);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setVisits(String d){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get visits d")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                ColorfulRingProgressView crpv = (ColorfulRingProgressView) v.findViewById(R.id.progressBar5);
                                crpv.setPercent(result);
                                TextView t= (TextView) v.findViewById(R.id.tvPercent);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Erreur ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setSuccess(String d){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get success d")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d)

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                ColorfulRingProgressView crpv = (ColorfulRingProgressView) v.findViewById(R.id.progressBar6);
                                crpv.setPercent(result);
                                TextView t= (TextView) v.findViewById(R.id.tvPercent1);
                                t.setText(((int)result)+"");



                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setDrop(String d){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get drop d")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                TextView t = (TextView) v.findViewById(R.id.tvPercent2);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}



    public void setSk(String d){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get sk d")
                .addBodyParameter("id",id+"")
.addBodyParameter("date",d)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                TextView t = (TextView) v.findViewById(R.id.tvPercent3);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setVisits(String d,String d1){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get visits dd")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d)
                .addBodyParameter("date1",d1)

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                ColorfulRingProgressView crpv = (ColorfulRingProgressView) v.findViewById(R.id.progressBar5);
                                crpv.setPercent(result);
                                TextView t= (TextView) v.findViewById(R.id.tvPercent);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Erreur ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setSuccess(String d,String d1){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get success dd")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d)
                .addBodyParameter("date1",d1)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                ColorfulRingProgressView crpv = (ColorfulRingProgressView) v.findViewById(R.id.progressBar6);
                                crpv.setPercent(result);
                                TextView t= (TextView) v.findViewById(R.id.tvPercent1);
                                t.setText(((int)result)+"");



                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setDrop(String d,String d1){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get drop dd")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d)
                .addBodyParameter("date1",d1)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                TextView t = (TextView) v.findViewById(R.id.tvPercent2);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}




    public void setSk(String d,String d1){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get sk dd")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d)
                .addBodyParameter("date1",d1)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                float result=0;
                                //SHOW RESPONSE FROM SERVER
                                if(response.getJSONObject(0).getString("result")!="null"){
                                    result = parseFloat( response.getJSONObject(0).getString("result"));}
                                TextView t = (TextView) v.findViewById(R.id.tvPercent3);
                                t.setText(((int)result)+"");





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(v.getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(v.getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}
                */


}