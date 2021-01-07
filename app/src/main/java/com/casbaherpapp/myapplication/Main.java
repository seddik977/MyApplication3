package com.casbaherpapp.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.joanzapata.iconify.widget.IconButton;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.support.toast.BadTokenListener;
import me.drakeet.support.toast.ToastCompat;

import static java.lang.Float.intBitsToFloat;
import static java.lang.Float.parseFloat;

public class Main extends AppCompatActivity {
    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    private AppBarConfiguration mAppBarConfiguration;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    private static final String DATA_URL = "http://www.casbahdz.com/adm/CommandeLivreur/commande_livreur_crud.php";//developed by IMAD

    public static int id=-1,idp=-1,x=0,creditt=0,idcreditt=0,exit=0;
    private static BDD bd;
    public static String b,date="";
    private static boolean hi=false;
    public static ProgressDialog progress ;
    private static String[] quantite,QU,quantite_v,QVU,produit,montant,payement,endomage,payementx,restex;
    private static String[] nom,num,longg,lat,adresse,zone,ID,credit,wilaya,type,id_livreur;
    private static int[] id_l,id_lx,idf;
    private static String[][] produitx,quantitex,quantite_ux,montant_bonx,endomagex,QVx,QVUx,IDPx;
    private Intent intent;
    private static int l;
    private MenuItem itemMessages;
    private static TextView itemMessagesBadgeTextView;
    private IconButton iconButtonMessages;
    private  Handler handler;
    private Intent intentgps;
    private Runnable runnable;
    TextView lbl;
    public static final int requestcode = 1;



    private static Context context;
    public int getData() {
        return id;
    }
    public int getidp() {
        return idp;
    }
    public static void get_mag(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get magasin")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                produit= new String[response.length()];
                                quantite= new String[response.length()];
                                montant= new String[response.length()];
                                payement= new String[response.length()];
                                quantite_v= new String[response.length()];
                                endomage= new String[response.length()];
                                QU= new String[response.length()];
                                QVU= new String[response.length()];


                                progress.dismiss();
                                bd.open();


                                //SHOW RESPONSE FROM SERVER
                                for(int i=0;i<response.length();i++) {
                                    int f=Integer.parseInt(response.getJSONObject(i).getString("fardeau"));
                                    produit[i] = response.getJSONObject(i).getString("produit");
                                    quantite[i] = ((int)Float.parseFloat(response.getJSONObject(i).getString("quantite_f")))+"";
                                    int qu=Integer.parseInt(response.getJSONObject(i).getString("quantite_u"));
                                    QU[i] = String.valueOf(qu-(f*Integer.parseInt(quantite[i])));

                                    montant[i] = response.getJSONObject(i).getString("valeur");

                                    bd.InsertStock(id,produit[i],quantite[i],QU[i],montant[i],f);



                                }
                                bd.close();

                                AndroidNetworking.post(DATA_INSERT_URL)
                                        .addBodyParameter("action","set veri")
                                        .addBodyParameter("id",id+"")
                                        .setTag("test")
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                                if(response != null)
                                                    try {
                                                        //bd();
                                                        String responseString= response.get(0).toString();
                                                    } catch (JSONException e) {
                                                        progress.dismiss();
                                                        e.printStackTrace();
                                                    }

                                            }

                                            //ERROR
                                            @Override
                                            public void onError(ANError anError) {


                                                progress.dismiss();
                                                anError.printStackTrace();

                                            }


                                        });

                                // String responseString= response.get(0).toString();


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

    public static int getCreditt(){
        return creditt;
    }
    public static int getidCreditt(){
        return idcreditt;
    }

    public static int getexit(){
        return exit;
    }

    public static void setCreditt(int i){
        creditt=i;
    }
    public static void exit(int i){
        exit=i;
    }
    public static void setidCreditt(int i){
        idcreditt=i;
    }
    public String getB() {
        return b;
    }

    public void setData(int data) {
        this.id = data;
    }
    public static void setidp(int data) {
        idp = data;
    }

    public void setB(String data) {
        this.b = data;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bd= new BDD(this);
        progress = new ProgressDialog(Main.this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(getIntent().getParcelableExtra(Intent.EXTRA_INTENT)!=null)
            intentgps= getIntent().getParcelableExtra(Intent.EXTRA_INTENT);

        if(extras.containsKey("id")) {
            id = Integer.parseInt(i.getStringExtra("id"));
            setData(id);

        }
        else{
            bd.open();
            id=bd.getID();
            b=bd.getb();
            bd.close();
        }
        if(extras.containsKey("branche")) {
            b = i.getStringExtra("branche");
            setB(b);

        }

        if(id==-1){
            bd.open();
            id=bd.getID();
            b=bd.getb();
            bd.close();

        }

        main();


        verif();
        //setInfos();
        if(extras.containsKey("hi")) {
            hi=true;
        }

        if(extras.containsKey("holla")) {
            date=i.getStringExtra("holla");
          /*  NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().performIdentifierAction(R.id.nav_home,1);*/
            NavigationView navigationVieww = findViewById(R.id.nav_view);
            navigationVieww.getMenu().performIdentifierAction(R.id.nav_gallery,1);
            intentgps = new Intent(this, GPS.class);
            intentgps.putExtra("id", id+"");


            startService(intentgps);

        }
        NavigationView navigationVieww = findViewById(R.id.nav_view);

        context=Main.this;



    }

    public void setnotif(){
        bd.open();
        int count =bd.getnoti();
        bd.close();
        if(count>99)
            itemMessagesBadgeTextView.setText("+"+99 );
        else if(count >0)
            itemMessagesBadgeTextView.setText(""+count );
        else
            itemMessagesBadgeTextView.setText("");

        itemMessagesBadgeTextView.setVisibility(View.VISIBLE);
        iconButtonMessages.setTextColor(getResources().getColor(R.color.white));

    }
    public void setdate(){
        date="";
    }
    public String getdate(){
        return date;
    }
    public static void get_pvente(){
        progress.show();
        bd.open();
        int id=bd.getID();
        bd.close();
        bd.open();
        int cpt=bd.getpvente1();
        bd.close();


        if(cpt==0) {
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action", "get pventes")
                    .addBodyParameter("branche", id + "")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response != null)
                                try {

                                    id_l = new int[response.length()];

                                    nom = new String[response.length()];
                                    zone = new String[response.length()];
                                    num = new String[response.length()];
                                    longg = new String[response.length()];
                                    lat = new String[response.length()];
                                    adresse = new String[response.length()];
                                    credit = new String[response.length()];
                                    wilaya = new String[response.length()];

                                    type = new String[response.length()];
                                    id_livreur = new String[response.length()];


                                    bd.open();


                                    for (int i = 0; i < response.length(); i++) {
                                        id_l[i] = response.getJSONObject(i).getInt("id");
                                        nom[i] = response.getJSONObject(i).getString("nom");
                                        num[i] = response.getJSONObject(i).getString("num");
                                        adresse[i] = response.getJSONObject(i).getString("lieu");
                                        wilaya[i] = response.getJSONObject(i).getString("wilaya");
                                        type[i] = response.getJSONObject(i).getString("type");
                                        id_livreur[i] = response.getJSONObject(i).getString("id_livreur");
                                        longg[i] = response.getJSONObject(i).getString("longg");
                                        lat[i] = response.getJSONObject(i).getString("lat");

                                        zone[i] = response.getJSONObject(i).getString("zone");
                                        if (!response.getJSONObject(i).getString("reste").equals("null"))
                                            credit[i] = response.getJSONObject(i).getString("reste");
                                        else
                                            credit[i] = 0 + "";

                                        bd.insertpventelong(b, id_l[i], nom[i], num[i], adresse[i], wilaya[i], type[i], zone[i], credit[i], id_livreur[i], longg[i], lat[i]);

                                    }
                                    progress.dismiss();


                                    //SHOW RESPONSE FROM SERVER

                                    bd.close();

                                    //bd();
                                    // String responseString= response.get(0).toString();


                                } catch (JSONException e) {

                                    progress.dismiss();
                                    e.printStackTrace();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            progress.dismiss();

                            anError.printStackTrace();

                        }


                    });
        }
        else{
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action", "get pventes1")
                    .addBodyParameter("branche", id + "")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response != null)
                                try {

                                    id_l = new int[response.length()];
                                    nom = new String[response.length()];
                                    zone = new String[response.length()];
                                    num = new String[response.length()];
                                    longg = new String[response.length()];
                                    lat = new String[response.length()];
                                    adresse = new String[response.length()];
                                    credit = new String[response.length()];
                                    wilaya = new String[response.length()];

                                    type = new String[response.length()];
                                    id_livreur = new String[response.length()];


                                    bd.open();


                                    for (int i = 0; i < response.length(); i++) {
                                        id_l[i] = response.getJSONObject(i).getInt("id");
                                        nom[i] = response.getJSONObject(i).getString("nom");
                                        num[i] = response.getJSONObject(i).getString("num");
                                        adresse[i] = response.getJSONObject(i).getString("lieu");
                                        wilaya[i] = response.getJSONObject(i).getString("wilaya");
                                        type[i] = response.getJSONObject(i).getString("type");
                                        id_livreur[i] = response.getJSONObject(i).getString("id_livreur");
                                        longg[i] = response.getJSONObject(i).getString("longg");
                                        lat[i] = response.getJSONObject(i).getString("lat");

                                        zone[i] = response.getJSONObject(i).getString("zone");
                                        if (!response.getJSONObject(i).getString("reste").equals("null"))
                                            credit[i] = response.getJSONObject(i).getString("reste");
                                        else
                                            credit[i] = 0 + "";

                                        bd.insertpventelong(b, id_l[i], nom[i], num[i], adresse[i], wilaya[i], type[i], zone[i], credit[i], id_livreur[i], longg[i], lat[i]);

                                    }
                                    progress.dismiss();


                                    //SHOW RESPONSE FROM SERVER

                                    bd.close();

                                    //bd();
                                    // String responseString= response.get(0).toString();


                                } catch (JSONException e) {

                                    progress.dismiss();
                                    e.printStackTrace();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            progress.dismiss();

                            anError.printStackTrace();

                        }


                    });
        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if(navigationView.getMenu().findItem(R.id.nav_home).isChecked()){
                moveTaskToBack(true);}
        }
        return super.onKeyDown(keyCode, event);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        itemMessages =menu.findItem(R.id.menu_messages);

        RelativeLayout badgeLayout = (RelativeLayout) itemMessages.getActionView();
        itemMessagesBadgeTextView = (TextView) badgeLayout.findViewById(R.id.badge_textView);
        itemMessagesBadgeTextView.setVisibility(View.GONE); // initially hidden

        iconButtonMessages = (IconButton) badgeLayout.findViewById(R.id.badge_icon_button);
        iconButtonMessages.setText("");
        iconButtonMessages.setTextColor(getResources().getColor(R.color.white));

        iconButtonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Main.this,notifications.class);
                startActivity(intent1);


            }
        });
        handler= new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                setnotif();
                handler.postDelayed(this, 1000);
            }
        };

//Start
        handler.postDelayed(runnable, 1000);

        return true;
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
                                    Intent intent1 = new Intent(Main.this,MainActivity.class);
                                    startActivity(intent1);
                                    finish();

                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(Main.this, "Pas de connexion", Toast.LENGTH_SHORT).show();

                        }


                    });     }

    }
    public void onDestroy() {
        super.onDestroy();
        bd.close();
        stopService(intentgps);
        handler.removeCallbacks(runnable);



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.dec) {
            bd.open();
            bd.dec();
            bd.close();

            Intent intent = this.getBaseContext().getPackageManager().getLaunchIntentForPackage(this.getBaseContext().getPackageName() );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return true;
        }

        if (id == R.id.importer) {

            Intent intent;

            String[] mimetypes =
                    { "application/vnd.ms-excel", // .xls
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
                    };

            intent = new Intent(Intent.ACTION_GET_CONTENT); // or use ACTION_OPEN_DOCUMENT
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                startActivityForResult(intent, requestcode);
            } catch (ActivityNotFoundException e) {

            }
            return true;
        }
        if (id == R.id.sync) {


            BDD bd = new BDD(this);

            progress.show();
            bd.synchronisatioLocation();
            bd.synchronisatioLiv();


            return true;
        }

        if(id==R.id.download){
            progress.show();
            bd.open();
            int veri=bd.getveri();
            if(veri==0){
                bd.open();
                bd.drop();
                bd.close();
                get_mag();
                get_pvente();
                get_produit();
                get_liv();
                get_pro();
                get_seuil();
                get_promotions();
                get_promotionsproduit();
                get_distance();
                get_produit_disponible();
                return true;}
            else{
                Toast.makeText(getApplicationContext(), "Veuillez synchroniser avant", Toast.LENGTH_SHORT).show();

                progress.dismiss();
            }
            bd.close();
        }



    /*    if (id == R.id.sup) {
            new AlertDialog.Builder(this)
                    .setMessage("Etes vous sur de re télécharger les livraisons? vous risquez de perdre les livraisons modifiées")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int idd) {

                            bd.open();
                            bd.dropliv();
                            bd.close();

                            get_liv();

                        }
                    })
                    .setNegativeButton("Non", null)
                    .show();



            return true;
        }
        if (id == R.id.notifications) {

            Intent intent1 = new Intent(Main.this,notifications.class);
            startActivity(intent1);







            return true;
        }*/
        if (id == R.id.stop) {
            progress.show();

            final Intent intent = new Intent(Main.this, Main.class);
            intent.putExtra("id", this.id+"");

            intent.putExtra("branche", b+"");

            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action", "ajout activite")
                    .addBodyParameter("id", this.id + "")
                    .addBodyParameter("activite","Terminee"+ "")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response != null)
                                try {
                                    bd.open();
                                    bd.close();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(0);


                                    progress.dismiss();

                                    String responseString = response.get(0).toString();


                                } catch (JSONException e) {
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(0);


                                    progress.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            bd.open();
                            bd.close();

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Enregistrement dans la base de données locale", Toast.LENGTH_SHORT).show();


                        }


                    });





            bd.open();
            bd.close();




            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case requestcode:
                String FilePath = data.getData().getPath();


                if (FilePath.contains("/root_path"))
                    FilePath = FilePath.replace("/root_path", "");


                try {
                    if (resultCode == RESULT_OK) {
                        AssetManager am = this.getAssets();
                        InputStream inStream;
                        Workbook wb = null;

                        try {
                            inStream= getContentResolver().openInputStream(data.getData());

                            if (FilePath.substring(FilePath.lastIndexOf(".")).equals(".xls")) {
                                wb = new HSSFWorkbook(inStream);
                            } else if (FilePath.substring(FilePath.lastIndexOf(".")).equals(".xlsx")) {
                                wb = new XSSFWorkbook(inStream);
                            } else {
                                wb = null;
                                Toast.makeText(getApplicationContext(), "Séléctionnez un fichier excel valide", Toast.LENGTH_SHORT).show();
                                return;

                            }

                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Sheet sheet1 = wb.getSheetAt(0);
                        bd.open();
                        ExcelHelper.insertExcelToSqlite(bd, sheet1);

                        bd.close();


                    }
                } catch (Exception ex) {
                    Log.e("POI Error", ex.getMessage().toString());
                    Toast.makeText(getApplicationContext(), "Erreur : Format incompatible", Toast.LENGTH_SHORT).show();

                }

        }
    }



    public static void dismiss(){

        try {
            progress.dismiss();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void main(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,R.id.nav_gventes, R.id.nav_slideshow,
                R.id.nav_pvente, R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);





        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // setContentView(R.layout.fragment_home);
              /*  navigationView.setCheckedItem(R.id.nav_home);
               ((AppCompatActivity) MainActivity.this).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, H,"CCC").setPrimaryNavigationFragment(H).commit();
*/AndroidNetworking.post(DATA_INSERT_URL)
                                .addBodyParameter("action", "ajout activite")
                                .addBodyParameter("id",id + "")
                                .addBodyParameter("activite","Demarrage")
                                .setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                        if (response != null)
                                            try {



                                                progress.dismiss();

                                                String responseString = response.get(0).toString();


                                            } catch (JSONException e) {


                                                progress.dismiss();
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "Erreur veuillez ressayer l'action", Toast.LENGTH_SHORT).show();
                                            }

                                    }

                                    //ERROR
                                    @Override
                                    public void onError(ANError anError) {
                                        bd.open();
                                        bd.close();


                                        progress.dismiss();
                                        Toast.makeText(getApplicationContext(), "Erreur veuillez ressayer l'action", Toast.LENGTH_SHORT).show();


                                    }


                                });
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_gallery,1);



             /*   setContentView(R.layout.pic);

                ivCameraPreview = findViewById(R.id.ivCameraPreview);
                Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
                btnTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if app has permission to access the camera.
                        if (EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.CAMERA)) {
                            launchCamera();

                        } else {
                            //If permission is not present request for the same.
                            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.permission_text), CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
                        }
                    }
                });*/


                /*   TextView mTextMessage = (TextView) findViewById(R.id.message);
                mTextMessage.setText("Home");*/

                    }
                });
    }

    /*public void setVisits(){
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
                                ColorfulRingProgressView crpv = (ColorfulRingProgressView) findViewById(R.id.progressBar5);
                                crpv.setPercent(result);



                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(Main.this, "Erreur veuillez ressayer l'action ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(Main.this, "Erreur veuillez ressayer l'action", Toast.LENGTH_SHORT).show();


                    }


                });}


*/
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
                                TextView nn=findViewById(R.id.nomm);
                                nn.setText(response.getJSONObject(0).getString("nom"));
                                TextView m=findViewById(R.id.maill);
                                m.setText(response.getJSONObject(0).getString("mail"));





                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(Main.this, "Erreur veuillez ressayer l'action", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {

                        Toast.makeText(Main.this, "Erreur veuillez ressayer l'action", Toast.LENGTH_SHORT).show();


                    }


                });}

    public static void get_liv(){

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action", "get livraisons")
                .addBodyParameter("id", id + "")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null)
                            try {
                                produitx = new String[response.length()][50];
                                quantitex = new String[response.length()][50];
                                quantite_ux = new String[response.length()][50];
                                IDPx = new String[response.length()][50];
                                restex = new String[response.length()];

                                montant_bonx = new String[response.length()][50];
                                payementx = new String[response.length()];
                                endomagex = new String[response.length()][50];
                                QVx = new String[response.length()][50];
                                QVUx = new String[response.length()][50];
                                id_lx = new int[response.length()];
                                idf = new int[response.length()];

                                int[] bonx = new int[response.length()];
                                String[] pventex = new String[response.length()];

                                String[] heurex = new String[response.length()];
                                String[] faitex = new String[response.length()];
                                String[] motifx = new String[response.length()];
                                String[] photo = new String[response.length()];

                                final String[] datex = new String[response.length()];
                                String[] validex = new String[response.length()];


                                bd.open();


                                //SHOW RESPONSE FROM SERVER
                                for (int i = 0; i < response.length(); i++) {
                                    id_lx[i] = response.getJSONObject(i).getInt("id");
                                    bonx[i] = response.getJSONObject(i).getInt("bon");
                                    pventex[i] = response.getJSONObject(i).getString("pvente");

                                    heurex[i] = response.getJSONObject(i).getString("heure");
                                    faitex[i] = response.getJSONObject(i).getString("faite");
                                    photo[i] = "";

                                    motifx[i] = response.getJSONObject(i).getString("motif");

                                    datex[i] = response.getJSONObject(i).getString("date");
                                    validex[i] = response.getJSONObject(i).getString("valide");

                                    restex[i] = response.getJSONObject(i).getString("reste");
                                    payementx[i] = response.getJSONObject(i).getString("payement");
                                    ;

                                    String date_m = response.getJSONObject(i).getString("date_m");

                                    idf[i]=bd.InsertLiv(id, id_lx[i], response.getJSONObject(i).getString("id_pvente"), bonx[i], pventex[i], heurex[i], faitex[i], motifx[i], datex[i], validex[i], date_m, payementx[i], restex[i], response.getJSONObject(i).getString("user"), photo[i]);
                                    l = i;
                                    AndroidNetworking.post(DATA_INSERT_URL)
                                            .addBodyParameter("action", "get produits")
                                            .addBodyParameter("id", id_lx[i] + "")

                                            .setTag("test")
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                    if (response != null)
                                                        try {


                                                            bd.open();


                                                            //SHOW RESPONSE FROM SERVER
                                                            for (int j = 0; j < response.length(); j++) {
                                                                IDPx[l][j] = response.getJSONObject(j).getString("id");
                                                                ;

                                                                produitx[l][j] = response.getJSONObject(j).getString("produit");
                                                                ;
                                                                quantitex[l][j] = response.getJSONObject(j).getString("quantite");
                                                                ;
                                                                quantite_ux[l][j] = response.getJSONObject(j).getString("quantite_u");
                                                                ;

                                                                montant_bonx[l][j] = response.getJSONObject(j).getString("montant_bon");
                                                                ;
                                                                endomagex[l][j] = response.getJSONObject(j).getString("endomage");
                                                                ;
                                                                QVx[l][j] = response.getJSONObject(j).getString("quantite_v");
                                                                ;
                                                                QVUx[l][j] = response.getJSONObject(j).getString("quantite_u_v");
                                                                ;
                                                                bd.open();
                                                                int iduu=response.getJSONObject(j).getInt("id_livraison");
                                                                for(int k=0;k<id_lx.length;k++){
                                                                    if(id_lx!=null) {
                                                                        if (response.getJSONObject(j).getInt("id_livraison") == id_lx[k]) {
                                                                            iduu = idf[k];
                                                                        }
                                                                    }
                                                                }
                                                                bd.Insertproduitv(Integer.parseInt(IDPx[l][j]), id, iduu, produitx[l][j], quantitex[l][j], quantite_ux[l][j], montant_bonx[l][j], endomagex[l][j], QVx[l][j], QVUx[l][j], datex[l]);
                                                                bd.close();


                                                            }



                                                            // String responseString= response.get(0).toString();


                                                        } catch (JSONException e) {


                                                            e.printStackTrace();
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
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {

                        progress.dismiss();
                    }


                });




    }


    public static  void get_produit(){
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get produitsss")
                .addBodyParameter("id",id+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                int[] id_l1= new int[response.length()];
                                String [] produit1= new String[response.length()];
                                String [] prix_f= new String[response.length()];
                                String []  prix_u= new String[response.length()];
                                String [] quantite_f= new String[response.length()];
                                String [] code_bar= new String[response.length()];
                                String [] fardeau= new String[response.length()];
                                String [] quantite_u= new String[response.length()];
                                String [] prix_uu= new String[response.length()];





                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    id_l1[i] = response.getJSONObject(i).getInt("id");
                                    produit1[i] = response.getJSONObject(i).getString("nom");
                                    prix_u[i] = (int)Double.parseDouble(response.getJSONObject(i).getString("quantite_f"))+" F";
                                    prix_f[i] = response.getJSONObject(i).getString("prix_f");
                                    quantite_f[i] = (int)Double.parseDouble(response.getJSONObject(i).getString("quantite_f"))+"";
                                    code_bar[i] = response.getJSONObject(i).getString("code_bar");
                                    fardeau[i] = response.getJSONObject(i).getString("fardeau");
                                    quantite_u[i] = (int)Double.parseDouble(response.getJSONObject(i).getString("quantite_u"))+"";
                                    prix_uu[i] = Double.parseDouble(response.getJSONObject(i).getString("prix_d"))+"";

                                    bd.Insertproduit(id_l1[i],produit1[i],prix_u[i],prix_f[i],quantite_f[i],code_bar[i],fardeau[i],quantite_u[i],prix_uu[i]);



                                }
                                progress.dismiss();


                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {

                        progress.dismiss();
                        anError.printStackTrace();

                    }


                });


    }

    public static void get_pro(){
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get produitssss")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                int[] id_lv= new int[response.length()];
                                String[] produitv= new String[response.length()];
                                String[]   prix_fv= new String[response.length()];
                                String[] prix_uv= new String[response.length()];
                                String[] code_bar= new String[response.length()];





                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    id_lv[i] = response.getJSONObject(i).getInt("id");
                                    produitv[i] = response.getJSONObject(i).getString("nom");
                                    prix_uv[i] = response.getJSONObject(i).getString("prix_d");
                                    prix_fv[i] = response.getJSONObject(i).getString("prix_f");
                                    code_bar[i] = response.getJSONObject(i).getString("code_bar");

                                    bd.Insertpro(id_lv[i],produitv[i],prix_uv[i],prix_fv[i]);
                                    if(i==response.length()-1){
                                        if (android.os.Build.VERSION.SDK_INT == 25) {
                                            ToastCompat.makeText(context, "Téléchargement reussi", Toast.LENGTH_SHORT)
                                                    .setBadTokenListener(new BadTokenListener() {
                                                        @Override
                                                        public void onBadTokenCaught(@NonNull Toast toast) {
                                                            Log.e("failed toast", "");
                                                        }
                                                    }).show();
                                        } else {
                                            Toast.makeText(context, "Téléchargement reussi", Toast.LENGTH_SHORT).show();
                                        }
                                        bd.open();
                                        bd.insertveri(1);
                                        bd.close();

                                    }

                                }
                                progress.dismiss();

                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {


                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {


                        progress.dismiss();
                        anError.printStackTrace();

                    }


                });


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
    public static void get_seuil(){
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get seuil")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                double[] id_lv= new double[response.length()];






                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    id_lv[i] = response.getJSONObject(i).getDouble("seuil");
                                    bd.open();
                                    bd.setseuil(id_lv[i]);
                                    bd.close();


                                }
                                progress.dismiss();

                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {


                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {


                        progress.dismiss();
                        anError.printStackTrace();

                    }


                });


    }


    public static void get_promotions(){
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get promotions")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    bd.open();
                                    bd.insertpromotion(response.getJSONObject(i).getInt("id_promotion"),response.getJSONObject(i).getString("dsc_promotion"),response.getJSONObject(i).getString("nom_promotion"),response.getJSONObject(i).getString("date_debut"),response.getJSONObject(i).getString("date_fin"),response.getJSONObject(i).getString("type_promotion"));
                                    bd.close();


                                }
                                progress.dismiss();

                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {


                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {


                        progress.dismiss();
                        anError.printStackTrace();

                    }


                });


    }
    public static void get_promotionsproduit(){
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get promotionsproduit")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    bd.open();

                                    bd.insertpromotionproduit(response.getJSONObject(i).getInt("id"),response.getJSONObject(i).getString("prix_reduction"),response.getJSONObject(i).getInt("id_promotion"),response.getJSONObject(i).getInt("id_produit"));
                                    bd.close();


                                }
                                progress.dismiss();

                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {


                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {


                        progress.dismiss();
                        anError.printStackTrace();

                    }


                });


    }
    public static void get_distance(){
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get distance")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                double[] id_lv= new double[response.length()];






                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    id_lv[i] = response.getJSONObject(i).getDouble("distance");
                                    bd.open();
                                    bd.setdistance(id_lv[i]);
                                    bd.close();


                                }
                                progress.dismiss();

                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {


                                progress.dismiss();
                                e.printStackTrace();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {


                        progress.dismiss();
                        anError.printStackTrace();

                    }


                });


    }



    private void get_produit_disponible() {


        progress.show();

        AndroidNetworking.post(DATA_URL)
                .addBodyParameter("action","0")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null){
                            int id;
                            String nom;
                            String famille;
                            int fardeau;
                            int palette;
                            double prix_usine;
                            int quantite_u;


                            try {
                                bd.open();
                                for(int i = 0 ; i<response.length();i++) {

                                    id = response.getJSONObject(i).getInt("id");
                                    nom = response.getJSONObject(i).getString("nom");
                                    famille = response.getJSONObject(i).getString("famille");
                                    fardeau = response.getJSONObject(i).getInt("fardeau");
                                    palette = response.getJSONObject(i).getInt("palette");
                                    prix_usine = response.getJSONObject(i).getDouble("prix_usine");
                                    quantite_u = response.getJSONObject(i).getInt("quantite_u");
                                    bd.Insertproduct(id,nom,famille,fardeau,palette,prix_usine,quantite_u);

                                }


                                bd.close();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }












                        }



                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {


                        Log.e("imad","errorS");
                        anError.printStackTrace();

                    }


                });
    }

}
