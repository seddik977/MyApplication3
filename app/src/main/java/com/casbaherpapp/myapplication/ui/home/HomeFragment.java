package com.casbaherpapp.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.MainActivity;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.TouchyWebView;
import com.casbaherpapp.myapplication.pv;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static java.lang.Float.parseFloat;

public class HomeFragment extends Fragment  {

    private HomeViewModel homeViewModel;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
private int id=-1;
    private View v;
    private Calendar cal;
    private String auj;
    private ProgressDialog progress;
    private BDD bd;
    private FusedLocationProviderClient fusedLocationClient;

    private boolean  shouldRefreshOnResume=false;
    public void onResume(){


        super.onResume();
        //update your fragment
        if(shouldRefreshOnResume) {
            NavigationView navigationView = ((Main)getContext()).findViewById(R.id.nav_view);
            navigationView.getMenu().performIdentifierAction(R.id.nav_home,1);}
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
        bd= new BDD(root.getContext());
        progress = new ProgressDialog(getContext());
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        progress.show();

        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH)+1;
       int mDay = cal.get(Calendar.DAY_OF_MONTH);
       auj=mYear+"-"+mMonth+"-"+mDay;

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
        v=root;
        id = ((Main)getContext()).getData();
        FloatingActionButton fab = ((Main)getContext()).findViewById(R.id.fab);
        fab.show();
        verif();

        setzone();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        map();


        return root;
    }





    public void setInfos(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get infos")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                //SHOW RESPONSE FROM SERVER
                                TextView nn=v.findViewById(R.id.nomm);
                                nn.setText(response.getJSONObject(0).getString("nom"));
                                TextView m=getView().findViewById(R.id.maill);
                                m.setText(response.getJSONObject(0).getString("mail"));





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(getContext(), "Erreur ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}
    public void verif(){
        bd.open();
        String user=bd.getuser();
        String  pass=bd.getpassword();

        bd.close();
        if(user!="" && pass!=""){
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","login")
                    .addBodyParameter("user",user)
                    .addBodyParameter("password",pass)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null)
                                try {

                                    id=response.getJSONObject(0).getInt("id");



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


                    });     }

    }

    public void setzone(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get zone")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                //SHOW RESPONSE FROM SERVER
                                TextView nn=((Main)getActivity()).findViewById(R.id.zone);
                                nn.setText("Zone d'aujourd'hui : "+response.getJSONObject(0).getString("zone"));

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


                });}






    public void map(){

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {

                    public void onSuccess(final Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {


                            String longg = location.getLongitude() + "";
                            String latt = location.getLatitude() + "";
                            TouchyWebView myWebView = v.findViewById(R.id.webview);
                            WebSettings webSettings = myWebView.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            String url = "http://casbahdz.com/casbahtemplate/vendeurr.php";
                            String postData = null;
                            try {
                                postData = "id=" + URLEncoder.encode(String.valueOf(id), "UTF-8")+"&lat="+URLEncoder.encode(latt, "UTF-8")+"&longg="+URLEncoder.encode(longg, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            myWebView.postUrl(url,postData.getBytes());
                        }
                    }
                });



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