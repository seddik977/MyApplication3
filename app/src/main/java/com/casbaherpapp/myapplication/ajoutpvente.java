package com.casbaherpapp.myapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ajoutpvente extends AppCompatActivity  {

    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int[] id_l,id_l1;
    private String[] produit,prix_u,prix_f;
    private RecyclerView mRecyclerView, mRecyclerView1;
    private Adapter2 mAdapter;
    private Adapter3 mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] nom,num,adresse,zone,ID,credit,montant,quantite,quantite_u,endomage,QV,QVU,produits,IDP,quantite_f;

    private String b,payement,reste, typ;
    private Integer[] ind,ind1,select,selected;
    private Button ajout,supprimer,modifier,appel,liv;
    private TextInputEditText n,a,nm;
    private    AutoCompleteTextView w,z,communeTV;
    private ArrayAdapter<String> communeAdapter;
    private BDD bd;
    private int id=-1,yaw=0,idp,idpv=-1,posp,pospv,s=0,id_livraison,bon,idselect,v=0;
    private String pvente="";
    private FloatingActionButton f;

    private FusedLocationProviderClient fusedLocationClient;
    private Spinner P;
    private boolean veri=false,verii=false;
    private List<String> arraySpinner;

    public void onCreate(Bundle savedInstanceState) {
        bd = new BDD(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ajoutpvente.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajoutpvente);
        bd = new BDD(ajoutpvente.this);
        progress = new ProgressDialog(this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);

        n= findViewById(R.id.nom);
        a= findViewById(R.id.adresse);
        z= findViewById(R.id.zone);
        nm= findViewById(R.id.num);
        w= findViewById(R.id.wilaya);
        final String[] COUNTRIES = new String[] {"Adrar","Chlef","Laghouat","Oum El Bouaghi","Batna","Béjaia","Biskra","Béchar","Blida","Bouira","Tamanrasset","Tébéessa","Tlemcen","Tiaret","Tizi Ouzou","Alger","Djelfa","Jijel","Sétif","Saida","Skikda","Sidi Bel Abbes","Annaba","Guelma","Constantine","Médéa","Mostaganem","M'Sila","Mascara","Ouargla","Oran","El Bayadh","Illizi","Bordj Bou Arreridj","Boumerdes","El Tarf","Tindouf","Tissemsilt","El Oued","Khenchela","Souk Ahras","Tipaza","Mila","Ain Defla","Naama","Ain Témouchent","Ghardaia","Relizane"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        COUNTRIES);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.wilaya);
        editTextFilledExposedDropdown.setAdapter(adapter);
        ajout= findViewById(R.id.ajout);



        P = findViewById(R.id.Spinner);
        arraySpinner= new ArrayList<>();
        arraySpinner.add("Supérette");
        arraySpinner.add("Réstaurant");
        arraySpinner.add("Fast food");
        arraySpinner.add("Grossiste");
        arraySpinner.add("Epicerie");
        arraySpinner.add("Autre");





        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        P.setAdapter(adapterr);

        Intent i = getIntent();

        Bundle extras = i.getExtras();
        if(extras.containsKey("id")) {
            id = Integer.parseInt(i.getStringExtra("id"));


        }
        if(extras.containsKey("branche")) {
            b = i.getStringExtra("branche");


        }

bd.open();
id=bd.getID();
b=bd.getb();
bd.close();
Onclick();

        communeTV= (AutoCompleteTextView) findViewById(R.id.zone);

        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                int pos = -1;

                for (int i = 0; i < COUNTRIES.length; i++) {
                    if (COUNTRIES[i].equals(selection)) {
                        pos = i;
                        break;
                    }
                }
                List<String> communes = new ArrayList<String>();
                try {
                    JSONArray communesJsonArray = new JSONArray(loadJSONFromAsset("communes.json"));
                    for(int i=0;i<communesJsonArray.length();i++) {
                        JSONObject communesJsonAray = communesJsonArray.getJSONObject(i);
                        if (communesJsonAray.getString("wilaya_id").equals(String.valueOf(pos + 1))) {
                            String nom = communesJsonAray.getString("nom");
                            communes.add(nom);
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                communeAdapter = new ArrayAdapter<>(ajoutpvente.this,R.layout.list_item,communes);
                communeTV.setAdapter(communeAdapter);

                communeTV.showDropDown();
            }
        });

sp();
    }
    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.communes);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void sp() {


        P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {

if(pos==5){
    TextInputLayout x=findViewById(R.id.will) ;
    x.setVisibility(View.VISIBLE);
 /*   TextInputLayout uxx=findViewById(R.id.valeurrrr) ;
    final RelativeLayout.LayoutParams lppp = (RelativeLayout.LayoutParams) uxx.getLayoutParams();
    lppp.addRule(RelativeLayout.BELOW, R.id.will);
    uxx.setLayoutParams(lppp);*/
}
else {
    TextInputLayout x=findViewById(R.id.will) ;
    x.setVisibility(View.GONE);
  /*  TextInputLayout uxx=findViewById(R.id.valeurrrr) ;
    final RelativeLayout.LayoutParams lppp = (RelativeLayout.LayoutParams) uxx.getLayoutParams();
    lppp.addRule(RelativeLayout.BELOW, R.id.Spinner);
    uxx.setLayoutParams(lppp);*/
}


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void Onclick(){

        ajout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progress.show();
                        veri=false;
                        ajout.setEnabled(false);
                        TextInputEditText typp=findViewById(R.id.typee);
                       typ=P.getSelectedItem().toString();
                        if(P.getSelectedItemPosition()==5)
                            typ=typp.getText().toString();


                        if(n.getText().toString().isEmpty() || w.getText().toString().isEmpty() ||nm.getText().toString().isEmpty() || a.getText().toString().isEmpty() || z.getText().toString().isEmpty()){
                            Toast.makeText(ajoutpvente.this, "Remplissez tous les champs SVP!", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            ajout.setEnabled(true);

                        }
                        else if(P.getSelectedItemPosition()==5 && typ.isEmpty()){
                            Toast.makeText(ajoutpvente.this, "Remplissez le type SVP", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            ajout.setEnabled(true);

                        }
                        else{
                           v=0;
                            bd.open();
                            Cursor c=bd.getpventeMod();
                            while(c.moveToNext()){
                                v=1;

                            }



                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(ajoutpvente.this, new OnSuccessListener<Location>() {

                                        public void onSuccess(final Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null ) {
                                                veri = true;
                                                final String longg = location.getLongitude() + "";
                                                final String latt = location.getLatitude() + "";
                                                if(v==1){
                                                    ddinsertlong(longg,latt);
                                                    finish();
                                                }
                                                else {


                                                    AndroidNetworking.post(DATA_INSERT_URL)
                                                            .addBodyParameter("action", "add pvente1")
                                                            .addBodyParameter("branche", b + "")
                                                            .addBodyParameter("lieu", a.getText() + "")
                                                            .addBodyParameter("zone", z.getText() + "")
                                                            .addBodyParameter("nom", n.getText() + "")
                                                            .addBodyParameter("num", nm.getText() + "")
                                                            .addBodyParameter("wilaya", w.getText() + "")
                                                            .addBodyParameter("type", typ+ "")
                                                            .addBodyParameter("lat", latt)
                                                            .addBodyParameter("longg", longg)
                                                            .addBodyParameter("id_livreur", id + "")


                                                            .setTag("test")
                                                            .setPriority(Priority.MEDIUM)
                                                            .build()
                                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                                @Override
                                                                public void onResponse(JSONArray response) {

                                                                    if (response != null)
                                                                        try {
                                                                            String responseString = response.get(1).toString();
                                                                            ddlong(responseString,longg,latt);
                                                                            progress.dismiss();
                                                                            bd.open();


                                                                            //SHOW RESPONSE FROM SERVER

                                                                            bd.close();
                                                                            ajout.setEnabled(true);
                                                                            finish();

                                                                            //bd();


                                                                        } catch (JSONException e) {
                                                                            ddinsertlong(longg,latt);
                                                                            finish();
                                                                            ajout.setEnabled(true);


                                                                            progress.dismiss();
                                                                            e.printStackTrace();
                                                                            Toast.makeText(ajoutpvente.this, "Changer le nom où le numéro, il existe un pvente avec les memes infos ", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                }

                                                                //ERROR
                                                                @Override
                                                                public void onError(ANError anError) {
                                                                    ddinsertlong(longg,latt);
                                                                    finish();
                                                                    progress.dismiss();
                                                                    ajout.setEnabled(true);


                                                                    anError.printStackTrace();

                                                                }


                                                            });

                                                }

                                            }
                                           else{
                                                if(v==1){
                                                    ddinsert();
                                                    finish();
                                                }
                                                else {

                                                veri=true;
                                                AndroidNetworking.post(DATA_INSERT_URL)
                                                        .addBodyParameter("action","add pvente")
                                                        .addBodyParameter("id_livreur",id+"")
                                                        .addBodyParameter("branche",b+"")
                                                        .addBodyParameter("lieu",a.getText()+"")
                                                        .addBodyParameter("zone",z.getText()+"")
                                                        .addBodyParameter("nom",n.getText()+"")
                                                        .addBodyParameter("num",nm.getText()+"")
                                                        .addBodyParameter("wilaya",w.getText()+"")
                                                        .addBodyParameter("type",typ+"")
                                                        .setTag("test")
                                                        .setPriority(Priority.MEDIUM)
                                                        .build()
                                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {

                                                                if(response != null)
                                                                    try {
                                                                        String responseString= response.get(1).toString();
                                                                        dd(responseString);

                                                                        progress.dismiss();
                                                                        bd.open();




                                                                        //SHOW RESPONSE FROM SERVER

                                                                        bd.close();
                                                                        ajout.setEnabled(true);
                                                                        finish();
                                                                        //bd();



                                                                    } catch (JSONException e) {
                                                                        ddinsert();
                                                                        finish();
                                                                        ajout.setEnabled(true);

                                                                        progress.dismiss();
                                                                        e.printStackTrace();
                                                                        Toast.makeText(ajoutpvente.this, "Changer le nom où le numéro, il existe un pvente avec les memes infos", Toast.LENGTH_SHORT).show();
                                                                    }

                                                            }

                                                            //ERROR
                                                            @Override
                                                            public void onError(ANError anError) {
                                                                ddinsert();
                                                                finish();
                                                                progress.dismiss();
                                                                ajout.setEnabled(true);

                                                                anError.printStackTrace();

                                                            }


                                                        });}}
                                        }
                                    });



                        }


                    }
                });


    }

    public void dd(String id_liv){

        int bon =1,idp=1;
        bd.open();



        int id=bd.getID();

        bd.insertpvente(b,Integer.parseInt(id_liv),n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",id+"");



        progress.dismiss();
        bd.close();

    }
    public void ddlong(String id_liv,String longg,String lat){

        int bon =1,idp=1;
        bd.open();



        int id=bd.getID();

        bd.insertpventelong(b,Integer.parseInt(id_liv),n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",id+"",longg,lat);



        progress.dismiss();
        bd.close();

    }
    public void ddinsert(){

        int bon =1,id_liv=1,idp=1;
        bd.open();
        Cursor c=bd.getpventee();

        while (c.moveToNext()){
            id_liv = c.getInt(c.getColumnIndex("id"))+1;
        }


        int id=bd.getID();

        bd.insertpventee(b,id_liv,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",id+"");



        progress.dismiss();
        bd.close();

    }
    public void ddinsertlong(String longg,String lat){

        int bon =1,id_liv=1,idp=1;
        bd.open();
        Cursor c=bd.getpventee();

        while (c.moveToNext()){
            id_liv = c.getInt(c.getColumnIndex("id"))+1;
        }


        int id=bd.getID();

        bd.insertpventeelong(b,id_liv,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",id+"",longg,lat);



        progress.dismiss();
        bd.close();

    }
    public void ddd(int id){
        bd.open();

        int idd=bd.getID();

        int bon =1,id_liv=1,idp=1;
        bd.open();


        bd.insertpvente(b,id,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",idd+"");



        progress.dismiss();
        bd.close();

    }





    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(ajoutpvente.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(ajoutpvente.this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(ajoutpvente.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }



    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(ajoutpvente.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+nm.getText()));
            startActivity(callIntent);
        }else{
            Toast.makeText(ajoutpvente.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }



}
