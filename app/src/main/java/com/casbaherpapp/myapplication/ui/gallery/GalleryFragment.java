package com.casbaherpapp.myapplication.ui.gallery;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.Adapter;
import com.casbaherpapp.myapplication.Adapter2;
import com.casbaherpapp.myapplication.Adapter4;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.Item;
import com.casbaherpapp.myapplication.Item2;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.MainActivity;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.liv;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.casbaherpapp.myapplication.ui.gventes.gvente.saveFile;

public class GalleryFragment extends Fragment {
    private BDD bd;
    private GalleryViewModel galleryViewModel;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private EditText theFilter;
    private int[] id_l,bon;
    private Integer[] ind;
    private ArrayList<String> communeasc,communefinal;
    private ArrayList<Integer> communecpt;
    private String[] pvente,commune,id_pvente,heure,faite,motif,photo,date,valide,reste,payement;
    private String[][] produit,quantite,quantite_u,montant_bon,endomage,QV,QVU,IDP;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context con;
    private Button D;
    private View v;
    private int tab=1,C=0,pos=-1;
private int idpp,idppp,year,dayOfMonth,month;
private String datetab="",ccfinal="";

    private int id=-2,yaw=0,CC=0;
    private boolean  shouldRefreshOnResume=false;
    public void onResume(){


        super.onResume();
        //update your fragment


        if(shouldRefreshOnResume) {
            if(C==0){
theFilter.setText("");
            ind=null;}
            if(CC==1 && C==0){
                ajouti();

            }
            else if(C==0){
            if(tab==3){
                get_livv();
            }
            else if(tab==2 ){
                get_liv(datetab);

            }
            else{

                bd();
            }}
        }
    }
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        progress = new ProgressDialog(getContext());
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        FloatingActionButton fab = ((Main)getContext()).findViewById(R.id.fab);
        fab.hide();
        theFilter = (EditText) root.findViewById(R.id.searchFilter);


        bd = new BDD(getContext());

       /* final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        mRecyclerView = root.findViewById(R.id.recycler);

        con=root.getContext();
        v=root;
        id = ((Main)getContext()).getData();
        idpp=((Main)getContext()).getidp();

        D = root.findViewById(R.id.datex);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int day) {
                               ProgressBar PP = (ProgressBar)v.findViewById(R.id.progressBar2);
                               TextView text2 = v.findViewById(R.id.value);


                                    PP.setProgress((int) 0);
                                    text2.setText(((int) 0) + "%");
                                m++;
                                String d=y+"-"+m+"-"+day;
                                datetab=y+"-"+m+"-"+day;
                                Calendar calendar = Calendar.getInstance();
                                final int your = calendar.get(Calendar.YEAR);
                                final int mou = calendar.get(Calendar.MONTH)+1;
                                final int dao = calendar.get(Calendar.DAY_OF_MONTH);

                                String dd=convertDate(day)+"-"+convertDate(m)+"-"+y;


                                D.setText(dd+"");

                                theFilter.setText("");
                                year=y;
                                month=m-1;
                                dayOfMonth=day;
                                ind=null;
//progress.show();
                                if(!d.equals(your+"-"+mou+"-"+dao)){
                                get_liv(d);
                                tab=2;}
                                else{
                                bd();
                                tab=1;}
                            }
                        }, year, month, dayOfMonth);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 0);
//Set min time to now
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();}
        });
        progress.show();
        verif();
        if(idpp!=-1){
tab=3;
D.setEnabled(false);
          get_livv();
            ((Main)getContext()).setidp(-1);

        }
        else if(!((Main)getContext()).getdate().equals("")){
            tab=2;
            datetab=((Main)getContext()).getdate();
            get_liv(((Main)getContext()).getdate());
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
            String datee="";
            try {
                datee=format2.format(format1.parse(((Main)getContext()).getdate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            D.setText(datee);
            ((Main)getContext()).setdate();

        }
        else
        {tab=1;
        bd();}
        filt();
Onclick();

if(tab==3){
    Button b=root.findViewById(R.id.commune);
    b.setEnabled(false);
}

        return root;
    }

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    public void get_liv(String d){
        progress.show();
        final int[] x = {0};
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get livraisonsD")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                produit= new String[response.length()][50];
                                quantite= new String[response.length()][50];
                                quantite_u= new String[response.length()][50];
                                IDP= new String[response.length()][50];
                                reste= new String[response.length()];


                                montant_bon= new String[response.length()][50];
                                payement= new String[response.length()];
                                endomage= new String[response.length()][50];
                                QV= new String[response.length()][50];
                                QVU= new String[response.length()][50];
                                id_l= new int[response.length()];

                                bon= new int[response.length()];
                                pvente= new String[response.length()];
                                id_pvente= new String[response.length()];

                                heure= new String[response.length()];
                                faite= new String[response.length()];
                                motif= new String[response.length()];
                               photo= new String[response.length()];

                                date= new String[response.length()];
                                valide= new String[response.length()];



                                bd.open();

                                //SHOW RESPONSE FROM SERVER
                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");

                                    bon[i] = response.getJSONObject(i).getInt("bon");
                                    pvente[i] = response.getJSONObject(i).getString("pvente");
                                    id_pvente[i] = response.getJSONObject(i).getString("id_pvente");

                                    heure[i] = response.getJSONObject(i).getString("heure");
                                    faite[i] = response.getJSONObject(i).getString("faite");

                                    motif[i] = response.getJSONObject(i).getString("motif");
                                    photo[i] = "";


                                    date[i] = response.getJSONObject(i).getString("date");
                                    valide[i] = response.getJSONObject(i).getString("valide");
                                    payement[i] = response.getJSONObject(i).getString("payement");
                                    reste[i] = response.getJSONObject(i).getString("reste");

                                    // bd.InsertLiv(id,id_l[i],pvente[i],produit[i],quantite[i],quantite_u[i],montant_bon[i],heure[i],faite[i],endomage[i],payement[i],motif[i],QV[i],QVU[i],date[i],valide[i],date_m);
                                    final int l=i;
                                    AndroidNetworking.post(DATA_INSERT_URL)
                                            .addBodyParameter("action","get produits")
                                            .addBodyParameter("id",id_l[i]+"")

                                            .setTag("test")
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                    if(response != null)
                                                        try {





                                                            //SHOW RESPONSE FROM SERVER
                                                            for(int j=0;j<response.length();j++) {
                                                                IDP[l][j]=response.getJSONObject(j).getString("id");
                                                                produit[l][j]=response.getJSONObject(j).getString("produit");
                                                                quantite[l][j]=response.getJSONObject(j).getString("quantite");
                                                                quantite_u[l][j]=response.getJSONObject(j).getString("quantite_u");
                                                                montant_bon[l][j]=response.getJSONObject(j).getString("montant_bon");
                                                                endomage[l][j]=response.getJSONObject(j).getString("endomage");
                                                                QV[l][j]=response.getJSONObject(j).getString("quantite_v");
                                                                QVU[l][j]=response.getJSONObject(j).getString("quantite_u_v");



                                                            }
                                                            ajout();
                                                            // String responseString= response.get(0).toString();


                                                        } catch (JSONException e) {


                                                            e.printStackTrace();
                                                            Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                        }

                                                }

                                                //ERROR
                                                @Override
                                                public void onError(ANError anError) {




                                                }


                                            });



                                }
                                progress.dismiss();
                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        id_l= new int[0];
                        bon= new int[0];
                        pvente= new String[0];
                        id_pvente= new String[0];

                        produit= new String[0][0];
                        quantite= new String[0][0];
                        montant_bon= new String[0][0];
                        heure= new String[0];
                        faite= new String[0];
                        motif= new String[0];
                        photo= new String[0];

                        payement= new String[0];
                        endomage= new String[0][0];
                        QV= new String[0][0];
                        reste= new String[0];

                        ajout();
                    }


                });


    }

    public void get_livv(String d){
        progress.show();
        final int[] x = {0};
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get livraisonsD")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                produit= new String[response.length()][50];
                                quantite= new String[response.length()][50];
                                quantite_u= new String[response.length()][50];
                                IDP= new String[response.length()][50];
                                reste= new String[response.length()];


                                montant_bon= new String[response.length()][50];
                                payement= new String[response.length()];
                                endomage= new String[response.length()][50];
                                QV= new String[response.length()][50];
                                QVU= new String[response.length()][50];
                                id_l= new int[response.length()];

                                bon= new int[response.length()];
                                pvente= new String[response.length()];
                                id_pvente= new String[response.length()];

                                heure= new String[response.length()];
                                faite= new String[response.length()];
                                motif= new String[response.length()];
                                photo= new String[response.length()];

                                date= new String[response.length()];
                                valide= new String[response.length()];



                                bd.open();

                                //SHOW RESPONSE FROM SERVER
                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");

                                    bon[i] = response.getJSONObject(i).getInt("bon");
                                    pvente[i] = response.getJSONObject(i).getString("pvente");
                                    id_pvente[i] = response.getJSONObject(i).getString("id_pvente");

                                    heure[i] = response.getJSONObject(i).getString("heure");
                                    faite[i] = response.getJSONObject(i).getString("faite");

                                    motif[i] = response.getJSONObject(i).getString("motif");
                                    photo[i] = "";


                                    date[i] = response.getJSONObject(i).getString("date");
                                    valide[i] = response.getJSONObject(i).getString("valide");
                                    payement[i] = response.getJSONObject(i).getString("payement");
                                    reste[i] = response.getJSONObject(i).getString("reste");


                                    String date_m =response.getJSONObject(i).getString("date_m");

                                    // bd.InsertLiv(id,id_l[i],pvente[i],produit[i],quantite[i],quantite_u[i],montant_bon[i],heure[i],faite[i],endomage[i],payement[i],motif[i],QV[i],QVU[i],date[i],valide[i],date_m);
                                    final int l=i;
                                    AndroidNetworking.post(DATA_INSERT_URL)
                                            .addBodyParameter("action","get produits")
                                            .addBodyParameter("id",id_l[i]+"")

                                            .setTag("test")
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                    if(response != null)
                                                        try {





                                                            //SHOW RESPONSE FROM SERVER
                                                            for(int j=0;j<response.length();j++) {
                                                                IDP[l][j]=response.getJSONObject(j).getString("id");
                                                                produit[l][j]=response.getJSONObject(j).getString("produit");
                                                                quantite[l][j]=response.getJSONObject(j).getString("quantite");
                                                                quantite_u[l][j]=response.getJSONObject(j).getString("quantite_u");
                                                                montant_bon[l][j]=response.getJSONObject(j).getString("montant_bon");
                                                                endomage[l][j]=response.getJSONObject(j).getString("endomage");
                                                                QV[l][j]=response.getJSONObject(j).getString("quantite_v");
                                                                QVU[l][j]=response.getJSONObject(j).getString("quantite_u_v");



                                                            }
                                                            ajoutii();
                                                            // String responseString= response.get(0).toString();


                                                        } catch (JSONException e) {


                                                            e.printStackTrace();
                                                            Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                        }

                                                }

                                                //ERROR
                                                @Override
                                                public void onError(ANError anError) {




                                                }


                                            });



                                }
                                progress.dismiss();
                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        id_l= new int[0];
                        bon= new int[0];
                        pvente= new String[0];
                        id_pvente= new String[0];

                        produit= new String[0][0];
                        quantite= new String[0][0];
                        montant_bon= new String[0][0];
                        heure= new String[0];
                        faite= new String[0];
                        motif= new String[0];
                        photo= new String[0];

                        payement= new String[0];
                        endomage= new String[0][0];
                        QV= new String[0][0];
                        reste= new String[0];

                        ajout();
                    }


                });


    }
    public void get_livv(){
        bd.open();
        int id=bd.getID();
        bd.close();

        progress.show();
        final int[] x = {0};
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get livraisonsP")
                .addBodyParameter("id",idpp+"")
                .addBodyParameter("id_livreur",id+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                produit= new String[response.length()][50];
                                quantite= new String[response.length()][50];
                                quantite_u= new String[response.length()][50];
                                IDP= new String[response.length()][50];
                                reste= new String[response.length()];


                                montant_bon= new String[response.length()][50];
                                payement= new String[response.length()];
                                endomage= new String[response.length()][50];
                                QV= new String[response.length()][50];
                                QVU= new String[response.length()][50];
                                id_l= new int[response.length()];

                                bon= new int[response.length()];
                                pvente= new String[response.length()];
                                id_pvente= new String[response.length()];

                                heure= new String[response.length()];
                                faite= new String[response.length()];
                                motif= new String[response.length()];
                                photo= new String[response.length()];

                                date= new String[response.length()];
                                valide= new String[response.length()];



                                bd.open();

                                //SHOW RESPONSE FROM SERVER
                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");

                                    bon[i] = response.getJSONObject(i).getInt("bon");
                                    pvente[i] = response.getJSONObject(i).getString("pvente");
                                    id_pvente[i] = response.getJSONObject(i).getString("id_pvente");

                                    heure[i] = response.getJSONObject(i).getString("heure");
                                    faite[i] = response.getJSONObject(i).getString("faite");

                                    motif[i] = response.getJSONObject(i).getString("motif");
                                    photo[i] = "";

                                    date[i] = response.getJSONObject(i).getString("date");
                                    valide[i] = response.getJSONObject(i).getString("valide");
                                    payement[i] = response.getJSONObject(i).getString("payement");
                                    reste[i] = response.getJSONObject(i).getString("reste");


                                    String date_m =response.getJSONObject(i).getString("date_m");

                                    // bd.InsertLiv(id,id_l[i],pvente[i],produit[i],quantite[i],quantite_u[i],montant_bon[i],heure[i],faite[i],endomage[i],payement[i],motif[i],QV[i],QVU[i],date[i],valide[i],date_m);
                                    final int l=i;
                                    AndroidNetworking.post(DATA_INSERT_URL)
                                            .addBodyParameter("action","get produits")
                                            .addBodyParameter("id",id_l[i]+"")

                                            .setTag("test")
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                    if(response != null)
                                                        try {





                                                            //SHOW RESPONSE FROM SERVER
                                                            for(int j=0;j<response.length();j++) {
                                                                IDP[l][j]=response.getJSONObject(j).getString("id");
                                                                produit[l][j]=response.getJSONObject(j).getString("produit");
                                                                quantite[l][j]=response.getJSONObject(j).getString("quantite");
                                                                quantite_u[l][j]=response.getJSONObject(j).getString("quantite_u");
                                                                montant_bon[l][j]=response.getJSONObject(j).getString("montant_bon");
                                                                endomage[l][j]=response.getJSONObject(j).getString("endomage");
                                                                QV[l][j]=response.getJSONObject(j).getString("quantite_v");
                                                                QVU[l][j]=response.getJSONObject(j).getString("quantite_u_v");



                                                            }
                                                            ajout();
                                                            // String responseString= response.get(0).toString();


                                                        } catch (JSONException e) {


                                                            e.printStackTrace();
                                                            Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                        }

                                                }

                                                //ERROR
                                                @Override
                                                public void onError(ANError anError) {




                                                }


                                            });



                                }
                                progress.dismiss();
                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        id_l= new int[0];
                        bon= new int[0];
                        pvente= new String[0];
                        id_pvente= new String[0];

                        produit= new String[0][0];
                        quantite= new String[0][0];
                        montant_bon= new String[0][0];
                        heure= new String[0];
                        faite= new String[0];
                        motif= new String[0];
                        photo= new String[0];

                        payement= new String[0];
                        endomage= new String[0][0];
                        QV= new String[0][0];
                        reste= new String[0];

                        ajout();
                    }


                });


    }

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
                                    Intent intent1 = new Intent(v.getContext(),MainActivity.class);
                                    startActivity(intent1);
                                    getActivity().finish();

                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(v.getContext(), "Pas de connexion", Toast.LENGTH_SHORT).show();

                        }


                    });     }

    }



    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector mGestureDetector;
        private OnItemClickListener mListener;
        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    /*public void get_liv(){
        final int[] x = {0};
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get livraisons")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                produit= new String[response.length()][50];
                                quantite= new String[response.length()][50];
                                quantite_u= new String[response.length()][50];
                                IDP= new String[response.length()][50];
                                reste= new String[response.length()][50];

                                montant_bon= new String[response.length()][50];
                                payement= new String[response.length()][50];
                                endomage= new String[response.length()][50];
                                QV= new String[response.length()][50];
                                QVU= new String[response.length()][50];
                                id_l= new int[response.length()];
                                bon= new int[response.length()];
                                pvente= new String[response.length()];

                                heure= new String[response.length()];
                                faite= new String[response.length()];
                                motif= new String[response.length()];

                                date= new String[response.length()];
                                valide= new String[response.length()];




                                bd.open();


                                //SHOW RESPONSE FROM SERVER
                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");
                                   bon[i] = response.getJSONObject(i).getInt("bon");
                                    pvente[i] = response.getJSONObject(i).getString("pvente");

                                    heure[i] = response.getJSONObject(i).getString("heure");
                                    faite[i] = response.getJSONObject(i).getString("faite");

                                    motif[i] = response.getJSONObject(i).getString("motif");

                                    date[i] = response.getJSONObject(i).getString("date");
                                    valide[i] = response.getJSONObject(i).getString("valide");


                                      String date_m =response.getJSONObject(i).getString("date_m");

                                    //bd.InsertLiv(id,id_l[i],response.getJSONObject(i).getString("id_pvente"),bon[i],pvente[i],heure[i],faite[i],motif[i],date[i],valide[i],date_m,"non");
final int l=i;
                                    AndroidNetworking.post(DATA_INSERT_URL)
                                            .addBodyParameter("action","get produits")
                                            .addBodyParameter("id",id_l[i]+"")

                                            .setTag("test")
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                    if(response != null)
                                                        try {




                                                            bd.open();


                                                            //SHOW RESPONSE FROM SERVER
                                                            for(int j=0;j<response.length();j++) {
                                                                Log.e( "BDD",l+"_"+j);
                                                                IDP[l][j]=response.getJSONObject(j).getString("id");;

                                                                produit[l][j]=response.getJSONObject(j).getString("produit");;
                                                               quantite[l][j]=response.getJSONObject(j).getString("quantite");;
                                                                quantite_u[l][j]=response.getJSONObject(j).getString("quantite_u");;

                                                                montant_bon[l][j]=response.getJSONObject(j).getString("montant_bon");;
                                                                payement[l][j]=response.getJSONObject(j).getString("payement");;
                                                               endomage[l][j]=response.getJSONObject(j).getString("endomage");;
                                                               QV[l][j]=response.getJSONObject(j).getString("quantite_v");;
                                                                QVU[l][j]=response.getJSONObject(j).getString("quantite_u_v");;
                                                                reste[l][j]=response.getJSONObject(j).getString("reste");
                                                                //bd.Insertproduitv(Integer.parseInt(IDP[l][j]),id,id_l[l],produit[l][j],quantite[l][j],quantite_u[l][j],montant_bon[l][j],payement[l][j],endomage[l][j],QV[l][j],QVU[l][j],reste[l][j],date[l]);



                                                            }
                                                            bd.close();

                                                            ajout();
                                                            // String responseString= response.get(0).toString();


                                                        } catch (JSONException e) {


                                                            e.printStackTrace();
                                                            Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                        }

                                                }

                                                //ERROR
                                                @Override
                                                public void onError(ANError anError) {




                                                }


                                            });



                                }
                                bd.close();
                                progress.dismiss();
                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                       bd();
                        progress.dismiss();
                    }


                });


    }*/

   public void bd(){
        progress.dismiss();
        bd.open();

        Cursor c =bd.getLiv(id);
        id_l= new int[c.getCount()];
       bon= new int[c.getCount()];
       pvente= new String[c.getCount()];
       id_pvente= new String[c.getCount()];

       heure= new String[c.getCount()];
        faite= new String[c.getCount()];
        motif= new String[c.getCount()];
       photo= new String[c.getCount()];

       reste= new String[c.getCount()];
       payement= new String[c.getCount()];
       date= new String[c.getCount()];


       int count=c.getCount();

       produit= new String[count][50];
       quantite= new String[count][50];
       quantite_u= new String[count][50];
       IDP= new String[count][50];

       montant_bon= new String[count][50];
       endomage= new String[count][50];
       QV= new String[count][50];
       QVU= new String[count][50];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));
            pvente[i] =c.getString(c.getColumnIndex("pvente"));
            id_pvente[i] =c.getString(c.getColumnIndex("id_pvente"));

            bon[i]=c.getInt(c.getColumnIndex("bon"));
            heure[i] =c.getString(c.getColumnIndex("heure"));
            faite[i] = c.getString(c.getColumnIndex("faite"));
            motif[i] = c.getString(c.getColumnIndex("motif"));
           photo[i] = c.getString(c.getColumnIndex("photo"));

            reste[i]=c.getString(c.getColumnIndex("reste"));
            payement[i]=c.getString(c.getColumnIndex("payement"));
            date[i]=c.getString(c.getColumnIndex("date"));

                Cursor cc = bd.getproduitv(id_l[i]);

            int j=0;
                while (cc.moveToNext()){


                    IDP[i][j]=cc.getInt(cc.getColumnIndex("id"))+"";

                produit[i][j]=cc.getString(cc.getColumnIndex("produit"));
                quantite[i][j]=cc.getString(cc.getColumnIndex("quantite"));
                quantite_u[i][j]=cc.getString(cc.getColumnIndex("quantite_u"));

                montant_bon[i][j]=cc.getString(cc.getColumnIndex("montant_bon"));
                endomage[i][j]=cc.getString(cc.getColumnIndex("endomage"));
                QV[i][j]=cc.getString(cc.getColumnIndex("quantite_v"));
                QVU[i][j]=cc.getString(cc.getColumnIndex("quantite_u_v"));
                j++;


            }





            i++;
        }
       ajout();

        bd.close();
        if(i!=0){

        }
        else {
            Toast.makeText(getContext(), "Il y a pas de livraisons où vous n'avez pas télécharger les données", Toast.LENGTH_SHORT).show();
        }



    }
    public void bdd(int idp){

        progress.dismiss();
        bd.open();

        Cursor c =bd.getLivv(id,idp);
        id_l= new int[c.getCount()];
        bon= new int[c.getCount()];
        pvente= new String[c.getCount()];
        id_pvente= new String[c.getCount()];

        heure= new String[c.getCount()];
        faite= new String[c.getCount()];
        motif= new String[c.getCount()];
        photo= new String[c.getCount()];

        reste= new String[c.getCount()];
        payement= new String[c.getCount()];


        int count=c.getCount();

        produit= new String[count][50];
        quantite= new String[count][50];
        quantite_u= new String[count][50];
        IDP= new String[count][50];

        montant_bon= new String[count][50];
        endomage= new String[count][50];
        QV= new String[count][50];
        QVU= new String[count][50];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));
            pvente[i] =c.getString(c.getColumnIndex("pvente"));
            id_pvente[i] =c.getString(c.getColumnIndex("id_pvente"));

            bon[i]=c.getInt(c.getColumnIndex("bon"));
            heure[i] =c.getString(c.getColumnIndex("heure"));
            faite[i] = c.getString(c.getColumnIndex("faite"));
            motif[i] = c.getString(c.getColumnIndex("motif"));
            photo[i] = c.getString(c.getColumnIndex("photo"));

            payement[i]=c.getString(c.getColumnIndex("payement"));
            reste[i]=c.getString(c.getColumnIndex("reste"));


            Cursor cc = bd.getproduitv(id_l[i]);
            int j=0;
            while (cc.moveToNext()){
                IDP[i][j]=cc.getInt(cc.getColumnIndex("id"))+"";

                produit[i][j]=cc.getString(cc.getColumnIndex("produit"));
                quantite[i][j]=cc.getString(cc.getColumnIndex("quantite"));
                quantite_u[i][j]=cc.getString(cc.getColumnIndex("quantite_u"));

                montant_bon[i][j]=cc.getString(cc.getColumnIndex("montant_bon"));
                endomage[i][j]=cc.getString(cc.getColumnIndex("endomage"));
                QV[i][j]=cc.getString(cc.getColumnIndex("quantite_v"));
                QVU[i][j]=cc.getString(cc.getColumnIndex("quantite_u_v"));
                j++;
                ajout();

            }





            i++;
        }

        bd.close();
        if(i!=0){

        }
        else {
            Toast.makeText(getContext(), "Il y a pas de livraisons où vous n'avez pas télécharger les données", Toast.LENGTH_SHORT).show();
        }



    }
public void filt(){

    theFilter.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int k, int i1, int i2) {
            int cpt=0,p=0;
           ind= new Integer[id_l.length];
            String[] pv;
            String q="";
            Double m=0.0;
            pv=new String[id_l.length];
            ArrayList<Item> exampleList = new ArrayList<>();
            for(int i=0;i<id_l.length;i++) {
if(CC==0){
                if(pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase()) || id_pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase())) {

                    ind[cpt]=i;
                    cpt++;



                    for (int j = 0; j < produit[i].length; j++) {


                        if ((quantite[i][j] != null) && (produit[i][j] != null)) {


                            q = q + quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                            p = p + 1;
                        }
                        if (montant_bon[i][j] != null) {
                            m = m + Double.parseDouble(montant_bon[i][j]);
                        }

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
                    }
                    exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
                    q = "";
                    p = 0;
                    m = 0.0;
                }}
else{
    if(ccfinal.equals(commune[i])){
    if(pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase()) || id_pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase())) {

        ind[cpt]=i;
        cpt++;



        for (int j = 0; j < produit[i].length; j++) {


            if ((quantite[i][j] != null) && (produit[i][j] != null)) {


                q = q + quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                p = p + 1;
            }
            if (montant_bon[i][j] != null) {
                m = m + Double.parseDouble(montant_bon[i][j]);
            }

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
        }
        exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
        q = "";
        p = 0;
        m = 0.0;
    }}

}
            }


            mRecyclerView = v.findViewById(R.id.recycler);

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(con);
            mAdapter = new Adapter(exampleList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);





        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });


}
public void Onclick(){
    final Button pvv = v.findViewById(R.id.commune);
    pvv.setOnClickListener(
            new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
        if(C==0) {
            C = 1;
            CC=0;
            Button pvv = v.findViewById(R.id.commune);
            pvv.getBackground().setColorFilter(R.color.black, PorterDuff.Mode.MULTIPLY);
            pvv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.appart, 0);

            int pro=0;
            ProgressBar PP = (ProgressBar)v.findViewById(R.id.progressBar2);
            TextView text2 = v.findViewById(R.id.value);



            for(int y=0;y<id_l.length;y++){
                if(faite[y].equals("oui"))
                    pro++;
            }
            if(id_l.length>0) {
                double x = (pro * 100) / (id_l.length);
                PP.setProgress((int) x);
                text2.setText(((int) x) + "%");
            }


            ArrayList<Item2> exampleList = new ArrayList<>();
            communefinal = new ArrayList<>();

            communeasc = new ArrayList<>();
            communecpt = new ArrayList<>();

            int cpt = 0;
            for (int i = 0; i < commune.length; i++) {
                if (!communeasc.contains(commune[i]) || communeasc.size() == 0)
                    cpt = 1;
                else
                    cpt = 0;
                for (int k = i + 1; k < commune.length; k++) {
                    if (commune[i].equals(commune[k]) && (!communeasc.contains(commune[i]) || communeasc.size() == 0)) {
                        cpt++;
                    }

                }
                if (cpt > 0) {
                    communeasc.add(commune[i]);
                    communecpt.add(cpt);
                    communefinal.add(commune[i]);

                }

            }
            Collections.sort(communecpt, Collections.reverseOrder());
            communeasc = new ArrayList<>();
            ArrayList<String> communefinall = new ArrayList<>();
            ArrayList<Integer> index = new ArrayList<>();


            for (int i = 0; i < commune.length; i++) {
                if (!communeasc.contains(commune[i]) || communeasc.size() == 0)
                    cpt = 1;
                else
                    cpt = 0;
                for (int k = i + 1; k < commune.length; k++) {
                    if (commune[i].equals(commune[k]) && (!communeasc.contains(commune[i]) || communeasc.size() == 0)) {
                        cpt++;
                    }

                }
                if (cpt > 0) {
                    for (int f = 0; f < communecpt.size(); f++) {
                        if (communecpt.get(f) == cpt && !communefinall.contains(commune[i]) && !index.contains(f)) {
                            communefinall.add(commune[i]);
                            communeasc.add(commune[i]);
                            communefinal.set(f, commune[i]);
                            index.add(f);

                        }

                    }
                }


            }

            for (int i = 0; i < communecpt.size(); i++) {

                exampleList.add(new Item2("", communefinal.get(i),  communecpt.get(i) + "",""));
            }

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(con);
            Adapter4 mAdapter = new Adapter4(exampleList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

        }
        else{
            C=0;
            Button pvv = v.findViewById(R.id.commune);
            pvv.getBackground().setColorFilter(null);
            pvv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.appart, 0);

            if(tab==1){

                bd();
            }
            else if(tab==2){
                get_liv(datetab);
            }
        }
                }
            });


    mRecyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(con, new RecyclerItemClickListener.OnItemClickListener() {
        public void onItemClick(View v, int position){

         if(C==0){
            Intent myIntent = new Intent(getActivity(), liv.class);
            List<String> pr= new ArrayList<>() ;
            List<String> q= new ArrayList<>() ;
            List<String> qu= new ArrayList<>() ;
            List<String> mon= new ArrayList<>() ;
            List<String> qv= new ArrayList<>() ;
            List<String> quv= new ArrayList<>() ;
            List<String> pay= new ArrayList<>() ;
            List<String> end= new ArrayList<>() ;
            List<String> idp= new ArrayList<>() ;
            List<String> rest= new ArrayList<>() ;
            if(ind!=null)
                position=ind[position];



            for(int i=0;i<50;i++) {
                if (produit[position][i] != null)
                    pr.add(produit[position][i]);
                if (quantite[position][i] != null)
                    q.add(quantite[position][i]);
                if (quantite_u[position][i] != null)
                    qu.add(quantite_u[position][i]);
                if (montant_bon[position][i] != null)
                    mon.add(montant_bon[position][i]);
                if (QV[position][i] != null)
                    qv.add(QV[position][i]);
                if (QVU[position][i] != null)
                    quv.add(QVU[position][i]);
                if (endomage[position][i] != null)
                    end.add(endomage[position][i]);
                if (IDP[position][i] != null)
                    idp.add(IDP[position][i]);


            }
            myIntent.putExtra("id_l", id_l[position]+"");
            myIntent.putExtra("bon", bon[position]+"");
            myIntent.putExtra("reste", reste[position]+"");
            myIntent.putExtra("payement", payement[position]+"");

            myIntent.putExtra("idlivreur", id+"");

            myIntent.putExtra("pvente", pvente[position]+"");



            myIntent.putStringArrayListExtra("produit", (ArrayList<String>) pr);

            myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idp);


            myIntent.putStringArrayListExtra("quantite", (ArrayList<String>)q);
            myIntent.putStringArrayListExtra("quantite_u", (ArrayList<String>)qu);

            myIntent.putStringArrayListExtra("montant", (ArrayList<String>) mon);
            myIntent.putStringArrayListExtra("quantite_v", (ArrayList<String>) qv);
            myIntent.putStringArrayListExtra("quantite_u_v", (ArrayList<String>) quv);

            myIntent.putExtra("motif", motif[position]+"");
            myIntent.putStringArrayListExtra("endomage", (ArrayList<String>) end);

            Bitmap decodedByte=null;
            String c="";
            if(!photo[position].equals("") && !photo[position].toLowerCase().equals("null")){
                //String s = photo[position].replace("data:image/png;base64,","");

                byte[] decodedString = Base64.decode(photo[position], Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                c="cc";
            }
            if(decodedByte!=null)
            saveFilee(getContext(),decodedByte,"myImage.jpg");

            myIntent.putExtra("photo", c+"");

            myIntent.putExtra("user", "non");
            if(tab==1)
            myIntent.putExtra("tab", 4+"");
            else
             myIntent.putExtra("tab", tab+"");



            startActivity(myIntent);}

         else{
             Button pvv = getActivity().findViewById(R.id.commune);

             pvv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back, 0, R.drawable.appart, 0);
if(tab==1) {
    bd();
    C = 0;
    CC = 1;

    int cpt = 0;
    String[] pv;
    String q = "";
    int p = 0;
    Double m = 0.0;
    pv = new String[id_l.length];
    ArrayList<Item> exampleList = new ArrayList<>();
    commune = new String[id_l.length];
    int cptt = 0;
    for (int i = 0; i < id_l.length; i++) {
        bd.open();
        if (!bd.getcommune(id_pvente[i]).equals(""))
            commune[i] = bd.getcommune(id_pvente[i]);
        else {
            commune[i] = "Pas vos clients";
        }
        ccfinal = communefinal.get(position);
        bd.close();
        if (commune[i].equals(communefinal.get(position))) {

            produit[cptt] = produit[i];
            quantite[cptt] = quantite[i];
            quantite_u[cptt] = quantite_u[i];
            montant_bon[cptt] = montant_bon[i];
            QV[cptt] = QV[i];
            QVU[cptt] = QVU[i];
            endomage[cptt] = endomage[i];
            IDP[cptt] = IDP[i];
            id_l[cptt] = id_l[i];
            bon[cptt] = bon[i];
            reste[cptt] = reste[i];
            payement[cptt] = payement[i];

            pvente[cptt] = pvente[i];
            motif[cptt] = motif[i];

            cptt++;


            if (faite[i].equals("oui")) {
                cpt++;

            }
            for (int j = 0; j < produit[i].length; j++) {


                if ((quantite[i][j] != null) && (produit[i][j] != null)) {


                    q = q + quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                    p = p + 1;
                }
                if (montant_bon[i][j] != null) {
                    m = m + Double.parseDouble(montant_bon[i][j]);
                }

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
            }


            exampleList.add(new Item("Bon :" + bon[i] + ", heure : " + heure[i], "Nb de produits :" + p + ", total :" + payement[i] + " DA", "", "N°:" + id_pvente[i] + ", " + pvente[i], faite[i]));
            q = "";
            p = 0;
            m = 0.0;
        }

    }
    ProgressBar PP = (ProgressBar) getActivity().findViewById(R.id.progressBar2);
    TextView text2 = getActivity().findViewById(R.id.value);
    if (id_l.length > 0) {
        double x = (cpt * 100) / (id_l.length);
        PP.setProgress((int) x);
        text2.setText(((int) x) + "%");
    }


    enter();
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(con);
    mAdapter = new Adapter(exampleList);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);

}
else{
    pos=position;
    get_livv(datetab);
}

         }



        }


    }));







}

public void ajoutii(){
    C = 0;
    CC = 1;

    int cpt = 0;
    String[] pv;
    String q = "";
    int p = 0;
    Double m = 0.0;
    pv = new String[id_l.length];
    ArrayList<Item> exampleList = new ArrayList<>();
    commune = new String[id_l.length];
    int cptt = 0;
    for (int i = 0; i < id_l.length; i++) {
        bd.open();
        if (!bd.getcommune(id_pvente[i]).equals(""))
            commune[i] = bd.getcommune(id_pvente[i]);
        else {
            commune[i] = "Pas vos clients";
        }
        ccfinal = communefinal.get(pos);
        bd.close();
        if (commune[i].equals(communefinal.get(pos))) {

            produit[cptt] = produit[i];
            quantite[cptt] = quantite[i];
            quantite_u[cptt] = quantite_u[i];
            montant_bon[cptt] = montant_bon[i];
            QV[cptt] = QV[i];
            QVU[cptt] = QVU[i];
            endomage[cptt] = endomage[i];
            IDP[cptt] = IDP[i];
            id_l[cptt] = id_l[i];
            bon[cptt] = bon[i];
            reste[cptt] = reste[i];
            payement[cptt] = payement[i];

            pvente[cptt] = pvente[i];
            motif[cptt] = motif[i];

            cptt++;


            if (faite[i].equals("oui")) {
                cpt++;

            }
            for (int j = 0; j < produit[i].length; j++) {


                if ((quantite[i][j] != null) && (produit[i][j] != null)) {


                    q = q + quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                    p = p + 1;
                }
                if (montant_bon[i][j] != null) {
                    m = m + Double.parseDouble(montant_bon[i][j]);
                }

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
            }


            exampleList.add(new Item("Bon :" + bon[i] + ", heure : " + heure[i], "Nb de produits :" + p + ", total :" + payement[i] + " DA", "", "N°:" + id_pvente[i] + ", " + pvente[i], faite[i]));
            q = "";
            p = 0;
            m = 0.0;
        }

    }
    ProgressBar PP = (ProgressBar) getActivity().findViewById(R.id.progressBar2);
    TextView text2 = getActivity().findViewById(R.id.value);
    if (id_l.length > 0) {
        double x = (cpt * 100) / (id_l.length);
        PP.setProgress((int) x);
        text2.setText(((int) x) + "%");
    }


    enter();
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(con);
    mAdapter = new Adapter(exampleList);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
}
public void ajouti(){

    C=0;

    int cpt=0;
    String[] pv;
    String q="";
    int p=0;
    Double m=0.0;
    pv=new String[id_l.length];
    ArrayList<Item> exampleList = new ArrayList<>();
    commune=new String[id_l.length];
    int cptt=0;
    for(int i=0;i<id_l.length;i++) {
        bd.open();
        if(!bd.getcommune(id_pvente[i]).equals(""))
            commune[i]=bd.getcommune(id_pvente[i]);
        else{
            commune[i]="Pas vos clients";
        }

        bd.close();
        if(commune[i].equals(ccfinal)){
            produit[cptt]=produit[i];
            quantite[cptt]=quantite[i];
            quantite_u[cptt]=quantite_u[i];
            montant_bon[cptt]=montant_bon[i];
            QV[cptt]=QV[i];
            QVU[cptt]=QVU[i];
            endomage[cptt]=endomage[i];
            IDP[cptt]=IDP[i] ;
            id_l[cptt]=id_l[i];
            bon[cptt]=bon[i];
            reste[cptt]=reste[i];
            payement[cptt]=payement[i];

            pvente[cptt]=pvente[i];
            motif[cptt]=motif[i];

            cptt++;



            if (faite[i].equals("oui")) {
                cpt++;

            }
            for(int j=0;j<produit[i].length;j++) {


                if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                    q=q+quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                    p=p+1;}
                if(montant_bon[i][j]!=null){
                    m=m+Double.parseDouble(montant_bon[i][j]);}

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
            }


            exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
            q="";
            p=0;
            m=0.0;}

    }
    ProgressBar PP = (ProgressBar)getActivity().findViewById(R.id.progressBar2);
    TextView text2 = getActivity().findViewById(R.id.value);
    if(id_l.length>0) {
        double x = (cpt * 100) / (id_l.length);
        PP.setProgress((int) x);
        text2.setText(((int) x) + "%");
    }


    enter();
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(con);
    mAdapter = new Adapter(exampleList);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
}
    public static void saveFilee(Context context, Bitmap b, String picName){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "io exception");
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void ajout(){
        int cpt=0;
        String[] pv;
        String q="";
        int p=0;
        Double m=0.0;
        pv=new String[id_l.length];
        ArrayList<Item> exampleList = new ArrayList<>();
        commune=new String[id_l.length];
        for(int i=0;i<id_l.length;i++) {
            bd.open();
            if(!bd.getcommune(id_pvente[i]).equals(""))
            commune[i]=bd.getcommune(id_pvente[i]);
            else{
                commune[i]="Pas vos clients";
            }

            bd.close();


            if (faite[i].equals("oui")) {
                cpt++;

            }
            for(int j=0;j<produit[i].length;j++) {


                if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                    q=q+quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                p=p+1;}
                if(montant_bon[i][j]!=null){
                m=m+Double.parseDouble(montant_bon[i][j]);}

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
            }


            exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
            q="";
p=0;
            m=0.0;

        }
         ProgressBar PP = (ProgressBar)v.findViewById(R.id.progressBar2);
        TextView text2 = v.findViewById(R.id.value);
if(id_l.length>0) {
    double x = (cpt * 100) / (id_l.length);
    PP.setProgress((int) x);
    text2.setText(((int) x) + "%");
}


        enter();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);





    }

    public void enter(){

      if(Main.getidCreditt()>0){
            TextView textView = getActivity().findViewById(R.id.searchFilter);
            textView.setText("");

            Intent myIntent = new Intent(getActivity(), liv.class);
            List<String> pr= new ArrayList<>() ;
            List<String> q= new ArrayList<>() ;
            List<String> qu= new ArrayList<>() ;
            List<String> mon= new ArrayList<>() ;
            List<String> qv= new ArrayList<>() ;
            List<String> quv= new ArrayList<>() ;
            List<String> pay= new ArrayList<>() ;
            List<String> end= new ArrayList<>() ;
            List<String> idp= new ArrayList<>() ;
            List<String> rest= new ArrayList<>() ;
            List<String> b= new ArrayList<>() ;

            int position=0;
            for(int j=0;j<id_l.length;j++){
                if(id_l[j]==Main.getidCreditt())
                    position=j;
            }
            Main.setidCreditt(0);



            for(int i=0;i<50;i++) {

                if (produit[position][i] != null)
                    pr.add(produit[position][i]);
                if (quantite[position][i] != null)
                    q.add(quantite[position][i]);
                if (quantite_u[position][i] != null)
                    qu.add(quantite_u[position][i]);
                if (montant_bon[position][i] != null)
                    mon.add(montant_bon[position][i]);
                if (QV[position][i] != null)
                    qv.add(QV[position][i]);
                if (QVU[position][i] != null)
                    quv.add(QVU[position][i]);

                if (endomage[position][i] != null)
                    end.add(endomage[position][i]);
                if (IDP[position][i] != null)
                    idp.add(IDP[position][i]);


            }
            myIntent.putExtra("id_l", id_l[position]+"");
            myIntent.putExtra("bon", bon[position]+"");

            myIntent.putExtra("idlivreur", id+"");

            myIntent.putExtra("pvente", pvente[position]+"");
            myIntent.putExtra("reste", reste[position]+"");
            myIntent.putExtra("payement", payement[position]+"");


            myIntent.putStringArrayListExtra("produit", (ArrayList<String>) pr);

            myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idp);


            myIntent.putStringArrayListExtra("quantite", (ArrayList<String>)q);
            myIntent.putStringArrayListExtra("quantite_u", (ArrayList<String>)qu);

            myIntent.putStringArrayListExtra("montant", (ArrayList<String>) mon);
            myIntent.putStringArrayListExtra("quantite_v", (ArrayList<String>) qv);
            myIntent.putStringArrayListExtra("quantite_u_v", (ArrayList<String>) quv);

            myIntent.putExtra("motif", motif[position]+"");
            myIntent.putStringArrayListExtra("endomage", (ArrayList<String>) end);

            Bitmap decodedByte=null;
            String c="";
            if(photo[position]!=null){
                if(!photo[position].equals("") && !photo[position].toLowerCase().equals("null")){                    //String s = photo[position].replace("data:image/png;base64,","");

                    byte[] decodedString = Base64.decode(photo[position], Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    c="cc";
                }}
            if(decodedByte!=null)
                saveFile(getContext(),decodedByte,"myImage.jpg");

            myIntent.putExtra("photo", c+"");
          myIntent.putExtra("tab", "4");



            startActivity(myIntent);


        }
    }

    public void ajoutt(){
        int cpt=0,p=0;
        String[] pv;
        String q="";
        double r=0;
        Double m=0.0;
        pv=new String[id_l.length];
        ArrayList<Item> exampleList = new ArrayList<>();
        for(int i=0;i<id_l.length;i++) {
            if (faite[i].equals("oui")) {
                cpt++;

            }
            for(int j=0;j<produit[i].length;j++) {


                if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                    q=q+quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                    p=p+0;}


          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
            }
            if(reste[i]!=null){
                r=r+Double.parseDouble(reste[i]);}
            exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
            q="";
            p=0;
            r=0;

        }
        ProgressBar PP = (ProgressBar)v.findViewById(R.id.progressBar2);
      TextView text2 = v.findViewById(R.id.value);
        if(id_l.length>0) {
            double x = (cpt * 100) / (id_l.length);
            PP.setProgress((int) x);
            text2.setText(((int) x) + "%");
        }



        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);





    }



}