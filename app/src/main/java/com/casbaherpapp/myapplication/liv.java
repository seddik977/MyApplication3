package com.casbaherpapp.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static java.security.AccessController.getContext;


public class liv extends AppCompatActivity {


    private static final int CAMERA_REQUEST_CODE = 1450;
    private String mCurrentPhotoPath;
    private ImageView ivCameraPreview;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_PERMISSION_CODE = 1460;
    private int id_l;
    private String quantite, pvente, produit, montant;
    private TextInputEditText Q, QU, QV, QVU, PV, M, ID, V, reste, PAY;
    private Spinner P;
    private static final String DATA_INSERT_URL = "http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress;
    private FloatingActionButton b, a, g;
    private ImageButton btnTakePhoto;
    private TextInputEditText motif, endomage, T;
    String temps;
    private int id, id_pvente = 0;
    private Double total = 0.0;
    private Switch s;
    private GoogleApiClient mGoogleApiClient;
    private int position = 0;
    private List<String> q, qv, qu, quv, idppl, mon, end, pay, idp, qvv, quvv, payy, endd, arraySpinner, vp, vpu, pu;
    private Bitmap bitmap;
    private String lat, longg, longg_pvente, lat_pvente, ss, user = "oui";
    private BDD bd;
    private FusedLocationProviderClient fusedLocationClient;
    private Button D;
    private Intent i;
    private int year, dayOfMonth, month, tab = 0;
    private String long_vendeur,lat_vendeur;

    private boolean shouldRefreshOnResume = false;
    private String nm = "";

    public void onResume() {


        super.onResume();
        //update your fragment
        if (shouldRefreshOnResume) {
            if (Main.getexit() == 1) {
                Main.exit(0);
                finish();
            }
        }


    }

    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qvv = new ArrayList<>();
        quvv = new ArrayList<>();
        payy = new ArrayList<>();
        endd = new ArrayList<>();
        vp = new ArrayList<>();
        vpu = new ArrayList<>();
        pu = new ArrayList<>();


        ss = "";
        setContentView(R.layout.liv);
        bd = new BDD(getApplicationContext());

        i = getIntent();
        s = (Switch) findViewById(R.id.switch1);
        endomage = findViewById(R.id.endomage);
        motif = findViewById(R.id.motif);
        Q = findViewById(R.id.editText1);
        QU = findViewById(R.id.unite);

        QV = findViewById(R.id.editText2);
        QVU = findViewById(R.id.qvu);

        PV = findViewById(R.id.editText5);
        ID = findViewById(R.id.editText4);
        PAY = findViewById(R.id.payement);

        M = findViewById(R.id.editText3);  //prix vendu
        V = findViewById(R.id.valeur);
        reste = findViewById(R.id.reste);

        T = findViewById(R.id.total);
        ivCameraPreview = findViewById(R.id.image);

        P = (Spinner) findViewById(R.id.Spinner);

        progress = new ProgressDialog(liv.this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        progress.show();

        Bundle extras = i.getExtras();

        if (extras.containsKey("id_l")) {
            id_l = Integer.parseInt(i.getStringExtra("id_l"));
            ID.setText(id_l + "");

            bd.open();
            Cursor c = bd.getlongglat(id_l);
            if (c.getCount() == 0) {
                getinfos(id_l);
            }
            while (c.moveToNext()) {
                longg_pvente = c.getString(c.getColumnIndex("longg"));
                lat_pvente = c.getString(c.getColumnIndex("lat"));
                nm = c.getString(c.getColumnIndex("num"));

                id_pvente = Integer.parseInt(c.getString(c.getColumnIndex("id")));

                pvente = i.getStringExtra("pvente");

                final FloatingActionButton pvv = findViewById(R.id.iteneraire);
                pvv.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uri = "http://maps.google.com/maps?daddr=" + lat_pvente + "," + longg_pvente + " (" + pvente + ")";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);


                            }
                        });

            }
            bd.close();

        }
        if (extras.containsKey("bon")) {
            int bon = Integer.parseInt(i.getStringExtra("bon"));
            ID.setText(bon + "");

        }
        if (extras.containsKey("idlivreur")) {
            id = Integer.parseInt(i.getStringExtra("idlivreur"));


        }
        if (extras.containsKey("pvente")) {
            pvente = i.getStringExtra("pvente");
            PV.setText(pvente);

        }
        if (extras.containsKey("photo")) {
            if (i.getStringExtra("photo").equals("cc")) {

                Bitmap src = loadBitmap(this, "myImage.jpg");
                ivCameraPreview.setImageBitmap(src);

            }


        }
        if (extras.containsKey("produit")) {
            arraySpinner = i.getStringArrayListExtra("produit");
            idppl = arraySpinner;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, arraySpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            P.setAdapter(adapter);

        }
        if (extras.containsKey("payement")) {
            pvente = i.getStringExtra("payement");
            PAY.setText(pvente);


        }
        if (extras.containsKey("reste")) {
            String r = i.getStringExtra("reste");
            reste.setText(r);
        }
        if (extras.containsKey("quantite")) {
            q = i.getStringArrayListExtra("quantite");
            Q.setText(q.get(0));
            QV.setText(q.get(0));
        }
        if (extras.containsKey("idp")) {
            idp = i.getStringArrayListExtra("idp");

        }
        WebView iv = findViewById(R.id.webview);
        iv.loadUrl(null);
        if (extras.containsKey("quantite_u")) {
            qu = i.getStringArrayListExtra("quantite_u");
            QU.setText(qu.get(0));
            QVU.setText(qu.get(0));
        }
        if (extras.containsKey("user")) {
            user = i.getStringExtra("user");

        }
        if (extras.containsKey("tab")) {
            tab = Integer.parseInt(i.getStringExtra("tab"));
            if (tab == 2 || tab == 3) {
                if (tab == 2) {
                    FloatingActionButton save = findViewById(R.id.enregistrer);
                    save.setVisibility(View.INVISIBLE);
                    FloatingActionButton aa = findViewById(R.id.annuler);
                    aa.setVisibility(View.INVISIBLE);
                }

                String montant = i.getStringExtra("motif");
                if (!montant.equals("null") && !montant.equals("")) {

                    iv.loadUrl("http://www.casbahdz.com/casbahtemplate/libs/image/image.php?id=" + id_l + "");
                    ivCameraPreview.setImageBitmap(null);

                }
            }

        }

        if (extras.containsKey("montant")) {
            mon = i.getStringArrayListExtra("montant");
            M.setText(mon.get(0));
            V.setText(mon.get(0));

        }

        if (extras.containsKey("endomage")) {
            end = i.getStringArrayListExtra("endomage");
            if (!end.get(0).equals("null"))
                endomage.setText(end.get(0));

        }
        bd.open();
        String faite="non";
        if(bd.getfaite(String.valueOf(id_l)))
            faite="oui";


        bd.close();

        if (extras.containsKey("quantite_v")) {
            qv = i.getStringArrayListExtra("quantite_v");

            if (!qv.get(0).equals("null"))
                QV.setText(qv.get(0));

            if(faite.equals("non"))
                QV.setText(q.get(0));


        }
        if (extras.containsKey("quantite_u_v")) {
            quv = i.getStringArrayListExtra("quantite_u_v");
            if (!quv.get(0).equals("null"))
                QVU.setText(quv.get(0));

            if(faite.equals("non"))
                QVU.setText(qu.get(0));

        }


        D = findViewById(R.id.datex);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(liv.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int day) {
                                m++;
                                String d = y + "-" + m + "-" + day;
                                String dd = convertDate(day) + "-" + convertDate(m) + "-" + y;


                                year = y;
                                month = m - 1;
                                dayOfMonth = day;
//progress.show();
                                change_date(d);
                                if(tab==4){
                                    bd.open();
                                    bd.supprimer(id_l);
                                    bd.close();
                                }

                            }
                        }, year, month, dayOfMonth);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 0);
//Set min time to now
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        btnTakePhoto = findViewById(R.id.photo);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if app has permission to access the camera.
                if (EasyPermissions.hasPermissions(liv.this, Manifest.permission.CAMERA)) {
                    launchCamera();

                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(liv.this, getString(R.string.permission_text), CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
                }
            }
        });


        Button btn = findViewById(R.id.accepter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if app has permission to access the camera.
                new AlertDialog.Builder(liv.this)
                        .setMessage("etes vous sur d'accepter cette commande ?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int idd) {
                                accepter();

                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();

            }
        });

        if (tab == 2 || tab == 3 || tab == 4) {

        } else {
            btn.setVisibility(View.INVISIBLE);
            Button btnn = findViewById(R.id.datex);
            btnn.setVisibility(View.INVISIBLE);

            FloatingActionButton location = findViewById(R.id.location);
            location.setVisibility(View.INVISIBLE);

        }

        b = findViewById(R.id.enregistrer);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
                b.setClickable(false);
                add();

            }
        });

        a = findViewById(R.id.annuler);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                a.setClickable(false);
                annuler();

            }
        });
        g = findViewById(R.id.location);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                g.setClickable(false);
                GPS();

            }
        });

        motif.setEnabled(false);
        btnTakePhoto.setEnabled(false);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    motif.setEnabled(true);
                    btnTakePhoto.setEnabled(true);
                    QV.setEnabled(false);
                    QVU.setEnabled(false);
                    M.setEnabled(false);
                    reste.setEnabled(false);
                    endomage.setEnabled(false);
                    PAY.setEnabled(false);
                    hide();


                } else {

                    motif.setEnabled(false);
                    btnTakePhoto.setEnabled(false);
                    QV.setEnabled(true);
                    QVU.setEnabled(true);
                    reste.setEnabled(true);
                    PAY.setEnabled(true);

                    endomage.setEnabled(true);
                    M.setEnabled(true);
                    show();

                }
            }
        });
        if (extras.containsKey("motif")) {
            montant = i.getStringExtra("motif");
            if (!montant.equals("null") && !montant.equals("")) {
                motif.setText(montant);
                s.setChecked(true);
                hide();
            } else {
                show();
            }

        }

        sp();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        for (int k = 0; k < mon.size(); k++) {
            total = total + Double.parseDouble(mon.get(k));
        }
        T.setText(total + "");
        PAY.setText(total + "");


        for (int j = 0; j < mon.size(); j++) {

if(faite.equals("oui")){
            qvv.add(j, String.valueOf(qv.get(j)));
            quvv.add(j, String.valueOf(quv.get(j)));}
else{
    qvv.add(j, String.valueOf(q.get(j)));
    quvv.add(j, String.valueOf(qu.get(j)));

}

            payy.add(j, String.valueOf(mon.get(j)));
            endd.add(j, String.valueOf(end.get(j)));

        }

        change();
        get_magg();
        get_uu();

        Button add = findViewById(R.id.ajout);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tab != 2 && tab != 3 && tab != 4) {
                    Intent myIntent = new Intent(liv.this, editproduits.class);
                    myIntent.putExtra("id_l", id_l + "");
                    myIntent.putExtra("id", id + "");

                    myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idppl);


                    startActivity(myIntent);
                } else if (tab == 4) {
                    Intent myIntent = new Intent(liv.this, editproduitsss.class);
                    myIntent.putExtra("id_l", id_l + "");
                    myIntent.putExtra("id", id + "");

                    myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idppl);


                    startActivity(myIntent);

                }
               /* else{
                    Intent myIntent = new Intent(liv.this, editproduitss.class);
                    myIntent.putExtra("id_l", id_l + "");
                    myIntent.putExtra("id", id + "");

                    myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idppl);


                    startActivity(myIntent);

                }*/

            }
        });

    }

    private void onCallBtnClick() {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        } else {

            if (ActivityCompat.checkSelfPermission(liv.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(liv.this, PERMISSIONS_STORAGE, 9);
            }
        }
    }


    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(liv.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + nm));
            startActivity(callIntent);
        } else {
            Toast.makeText(liv.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }


    public void getinfos(final int id) {
        progress.show();
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action", "get longglat")
                .addBodyParameter("id", id + "")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null)
                            try {


                                id_pvente = response.getJSONObject(0).getInt("id");


                                longg_pvente = response.getJSONObject(0).getString("longg");
                                lat_pvente = response.getJSONObject(0).getString("lat");
                                nm = response.getJSONObject(0).getString("num");


                                final FloatingActionButton pvv = findViewById(R.id.iteneraire);
                                pvv.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String uri = "http://maps.google.com/maps?daddr=" + lat_pvente + "," + longg_pvente + " (" + pvente + ")";
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                                intent.setPackage("com.google.android.apps.maps");
                                                startActivity(intent);


                                            }
                                        });


                                final FloatingActionButton pvvv = findViewById(R.id.tlf);
                                pvvv.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                onCallBtnClick();

                                            }
                                        });


                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(liv.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(liv.this, "Client n'existe pas", Toast.LENGTH_SHORT).show();

                    }


                });


    }


    public void change_date(String d) {
        progress.show();
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action", "changer date")
                .addBodyParameter("id", id_l + "")
                .addBodyParameter("date", d + "")
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null)
                            try {


                                bd.open();
                                String responseString = response.get(0).toString();
                                if (responseString.equals("Success")) {
                                    Toast.makeText(liv.this, "Changée avec success", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(liv.this, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show();

                                }

                                progress.dismiss();
                                bd.close();

                                //bd();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(liv.this, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(liv.this, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show();

                    }


                });


    }


    public void accepter() {
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action", "accepter")
                .addBodyParameter("id", id_l + "")

                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null)
                            try {


                                bd.open();
                                String responseString = response.get(0).toString();
                                if (responseString.equals("Success")) {
                                    Toast.makeText(liv.this, "Acceptée avec success", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(liv.this, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show();

                                }

                                progress.dismiss();
                                bd.close();

                                //bd();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(liv.this, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(liv.this, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show();

                    }


                });


    }

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    public static Bitmap loadBitmap(Context context, String picName) {
        Bitmap b = null;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "io exception");
            e.printStackTrace();
        }


        return b;
    }

    public void hide() {
        Spinner n = findViewById(R.id.Spinner);
        n.setVisibility(View.INVISIBLE);

        TextInputLayout nmm = findViewById(R.id.qavf);
        nmm.setVisibility(View.INVISIBLE);


        TextInputLayout w = findViewById(R.id.qavu);
        w.setVisibility(View.INVISIBLE);


        TextInputLayout cc = findViewById(R.id.valeurr);
        cc.setVisibility(View.INVISIBLE);


        TextInputLayout a = findViewById(R.id.qvf);
        a.setVisibility(View.INVISIBLE);

        TextInputLayout bb = findViewById(R.id.qvuu);
        bb.setVisibility(View.INVISIBLE);


        TextInputLayout x = findViewById(R.id.montant);
        x.setVisibility(View.INVISIBLE);

        TextInputLayout xx = findViewById(R.id.tt);
        xx.setVisibility(View.INVISIBLE);
        TextInputLayout xxx = findViewById(R.id.pp);
        xxx.setVisibility(View.INVISIBLE);
        TextInputLayout vxx = findViewById(R.id.rr);
        vxx.setVisibility(View.INVISIBLE);
        RelativeLayout uxx = findViewById(R.id.div);
        uxx.setVisibility(View.VISIBLE);


        TextInputLayout dd = findViewById(R.id.endo);
        dd.setVisibility(View.INVISIBLE);

        TextInputLayout bon = findViewById(R.id.bon);

        final RelativeLayout.LayoutParams lppp = (RelativeLayout.LayoutParams) uxx.getLayoutParams();
        lppp.addRule(RelativeLayout.BELOW, R.id.bon);
        uxx.setLayoutParams(lppp);
        Button ajout = findViewById(R.id.ajout);
        ajout.setVisibility(View.INVISIBLE);

    }

    public void show() {
        Button ajout = findViewById(R.id.ajout);
        ajout.setVisibility(View.VISIBLE);
        Spinner n = findViewById(R.id.Spinner);
        n.setVisibility(View.VISIBLE);

        TextInputLayout nmm = findViewById(R.id.qavf);
        nmm.setVisibility(View.VISIBLE);


        TextInputLayout w = findViewById(R.id.qavu);
        w.setVisibility(View.VISIBLE);


        TextInputLayout cc = findViewById(R.id.valeurr);
        cc.setVisibility(View.VISIBLE);


        TextInputLayout a = findViewById(R.id.qvf);
        a.setVisibility(View.VISIBLE);

        TextInputLayout bb = findViewById(R.id.qvuu);
        bb.setVisibility(View.VISIBLE);


        TextInputLayout x = findViewById(R.id.montant);
        x.setVisibility(View.VISIBLE);

        TextInputLayout xx = findViewById(R.id.tt);
        xx.setVisibility(View.VISIBLE);
        TextInputLayout xxx = findViewById(R.id.pp);
        xxx.setVisibility(View.VISIBLE);
        TextInputLayout vxx = findViewById(R.id.rr);
        vxx.setVisibility(View.VISIBLE);
        RelativeLayout uxx = findViewById(R.id.div);
        uxx.setVisibility(View.INVISIBLE);


        TextInputLayout dd = findViewById(R.id.endo);
        dd.setVisibility(View.VISIBLE);




/*
     final RelativeLayout.LayoutParams lppp = (RelativeLayout.LayoutParams) dd.getLayoutParams();
        lppp.addRule(RelativeLayout.BELOW, R.id.endo);

        uxx.setLayoutParams(lppp);*/


        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) dd.getLayoutParams();
        params.bottomMargin = 200;

    }

    public void change() {
        M.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                total = 0.0;
                String c = M.getText().toString();
                if (M.getText().toString().equals(""))
                    c = "0";

                for (int k = 0; k < payy.size(); k++) {
                    if (k == position) {
                        total = total + Double.parseDouble(String.valueOf(c));

                    } else {
                        total = total + Double.parseDouble(payy.get(k));
                    }
                }
                T.setText(total + "");
                PAY.setText(total + "");


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });
    }


    public void sp() {


        P.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {


                qvv.set(position, String.valueOf(QV.getText()));
                quvv.set(position, String.valueOf(QVU.getText()));
                payy.set(position, String.valueOf(M.getText()));
                endd.set(position, String.valueOf(endomage.getText()));
                position = pos;

                Q.setText(q.get(pos));


                QU.setText(qu.get(pos));


                V.setText(mon.get(pos));


                endomage.setText(endd.get(position));


                QV.setText(qvv.get(position));


                QVU.setText(quvv.get(position));


                M.setText(payy.get(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, liv.this);
//If the permission has been granted...//
        if (EasyPermissions.hasPermissions(liv.this, Manifest.permission.CAMERA)) {
            launchCamera();

        } else {
            boolean permissionGranted = false;
            switch (requestCode) {
                case 9:
                    permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    break;
            }
            if (permissionGranted) {
                phoneCall();
            } else {
                Toast.makeText(liv.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void launchCamera() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.casbaherpapp.myapplication",
                    photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            //Start the camera application
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    /**
     * Previews the captured picture on the app
     * Called when the picture is taken
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Preview the image captured by the camera
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            if (bitmap != null) {
                float degrees = 90; //rotation degree
                Matrix matrix = new Matrix();
                matrix.setRotate(degrees);
                Bitmap bOutput = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                ivCameraPreview.setImageBitmap(bOutput);
                bitmap = bOutput;

            }
        }
    }


    public void bdannuler() {
        for (int i = 0; i < idp.size(); i++) {
            bd.open();
            bd.annuler(id_l, idp.get(i), id, arraySpinner.get(i));
            bd.close();


        }


    }

    public void annuler() {


        new AlertDialog.Builder(this)
                .setMessage("etes vous sur d'annuler la livraison ?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int idd) {
                        for (int i = 0; i < idp.size(); i++) {
                            progress.show();
                            final int l = i;
                            bdannuler();
                            AndroidNetworking.post(DATA_INSERT_URL)
                                    .addBodyParameter("action", "annuler livraison")
                                    .addBodyParameter("id", id_l + "")
                                    .addBodyParameter("idl", id + "")
                                    .addBodyParameter("idp", idp.get(i) + "")
                                    .addBodyParameter("produit", arraySpinner.get(i) + "")
                                    .setTag("test")
                                    .setPriority(Priority.HIGH)
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                        public void onResponse(JSONArray response) {

                                            if (response != null)
                                                try {

                                                    progress.dismiss();

                                                    String responseString = response.get(0).toString();

                                                    a.setClickable(true);
                                                    finish();

                                         /*   motif.setText("");
                                            QV.setText(Q.getText());
                                            M.setText(mon.get(i));
                                                   */

                                                } catch (JSONException e) {
                                                    a.setClickable(true);
                                                    progress.dismiss();
                                                    e.printStackTrace();
                                                    finish();
                                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();

                                                }

                                        }

                                        //ERROR
                                        @Override
                                        public void onError(ANError anError) {
                                            bdannuler();
                                           /* motif.setText("");
                                            QV.setText(Q.getText());
                                            QVU.setText(QU.getText());

                                            M.setText("0");*/
                                            a.setClickable(true);

                                            progress.dismiss();
                                            Toast.makeText(getApplicationContext(), "Enregistrement dans la base de données locale", Toast.LENGTH_SHORT).show();
finish();

                                        }


                                    });

                        }
                    }
                })
                .setNegativeButton("Non", null)
                .show();
        a.setClickable(true);

    }

    public void GPS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {


                            new AlertDialog.Builder(liv.this)
                                    .setMessage("etes vous sur que vous etes arrivez au client ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            bd.open();
                                            bd.updatelonglat(id_pvente,location.getLongitude()+"",location.getLatitude()+"");
                                            bd.close();
                                            longg_pvente=location.getLongitude()+"";
                                            lat_pvente=location.getLatitude()+"";

                                            progress.show();
                                            AndroidNetworking.post(DATA_INSERT_URL)
                                                    .addBodyParameter("action", "localisation pvente")
                                                    .addBodyParameter("pvente", id_pvente + "")
                                                    .addBodyParameter("longg", location.getLongitude() + "")
                                                    .addBodyParameter("lat", location.getLatitude() + "")


                                                    .setTag("test")
                                                    .setPriority(Priority.HIGH)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {

                                                            if (response != null)
                                                                try {


                                                                    String responseString = response.get(0).toString();



                                                                } catch (JSONException e) {

                                                                    e.printStackTrace();
                                                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                                }

                                                        }

                                                        //ERROR
                                                        @Override
                                                        public void onError(ANError anError) {


                                                            Toast.makeText(getApplicationContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                                                        }


                                                    });


                                            Calendar rightNow = Calendar.getInstance();

                                            AndroidNetworking.post(DATA_INSERT_URL)
                                                    .addBodyParameter("action", "arrivage pvente")
                                                    .addBodyParameter("id", id_l + "")

                                                    .addBodyParameter("heure", rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE))


                                                    .setTag("test")
                                                    .setPriority(Priority.HIGH)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {

                                                            if (response != null)
                                                                try {

                                                                    progress.dismiss();

                                                                    String responseString = response.get(0).toString();

                                                                    g.setClickable(true);

                                                                } catch (JSONException e) {
                                                                    g.setClickable(true);

                                                                    progress.dismiss();
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                                }

                                                        }

                                                        //ERROR
                                                        @Override
                                                        public void onError(ANError anError) {
                                                            g.setClickable(true);

                                                            progress.dismiss();
                                                            Toast.makeText(getApplicationContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                                                        }


                                                    });

                                        }
                                    })
                                    .setNegativeButton("Non", null)
                                    .show();
                        }
                        else{
                            g.setClickable(true);
                        }
                    }
                });

    }

    public void onPermissionsGranted(int requestCode, List<String> perms) {
        launchCamera();
    }


    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }


    /**
     * Creates the image file in the external directory
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        temps = new SimpleDateFormat("yyyy-MM-dd_HH:mm").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void bdupdateA() {

        bd.open();
        for (int i = 0; i < idp.size(); i++) {
            int u = (Integer.parseInt(qvv.get(i)) * Integer.parseInt(pu.get(i))) + Integer.parseInt(quvv.get(i));


            bd.updateLivA(user, ID.getText().toString(), idp.get(i), id_l, qvv.get(i), quvv.get(i), PAY.getText().toString(), endd.get(i), reste.getText().toString(), arraySpinner.get(i), id, u, payy.get(i));


        }


        bd.close();

    }

    public void bdupdateB() {
        TextInputEditText t = findViewById(R.id.motif);
        final String motif = String.valueOf(t.getText());
        bd.open();
        for (int i = 0; i < idp.size(); i++) {
            try {

                bd.updateLivB(user, idp.get(i), id_l, "0", "0", "0", "0", "0", arraySpinner.get(i), id, 0, motif);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Quelque produit n'est pas en stock", Toast.LENGTH_SHORT).show();

            }


        }


        bd.close();

    }

    public float getDistance() {
        if(lat_pvente!=null){
if(!lat_pvente.equals("NULL") && !longg_pvente.equals("NULL") && !lat_pvente.equals("") && !longg_pvente.equals("")  ){
        Location loc1 = new Location("");

        loc1.setLatitude(Double.parseDouble(lat_pvente));
        loc1.setLongitude(Double.parseDouble(longg_pvente));

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(lat_vendeur));
        loc2.setLongitude(Double.parseDouble(long_vendeur));

        float distanceInMeters = loc1.distanceTo(loc2);

        return distanceInMeters;} return 0;}
else{
    bd.open();
    float x = Float.parseFloat(bd.getdistance()+1);
    return x;
}

    }

    public void bdupdateC(String photo, String temp) {
        TextInputEditText t = findViewById(R.id.motif);
        final String motif = String.valueOf(t.getText());
        bd.open();
        for (int i = 0; i < idp.size(); i++) {
            try {
                bd.updateLivC(user, idp.get(i), id_l, "0", "0", "0", "0", "0", arraySpinner.get(i), id, 0, motif, photo, temp);

            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Quelque produit n'est pas en stock", Toast.LENGTH_SHORT).show();

            }

        }


        bd.close();

    }


    public void add() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                .addOnSuccessListener(liv.this, new OnSuccessListener<Location>() {

                    public void onSuccess(final Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            long_vendeur = location.getLongitude() + "";
                            lat_vendeur = location.getLatitude() + "";
                            bd.open();

if(getDistance()<=Integer.parseInt(bd.getdistance()))  {
                   bd.close();

                            qvv.set(position, String.valueOf(QV.getText())); //inserer quantity vendu fadeaux
                            quvv.set(position, String.valueOf(QVU.getText())); //inserer quantity vendu unites
                            payy.set(position, String.valueOf(M.getText())); //inserer prix vendu dans array
                            endd.set(position, String.valueOf(endomage.getText())); //inserer produit endomage
                            if (!s.isChecked()) {


                                if (endomage.getText().equals("")) {
                                    ss = "0";
                                } else {
                                    ss = String.valueOf(endomage.getText());
                                }
                               //M = prix vendu de produit
                                if (String.valueOf(M.getText()) == null || String.valueOf(QV.getText()) == null) {
                                    Toast.makeText(getApplicationContext(), "Quantité à vendre où payement non valide", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                    b.setClickable(true);

                                } else if (verif()) {
                                    if (tab != 2 && tab != 3)
                                        bdupdateA();

                                    if (user.equals("non")) {
                                        for (int i = 0; i < idp.size(); i++) {
                                            int u = (Integer.parseInt(qvv.get(i)) * Integer.parseInt(pu.get(i))) + Integer.parseInt(quvv.get(i));


                                            final String finalSs = ss;
                                            final int l = i;
                                            AndroidNetworking.post(DATA_INSERT_URL)
                                                    .addBodyParameter("action", "update livraison")
                                                    .addBodyParameter("id", idp.get(i) + "")
                                                    .addBodyParameter("idl", id_l + "")
                                                    .addBodyParameter("quantite_v", qvv.get(i) + "")
                                                    .addBodyParameter("quantite_u_v", quvv.get(i) + "")
                                                    .addBodyParameter("payement", PAY.getText().toString() + "")
                                                    .addBodyParameter("endomage", endd.get(i))
                                                    .addBodyParameter("montant", payy.get(i))
                                                    .addBodyParameter("reste", reste.getText().toString())
                                                    .addBodyParameter("produit", arraySpinner.get(i) + "")
                                                    .addBodyParameter("idlivreur", id + "")
                                                    .addBodyParameter("quantite", u + "")
                                                    .addBodyParameter("bon", ID.getText().toString() + "")

                                                    .setTag("test")
                                                    .setPriority(Priority.HIGH)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {

                                                            if (response != null)
                                                                try {
                                                                    progress.dismiss();

                                                                    String responseString = response.get(0).toString();

                                                                    b.setClickable(true);
                                        /*if(l==idp.size()-1){
                                            get_mag();

                                        }*/

                                                                } catch (JSONException e) {
                                                                    b.setClickable(true);

                                                                    progress.dismiss();
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                                }

                                                        }

                                                        //ERROR
                                                        @Override
                                                        public void onError(ANError anError) {
                               /* bd.open();
                                bd.updateLivA(id_l,quantite_v,quantite_v_u,payement, ss);
                                bd.close();
                                b.setClickable(true);*/
                                                            b.setClickable(true);
                                                            progress.dismiss();
                                                            Toast.makeText(getApplicationContext(), "Enregistrement dans la base de données locale", Toast.LENGTH_SHORT).show();


                                                        }


                                                    });


                                        }
                                    } else {
                                        progress.dismiss();
                                        b.setClickable(true);
                                    }
                                }
                            } else {
                                TextInputEditText t = findViewById(R.id.motif);
                                final String motif = String.valueOf(t.getText());
                                final double payement = Double.parseDouble(String.valueOf(M.getText()));

                                if (String.valueOf(t.getText()) == null || String.valueOf(t.getText()).equals("")) {
                                    Toast.makeText(getApplicationContext(), "Remplissez le motif", Toast.LENGTH_SHORT).show();
                                    b.setClickable(true);
                                    progress.dismiss();


                                } else if (bitmap == null) {
                                    if (tab != 2 && tab != 3) {
                                        bdupdateB();

                                    }

                                    if (user.equals("non")) {
                                        for (int i = 0; i < idp.size(); i++) {
                                            AndroidNetworking.post(DATA_INSERT_URL)
                                                    .addBodyParameter("action", "update livraisonA")
                                                    .addBodyParameter("id", id_l + "")
                                                    .addBodyParameter("motif", motif + "")
                                                    .addBodyParameter("idl", id + "")
                                                    .addBodyParameter("idp", idp.get(i) + "")
                                                    .addBodyParameter("produit", arraySpinner.get(i) + "")
                                                    .setTag("test")
                                                    .setPriority(Priority.HIGH)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {

                                                            if (response != null)
                                                                try {


                                                                    progress.dismiss();

                                                                    String responseString = response.get(0).toString();

                                                                    b.setClickable(true);

                                                                } catch (JSONException e) {
                                                                    b.setClickable(true);

                                                                    progress.dismiss();
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                                }

                                                        }

                                                        //ERROR
                                                        @Override
                                                        public void onError(ANError anError) {

                                                            b.setClickable(true);

                                                            progress.dismiss();
                                                            Toast.makeText(getApplicationContext(), "Enregistrement dans la base de données locale", Toast.LENGTH_SHORT).show();


                                                        }


                                                    });


                                        }
                                    } else {
                                        progress.dismiss();
                                        b.setClickable(true);
                                    }
                                } else {

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                                    byte[] byte_arr = stream.toByteArray();
                                    String tt = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                                    final String temp = resizeBase64Image(tt);
                                    if (tab != 2 && tab != 3)
                                        bdupdateC(temp, temps);

                                    if (user.equals("non")) {

                                        for (int i = 0; i < idp.size(); i++) {
                                            AndroidNetworking.post(DATA_INSERT_URL)
                                                    .addBodyParameter("action", "update livraisonB")
                                                    .addBodyParameter("id", id_l + "")
                                                    .addBodyParameter("motif", motif + "")
                                                    .addBodyParameter("photo", temp)
                                                    .addBodyParameter("temps", temps)
                                                    .addBodyParameter("idl", id + "")
                                                    .addBodyParameter("idp", idp.get(i) + "")
                                                    .addBodyParameter("produit", arraySpinner.get(i) + "")

                                                    .setTag("test")
                                                    .setPriority(Priority.HIGH)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {

                                                            if (response != null)
                                                                try {

                                                                    progress.dismiss();

                                                                    String responseString = response.get(0).toString();

                                                                    b.setClickable(true);

                                                                } catch (JSONException e) {
                                                                    b.setClickable(true);

                                                                    progress.dismiss();
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                                                }

                                                        }

                                                        //ERROR
                                                        @Override
                                                        public void onError(ANError anError) {

                                                            b.setClickable(true);

                                                            progress.dismiss();
                                                            Toast.makeText(getApplicationContext(), "Enregistrement dans la base de données locale", Toast.LENGTH_SHORT).show();


                                                        }


                                                    });

                                        }
                                    } else {
                                        progress.dismiss();
                                        b.setClickable(true);
                                    }
                                }


                            }
                        }

else{
    progress.dismiss();
    b.setClickable(true);

    bd.close();
    Toast.makeText(getApplicationContext(), "Vous etes loin du client  انت بعيد عن الزبون", Toast.LENGTH_SHORT).show();

}
                        }

                        else{
                            b.setClickable(true);
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Activez la localisation ", Toast.LENGTH_SHORT).show();


                        }
                    }
                });}


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }




    public void get_magg() {
        bd.open();
        for (int i = 0; i < arraySpinner.size(); i++) {


            Cursor c = bd.getmagg(id);

            while (c.moveToNext()) {
                if (arraySpinner.get(i).equals(c.getString(c.getColumnIndex("produit")))) {

                    vpu.add(c.getString(c.getColumnIndex("quantite")));


                }


            }
        }
        bd.close();
    }
    public String resizeBase64Image(String base64image){
        byte [] encodeByte=Base64.decode(base64image.getBytes(),Base64.DEFAULT);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length,options);


        if(image.getHeight() <= 400 && image.getWidth() <= 400){
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 400, 400, false);

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }

    public boolean verif() {

        int quantite_v = Integer.parseInt(String.valueOf(QV.getText()));
        int quantite_v_u = Integer.parseInt(String.valueOf(QVU.getText()));
        quvv.set(position, quantite_v_u + "");
        qvv.set(position, quantite_v + "");
try{
        for (int i = 0; i < qvv.size(); i++) {
            int u = (Integer.parseInt(qvv.get(i)) * Integer.parseInt(pu.get(i))) + Integer.parseInt(quvv.get(i));
            if (u > Integer.parseInt(vpu.get(i))) {
                progress.dismiss();
                b.setClickable(true);
                Toast.makeText(getApplicationContext(), "Stock insuffisant pour le produit :" + arraySpinner.get(i), Toast.LENGTH_SHORT).show();

                return false;
            }


        }}
catch (Exception e){
    progress.dismiss();
    Toast.makeText(getApplicationContext(), "Il ya de produits qui n'existent pas dans votre stock" , Toast.LENGTH_SHORT).show();

    return false;
}


        return true;

    }

    public void get_u() {
        for (int i = 0; i < arraySpinner.size(); i++) {
            final int x = i;
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action", "get unite")
                    .addBodyParameter("nom", arraySpinner.get(i) + "")

                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response != null)
                                try {


                                    pu.add(response.getJSONObject(0).getString("fardeau"));


                                    // String responseString= response.get(0).toString();

                                    progress.dismiss();
                                } catch (JSONException e) {
                                    progress.dismiss();

                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {


                            progress.dismiss();


                        }


                    });
        }

    }

    public void get_uu() {
        progress.dismiss();
        bd.open();
        for (int i = 0; i < arraySpinner.size(); i++) {


            Cursor c = bd.getmag(id);

            while (c.moveToNext()) {
                if (arraySpinner.get(i).equals(c.getString(c.getColumnIndex("produit")))) {
                    pu.add(c.getString(c.getColumnIndex("fardeau")));

                }


            }
        }
        bd.close();
    }
}