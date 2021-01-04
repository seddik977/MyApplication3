package com.casbaherpapp.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.ui.gallery.GalleryFragment;
import com.casbaherpapp.myapplication.ui.gventes.gvente;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;


public class modpvente extends AppCompatActivity {

    private static final String DATA_INSERT_URL = "http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress;
    private int[] id_l, id_l1;
    private String[] produit, prix_u, prix_f;
    private RecyclerView mRecyclerView, mRecyclerView1;
    private Adapter2 mAdapter;
    private Adapter3 mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] nom, num, adresse, zone, ID, credit, montant, quantite, quantite_u, endomage, QV, QVU, produits, IDP, quantite_f;

    private String b, payement, reste;
    private Integer[] ind, ind1, select, selected;
    private Button ajout, supprimer, modifier, appel, liv;
    private TextInputEditText n, a,  nm;
    private AutoCompleteTextView w,z,communeTV;
    private ArrayAdapter<String> communeAdapter;

    private BDD bd;
    private int id = -1, yaw = 0, idp, idpv = -1, posp, pospv, s = 0, id_livraison, bon, idselect;
    private String pvente = "",typ,selection;
    private FloatingActionButton f;

    private FusedLocationProviderClient fusedLocationClient;
    private Spinner P;
    private boolean veri = false, verii = false;
    private List<String> arraySpinner;

    public void onCreate(Bundle savedInstanceState) {
        bd = new BDD(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(modpvente.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.modpvente);
        bd = new BDD(modpvente.this);
        progress = new ProgressDialog(this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);

        n = findViewById(R.id.nom);
        a = findViewById(R.id.adresse);
        z = findViewById(R.id.zone);
        nm = findViewById(R.id.num);
        w = findViewById(R.id.wilaya);
        final String[] COUNTRIES = new String[]{"Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaia", "Biskra", "Béchar", "Blida", "Bouira", "Tamanrasset", "Tébéessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saida", "Skikda", "Sidi Bel Abbes", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara", "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdes", "El Tarf", "Tindouf", "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Ain Defla", "Naama", "Ain Témouchent", "Ghardaia", "Relizane"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        COUNTRIES);

        final AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.wilaya);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid (CharSequence text){
                //some logic here returns true or false based on if the text is validated
                AutoCompleteTextView drop = findViewById(R.id.wilaya);
                ArrayAdapter<String> adapter= (ArrayAdapter<String>) drop.getAdapter();

                String[] items = new String[COUNTRIES.length];
                for(int i=0 ; i<COUNTRIES.length ; i++){
                    items[i]= COUNTRIES[i];
                }

                Arrays.sort(items);


                if (Arrays.binarySearch(items, text.toString()) > 0)
                    return true;
                else
                    return false;
            }

            @Override
            public CharSequence fixText (CharSequence invalidText){
                //If .isValid() returns false then the code comes here
                //do whatever way you want to fix in the users input and  return it
                return "";
            }
        });



        supprimer = findViewById(R.id.supprimer);
        modifier = findViewById(R.id.modifier);
        appel = findViewById(R.id.appel);
        liv = findViewById(R.id.liv);


        P = findViewById(R.id.Spinner);
        arraySpinner = new ArrayList<>();
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
        if (extras.containsKey("id")) {
            id = Integer.parseInt(i.getStringExtra("id"));


        }
        if (extras.containsKey("branche")) {
            b = i.getStringExtra("branche");


        }
        if (extras.containsKey("nom")) {

            n.setText(i.getStringExtra("nom"));

        }
        if (extras.containsKey("num")) {

            nm.setText(i.getStringExtra("num"));

        }

        if (extras.containsKey("adresse")) {

            a.setText(i.getStringExtra("adresse"));

        }
        if (extras.containsKey("zone")) {

            z.setText(i.getStringExtra("zone"));

        }
        if (extras.containsKey("wilaya")) {

            w.setText(i.getStringExtra("wilaya"));

        }
        if (extras.containsKey("type")) {

            if (i.getStringExtra("type") != "" && i.getStringExtra("type") != null) {
                int typ = 5;

                if (i.getStringExtra("type").equals("Supérette")) {
                    typ = 0;
                } else if (i.getStringExtra("type").equals("Réstaurant")) {
                    typ = 1;

                } else if (i.getStringExtra("type").equals("Fast food")) {
                    typ = 2;

                } else if (i.getStringExtra("type").equals("Grossiste")) {
                    typ = 3;

                } else if (i.getStringExtra("type").equals("Epicerie")) {
                    typ = 4;

                }
                else {
                    TextInputLayout x=findViewById(R.id.will) ;
                    x.setVisibility(View.VISIBLE);
                    TextInputEditText typp=findViewById(R.id.typee);
                    typp.setText(i.getStringExtra("type"));
                }
                P.setSelection(typ);
            }

        }

        if (extras.containsKey("idp")) {


            idp = Integer.parseInt(i.getStringExtra("idp"));
            TextInputEditText c=findViewById(R.id.idclient);

            bd.open();
            boolean bb = bd.getmodifier(idp + "");
            bd.close();
            if (!bb){
            c.setText("CAS"+id+"-"+idp);}
        }

        bd.open();
        Cursor c = bd.getpvente(idp);
while(c.moveToNext()){


    n.setText(c.getString(c.getColumnIndex("nom")));
    nm.setText(c.getString(c.getColumnIndex("num"))+"");
    a.setText(c.getString(c.getColumnIndex("adresse")));
    z.setText( c.getString(c.getColumnIndex("zone")));
    w.setText(c.getString(c.getColumnIndex("wilaya")));


   selection = c.getString(c.getColumnIndex("wilaya"));

    if (c.getString(c.getColumnIndex("type")) != "" && c.getString(c.getColumnIndex("type"))  != null) {
        int typ = 5;

        if (c.getString(c.getColumnIndex("type")) .equals("Supérette")) {
            typ = 0;
        } else if (c.getString(c.getColumnIndex("type")) .equals("Réstaurant")) {
            typ = 1;

        } else if (c.getString(c.getColumnIndex("type")) .equals("Fast food")) {
            typ = 2;

        } else if (c.getString(c.getColumnIndex("type")) .equals("Grossiste")) {
            typ = 3;

        } else if (c.getString(c.getColumnIndex("type")) .equals("Epicerie")) {
            typ = 4;

        }
        else {
            TextInputLayout x=findViewById(R.id.will) ;
            x.setVisibility(View.VISIBLE);
            TextInputEditText typp=findViewById(R.id.typee);
            typp.setText(c.getString(c.getColumnIndex("type")) );
        }
        P.setSelection(typ);



}}
bd.close();








        Onclick();


        communeTV= (AutoCompleteTextView) findViewById(R.id.zone);

        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) editTextFilledExposedDropdown.getText().toString();
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
                communeAdapter = new ArrayAdapter<>(modpvente.this,R.layout.list_item,communes);
                communeTV.setAdapter(communeAdapter);

                communeTV.showDropDown();
            }
        });

        communeTV.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid (CharSequence text){
                //some logic here returns true or false based on if the text is validated

                ArrayAdapter<String> communeAdapter= (ArrayAdapter<String>) communeTV.getAdapter();
                if(communeAdapter!=null){
                    String selection = (String) editTextFilledExposedDropdown.getText().toString();
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
                            if (communesJsonAray.getString("wilaya_id").equals(String.valueOf(pos+ 1))) {
                                String nom = communesJsonAray.getString("nom");
                                communes.add(nom);
                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] items = new String[communes.size()];
                    for(int i=0 ; i<communes.size() ; i++){
                        items[i]= communes.get(i);
                    }
                    Arrays.sort(items);

                    if (Arrays.binarySearch(items, text.toString()) > 0)
                        return true;
                    else
                        return false;}
                return true;
            }

            @Override
            public CharSequence fixText (CharSequence invalidText){
                //If .isValid() returns false then the code comes here
                //do whatever way you want to fix in the users input and  return it
                return "";
            }
        });


        int pos = -1;

        for (int j = 0; j < COUNTRIES.length; j++) {
            if (COUNTRIES[j].equals(selection)) {
                pos = j;
                break;
            }
        }
        List<String> communes = new ArrayList<String>();
        try {
            JSONArray communesJsonArray = new JSONArray(loadJSONFromAsset("communes.json"));
            for(int k=0;k<communesJsonArray.length();k++) {
                JSONObject communesJsonAray = communesJsonArray.getJSONObject(k);
                if (communesJsonAray.getString("wilaya_id").equals(String.valueOf(pos+ 1))) {
                    String nommm = communesJsonAray.getString("nom");
                    communes.add(nommm);
                }


            }


        } catch (JSONException ee) {
            ee.printStackTrace();
        }
        communeAdapter = new ArrayAdapter<>(modpvente.this,R.layout.list_item,communes);
        communeTV.setAdapter(communeAdapter);
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

    public void Onclick() {


        supprimer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (idp == -1) {
                            Toast.makeText(modpvente.this, "Selectionner un point de vente", Toast.LENGTH_SHORT).show();

                        } else {



                            new AlertDialog.Builder(modpvente.this)
                                    .setMessage("Etes vous sur de supprimer le point de vente ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            bd.open();
                                            bd.supprimerpvente(idp);
                                            bd.close();
                                            bd.open();
                                            boolean bb = bd.getmodifier(idp + "");
                                            bd.close();
                                            if (!bb) {
                                                progress.show();
                                                supprimer.setEnabled(false);
                                                AndroidNetworking.post(DATA_INSERT_URL)
                                                        .addBodyParameter("action", "supprimer pvente")
                                                        .addBodyParameter("id", idp + "")


                                                        .setTag("test")
                                                        .setPriority(Priority.MEDIUM)
                                                        .build()
                                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {

                                                                if (response != null)
                                                                    try {

                                                                        progress.dismiss();
                                                                        bd.open();


                                                                        //SHOW RESPONSE FROM SERVER

                                                                        bd.close();
                                                                        supprimer.setEnabled(true);

                                                                        //bd();
                                                                        String responseString = response.get(0).toString();
                                                                        idp = -1;
                                                                        a.setText("");
                                                                        n.setText("");
                                                                        nm.setText("");
                                                                        z.setText("");
                                                                        finish();


                                                                    } catch (JSONException e) {
                                                                        supprimer.setEnabled(true);

                                                                        progress.dismiss();
                                                                        e.printStackTrace();
                                                                        Toast.makeText(modpvente.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                                    }

                                                            }

                                                            //ERROR
                                                            @Override
                                                            public void onError(ANError anError) {
                                                                progress.dismiss();
                                                                supprimer.setEnabled(true);
                                                                Toast.makeText(modpvente.this, "Pas de connexion", Toast.LENGTH_SHORT).show();

                                                                anError.printStackTrace();

                                                            }


                                                        });

                                            }
                                        }
                                    })
                                    .setNegativeButton("Non", null)
                                    .show();

                        }


                    }
                });

        modifier.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextInputEditText typp=findViewById(R.id.typee);
                       modifier.setFocusableInTouchMode(true);
                        modifier.requestFocus();
                        modifier.setFocusableInTouchMode(false);
                        typ=P.getSelectedItem().toString();
                        if(P.getSelectedItemPosition()==5)
                            typ=typp.getText().toString();
                        if (idp == -1) {
                            Toast.makeText(modpvente.this, "Selectionner un point de vente", Toast.LENGTH_SHORT).show();


                        } else if (n.getText().toString().isEmpty() || w.getText().toString().isEmpty() || nm.getText().toString().isEmpty() || a.getText().toString().isEmpty() || z.getText().toString().isEmpty()) {
                            Toast.makeText(modpvente.this, "Remplissez tous les champs SVP", Toast.LENGTH_SHORT).show();


                        }
                        else if(P.getSelectedItemPosition()==5 && typ.isEmpty()){
                            Toast.makeText(modpvente.this, "Remplissez le type SVP!", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                           modifier.setEnabled(true);

                        }else {


                            new AlertDialog.Builder(modpvente.this)
                                    .setMessage("etes vous sur de modifier le point de vente ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            bd.open();
                                            boolean bb = bd.getmodifier(idp + "");
                                            bd.close();
                                            if (!bb) {

                                                progress.show();
                                                modifier.setEnabled(false);

                                                if (ActivityCompat.checkSelfPermission(modpvente.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(modpvente.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    ActivityCompat#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for ActivityCompat#requestPermissions for more details.
                                                    return;
                                                }
                                                fusedLocationClient.getLastLocation()
                                                        .addOnSuccessListener(modpvente.this, new OnSuccessListener<Location>() {

                                                            public void onSuccess(final Location location) {
                                                                // Got last known location. In some rare situations this can be null.
                                                                if (location != null) {

                                                                    verii = true;
                                                                    final String longg = location.getLongitude() + "";
                                                                    final String latt = location.getLatitude() + "";


                                                                    AndroidNetworking.post(DATA_INSERT_URL)
                                                                            .addBodyParameter("action", "mod pvente1")
                                                                            .addBodyParameter("branche", b + "")
                                                                            .addBodyParameter("lieu", a.getText() + "")
                                                                            .addBodyParameter("zone", z.getText() + "")
                                                                            .addBodyParameter("nom", n.getText() + "")
                                                                            .addBodyParameter("num", nm.getText() + "")
                                                                            .addBodyParameter("wilaya", w.getText() + "")
                                                                            .addBodyParameter("type", typ + "")
                                                                            .addBodyParameter("lat", latt)
                                                                            .addBodyParameter("longg", longg)
                                                                            .addBodyParameter("id", idp + "")

                                                                            .setTag("test")
                                                                            .setPriority(Priority.MEDIUM)
                                                                            .build()
                                                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                                                @Override
                                                                                public void onResponse(JSONArray response) {

                                                                                    if (response != null)
                                                                                        try {

                                                                                            progress.dismiss();
                                                                                            bd.open();


                                                                                            //SHOW RESPONSE FROM SERVER

                                                                                            bd.close();
                                                                                            modifier.setEnabled(true);

                                                                                            //bd();
                                                                                            String responseString = response.get(0).toString();
                                                                                            if(responseString.equals("Success")){
                                                                                                ddd(idp);
                                                                                                idp = -1;
                                                                                                a.setText("");
                                                                                                n.setText("");
                                                                                                nm.setText("");
                                                                                                z.setText("");
                                                                                                w.setText("");

                                                                                                finish();}
                                                                                            else{
                                                                                                Toast.makeText(modpvente.this, "Changer le nom où le numéro, il existe un pvente avec les memes infos", Toast.LENGTH_SHORT).show();

                                                                                            }


                                                                                        } catch (JSONException e) {
                                                                                            ddinsert(idp);
                                                                                            modifier.setEnabled(true);
                                                                                            verii = false;

                                                                                            progress.dismiss();
                                                                                            e.printStackTrace();
                                                                                            Toast.makeText(modpvente.this, "Changer le nom, il existe un pvente de mm nom ", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                }

                                                                                //ERROR
                                                                                @Override
                                                                                public void onError(ANError anError) {
                                                                                    ddinsert(idp);
                                                                                    progress.dismiss();
                                                                                    modifier.setEnabled(true);
                                                                                    finish();
                                                                                    verii = false;

                                                                                    anError.printStackTrace();

                                                                                }


                                                                            });


                                                                } else {

                                                                    AndroidNetworking.post(DATA_INSERT_URL)
                                                                            .addBodyParameter("action", "mod pvente")
                                                                            .addBodyParameter("id", idp + "")
                                                                            .addBodyParameter("branche", b + "")
                                                                            .addBodyParameter("lieu", a.getText() + "")
                                                                            .addBodyParameter("zone", z.getText() + "")
                                                                            .addBodyParameter("nom", n.getText() + "")
                                                                            .addBodyParameter("num", nm.getText() + "")

                                                                            .addBodyParameter("wilaya", w.getText() + "")
                                                                            .addBodyParameter("type", typ + "")


                                                                            .setTag("test")
                                                                            .setPriority(Priority.MEDIUM)
                                                                            .build()
                                                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                                                @Override
                                                                                public void onResponse(JSONArray response) {

                                                                                    if (response != null)
                                                                                        try {
                                                                                            progress.dismiss();
                                                                                            bd.open();


                                                                                            //SHOW RESPONSE FROM SERVER

                                                                                            bd.close();
                                                                                            modifier.setEnabled(true);

                                                                                            //bd();
                                                                                            String responseString = response.get(0).toString();
                                                                                            if(responseString.equals("Success")){
                                                                                                ddd(idp);
                                                                                            idp = -1;
                                                                                            a.setText("");
                                                                                            n.setText("");
                                                                                            nm.setText("");
                                                                                            z.setText("");
                                                                                            w.setText("");

                                                                                            finish();}
                                                                                            else{
                                                                                                Toast.makeText(modpvente.this, "Changer le nom où le numéro, il existe un pvente avec les memes infos", Toast.LENGTH_SHORT).show();

                                                                                            }

                                                                                        } catch (JSONException e) {
                                                                                            modifier.setEnabled(true);
                                                                                            ddinsert(idp);
                                                                                            progress.dismiss();

                                                                                            e.printStackTrace();
                                                                                            Toast.makeText(modpvente.this, "Changer le nom où le numéro, il existe un pvente avec les memes infos", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                }

                                                                                //ERROR
                                                                                @Override
                                                                                public void onError(ANError anError) {
                                                                                    ddinsert(idp);
                                                                                    progress.dismiss();
                                                                                    modifier.setEnabled(true);
                                                                                    finish();

                                                                                    anError.printStackTrace();

                                                                                }


                                                                            });
                                                                }

                                                            }
                                                        });


                                            }
                                            else{
                                                dddu(idp);
                                               // Toast.makeText(modpvente.this, "Veuillez synchroniser d'abord", Toast.LENGTH_SHORT).show();

                                            }


                                        }
                                    })
                                    .setNegativeButton("Non", null)
                                    .show();

                        }









                    }
                });

        appel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progress.show();
                        appel.setEnabled(false);
                        if(idp==-1){
                            Toast.makeText(modpvente.this, "Selectionner un point de vente", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            appel.setEnabled(true);

                        }

                        else {
                            AndroidNetworking.post(DATA_INSERT_URL)
                                    .addBodyParameter("action","insert appel")
                                    .addBodyParameter("id",id+"")
                                    .addBodyParameter("pvente",idp+"")

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

                                                    bd.close();
                                                    progress.dismiss();
                                                    appel.setEnabled(true);
                                                    String responseString= response.get(0).toString();
                                                    onCallBtnClick();






                                                } catch (JSONException e) {
                                                    progress.dismiss();
                                                    appel.setEnabled(true);
                                                    e.printStackTrace();
                                                    Toast.makeText(modpvente.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                }

                                        }

                                        //ERROR
                                        @Override
                                        public void onError(ANError anError) {
                                            progress.dismiss();
                                            appel.setEnabled(true);

                                            anError.printStackTrace();

                                        }


                                    });

                        }


                    }
                });
        liv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progress.show();
                        liv.setEnabled(false);
                        if(idp==-1){
                            Toast.makeText(modpvente.this, "Selectionner un point de vente", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            liv.setEnabled(true);

                        }

                        else{
                            progress.dismiss();
                            liv.setEnabled(true);
                            Main.setidp(idp);
                            Main.setCreditt(1);
                            finish();

                        }









                    }
                });

Button qr=findViewById(R.id.qr);
        qr.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
bd.open();
String mod=bd.getmodifier(idp);
bd.close();
if(mod!=null) {
    if (!mod.equals("oui")) {
        Intent myIntent = new Intent(modpvente.this, qr.class);


        myIntent.putExtra("id", idp + "");

        startActivity(myIntent);

    }
}
else{
    Intent myIntent = new Intent(modpvente.this, qr.class);


    myIntent.putExtra("id", idp + "");

    startActivity(myIntent);

}



                    }
                });


        final Button pvv = findViewById(R.id.pventee);

        pvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                pvv.setEnabled(false);
                if(idp==-1){
                    Toast.makeText(modpvente.this, "Selectionner un point de vente", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    pvv.setEnabled(true);

                }

                else{
                    AndroidNetworking.post(DATA_INSERT_URL)
                            .addBodyParameter("action","get point")
                            .addBodyParameter("id",idp+"")
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

                                            bd.close();
                                            progress.dismiss();
                                            pvv.setEnabled(true);

                                            String uri = "http://maps.google.com/maps?daddr=" +response.getJSONObject(0).getString("lat") + "," + response.getJSONObject(0).getString("longg") + " (" + response.getJSONObject(0).getString("nom") + ")";
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                            intent.setPackage("com.google.android.apps.maps");
                                            startActivity(intent);



                                        } catch (JSONException e) {
                                            progress.dismiss();
                                            pvv.setEnabled(true);
                                            e.printStackTrace();
                                            Toast.makeText(modpvente.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                        }

                                }

                                //ERROR
                                @Override
                                public void onError(ANError anError) {
                                    progress.dismiss();
                                    pvv.setEnabled(true);

                                    anError.printStackTrace();

                                }


                            });
                  /*  progress.dismiss();
                    pvv.setEnabled(true);
                   Intent myIntent = new Intent(getContext(), pv.class);



                    myIntent.putExtra("id", idp+"");
                    myIntent.putExtra("id", idp+"");

                    startActivity(myIntent);*/


                }




            }
        });
    }



    public void ddd(int id){

        int bon =1,id_liv=1,idp=1;
        bd.open();


        bd.insertpvente(b,id,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",0+"");



        progress.dismiss();
        bd.close();

    }

    public void dddu(int id){

        int bon =1,id_liv=1,idp=1;
        bd.open();


        bd.insertpventeu(b,id,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",0+"");



        progress.dismiss();
        bd.close();

        finish();

    }

    public void dddlong(int id,String longg,String lat){

        int bon =1,id_liv=1,idp=1;
        bd.open();


        bd.insertpventelong(b,id,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",0+"",longg,lat);



        progress.dismiss();
        bd.close();

    }



    public void ddinsert(int id){

        int bon =1,id_liv=1,idp=1;
        bd.open();


        bd.insertpventeee(b,id,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",0+"");



        progress.dismiss();
        bd.close();

    }

    public void ddinsertlong(int id,String longg,String lat){

        int bon =1,id_liv=1,idp=1;
        bd.open();


        bd.insertpventeeelong(b,id,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),typ+"",z.getText().toString(),"0",0+"",longg,lat);



        progress.dismiss();
        bd.close();

    }
    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(modpvente.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(modpvente.this, PERMISSIONS_STORAGE, 9);
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
            Toast.makeText(modpvente.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }



    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(modpvente.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+nm.getText()));
            startActivity(callIntent);
        }else{
            Toast.makeText(modpvente.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }



}
