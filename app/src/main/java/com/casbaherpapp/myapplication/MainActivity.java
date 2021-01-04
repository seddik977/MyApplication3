package com.casbaherpapp.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.imad.OrderProductsDistributeur;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import android.content.Intent;
import android.location.LocationManager;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity  implements  View.OnClickListener{

    private AppBarConfiguration mAppBarConfiguration;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";

    private static final int PERMISSIONS_REQUEST = 100;
    private static final int CAMERA_REQUEST_CODE = 1450;
    private static final int CAMERA_PERMISSION_CODE = 1460;

    private String mCurrentPhotoPath,b;

    private TextView mdp;

    private TextInputLayout userNameTextInputLayout;
    private TextInputLayout passWordTextInputLayout;
    private Button login;
    private int id=-1,v=1;
    private String pass="",user="";
    public ProgressDialog progress ;
    private BDD bd;
    private Intent intent;
    private static int c=0;
    private RadioGroup roleRadioGroup;
    private  String role;


    @Override
    protected void onStart(){
        super.onStart();
        final Intent myIntent = new Intent(MainActivity.this, intro.class);

        if(c==0) {
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            myIntent.putExtra("keep", true);

            startActivity(myIntent);
            c++;}


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(c==1){
                    myIntent.putExtra("keep", false);
                    startActivity(myIntent);
                    c++;}
            }
        }, 5000);



    }
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.login);
        //REFERENCE VIEWS
        userNameTextInputLayout = (TextInputLayout) findViewById(R.id.usernameTIL);
        passWordTextInputLayout=(TextInputLayout) findViewById(R.id.passwordTIL);
        mdp=  (TextView) findViewById(R.id.textmdp);

        login= (Button) findViewById(R.id.login);

        progress = new ProgressDialog(this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        bd= new BDD(this);

        // disable dismiss by tapping outside of the dialog

        //HANDLE EVENTS
        login.setOnClickListener(this);
        mdp.setOnClickListener(this);

        verifierLoggedIn();
//        p.setText(pass);
//        u.setText(user);
//        if(!pass.equals("")){
//            p.requestFocus();
//            p.setSelection(pass.length());}
        roleRadioGroup = (RadioGroup)findViewById(R.id.roleRadioGroup);
        role =
                ((RadioButton)findViewById(roleRadioGroup.getCheckedRadioButtonId()))
                        .getText().toString();
        roleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 role =
                        ((RadioButton)findViewById(roleRadioGroup.getCheckedRadioButtonId()))
                                .getText().toString();

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
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
*/
                        NavigationView navigationView = (NavigationView) MainActivity.this.findViewById(R.id.nav_view);
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
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
    }
    public void verifierLoggedIn(){
        bd.open();

        id=bd.getID();
        user=bd.getuser();
        pass=bd.getpassword();
        role=bd.getrole();

        b=bd.getb();

        Log.e("role",bd.getrole()+user+pass+b);


        if(!user.equals("") && !pass.equals("0")){
          if(role.equals("Livreur")){
              GPStask();
          }else{
              RedirectDistributeur(id,b, role);
          }


        }
        else{

            user=bd.getuserR();
            pass=bd.getpasswordD();
            b=bd.getb();
        }
        if(pass.equals("0")){
            pass="";
        }
        bd.close();
    }
public void loginDistributeur(){
    final String pass = String.valueOf(passWordTextInputLayout.getEditText().getText().toString().trim());
    String s=String.valueOf(userNameTextInputLayout.getEditText().getText().toString().trim()).toLowerCase();
    final String user = s.replaceAll("\\s+","");
    if(user==null || pass==null)
    {
        Toast.makeText(MainActivity.this, "Remplissez tous les champs SVP", Toast.LENGTH_SHORT).show();
    }
    else
    {


        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","login dist")
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
                                login.setClickable(true);
                                progress.dismiss();
                                //SHOW RESPONSE FROM SERVER
                                id=response.getJSONObject(0).getInt("id");
                                b=response.getJSONObject(0).getString("branche");
                                Toast.makeText(getBaseContext(), id+"==="+b, Toast.LENGTH_SHORT).show();
                                bd.open();

                                bd.Insert(id,user,pass,b, role);

                                bd.close();


                                // String responseString= response.get(0).toString();
RedirectDistributeur(id,b, role);

                            } catch (JSONException e) {

                                login.setClickable(true);
                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                       Log.e("error",anError.getMessage());
                        login.setClickable(true);
                        progress.dismiss();
                        Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();

                    }


                });

    }

}

    public void loginLivreur() {
        final String pass = String.valueOf(passWordTextInputLayout.getEditText().getText().toString().trim());
        String s=String.valueOf(userNameTextInputLayout.getEditText().getText().toString().trim()).toLowerCase();
        final String user = s.replaceAll("\\s+","");
        if(user==null || pass==null)
        {
            Toast.makeText(MainActivity.this, "Remplissez tous les champs SVP", Toast.LENGTH_SHORT).show();
        }
        else
        {
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
                                    login.setClickable(true);
                                    progress.dismiss();
                                    //SHOW RESPONSE FROM SERVER
                                    id=response.getJSONObject(0).getInt("id");
                                    b=response.getJSONObject(0).getString("branche");
                                    bd.open();

                                    bd.Insert(id,user,pass,b,role);

                                    bd.close();
                                    GPStask();

                                    // String responseString= response.get(0).toString();


                                } catch (JSONException e) {
                                    login.setClickable(true);
                                    progress.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            login.setClickable(true);
                            progress.dismiss();
                            Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();

                        }


                    });

        }

    }
    public void GPStask(){
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // finish();
            Toast.makeText(MainActivity.this, "Activez la localisation SVP", Toast.LENGTH_SHORT).show();

        }
        else {
            //Check whether this app has access to the location permission//


            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

//If the location permission has been granted, then start the TrackerService//

            if (permission == PackageManager.PERMISSION_GRANTED) {
                startTrackerService();
            } else {

//If the app doesn’t currently have access to the user’s location, then request access//

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST);
                v=0;
            }

        }



    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {


        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //...then start the GPS tracking service//

            startTrackerService();
        } else  {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTrackerService() {
        intent = new Intent(this, GPS.class);
        intent.putExtra("id", id+"");


        startService(intent);

        final Intent intentt = new Intent(this, ForegroundService.class);
        intentt.putExtra("id", id+"");


            startService(intentt);



        Intent myIntent = new Intent(MainActivity.this, Main.class);

        myIntent.putExtra("id", id+"");

        myIntent.putExtra("branche", b+"");
        myIntent.putExtra("role", role +"");

        myIntent.putExtra(Intent.EXTRA_INTENT, intent);


        startActivity(myIntent);
       finish();

    }
private void RedirectDistributeur(int id,String branche,String role){

    final Intent intentt = new Intent(this, ForegroundService.class);
    intentt.putExtra("id", id+"");
    startService(intentt);
    Intent myIntent = new Intent(MainActivity.this, OrderProductsDistributeur.class);

    myIntent.putExtra("id", id+"");

    myIntent.putExtra("branche", b+"");
    myIntent.putExtra("role", this.role +"");

    myIntent.putExtra(Intent.EXTRA_INTENT, intent);


    startActivity(myIntent);
    finish();
}

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case  R.id.login:

                 progress.show();
                 login.setClickable(false);

                 String username = userNameTextInputLayout.getEditText().getText().toString().trim();
                 String password  = passWordTextInputLayout.getEditText().getText().toString().trim();


                 if((username.length()<1 || password.length()<1  ))
                 {
                     login.setClickable(true);
                     progress.dismiss();
                     Toast.makeText(MainActivity.this, "Remplissez tous les champs SVP;", Toast.LENGTH_SHORT).show();

                 }
                 else{

                    if(role.equals("Livreur")){
                        Toast.makeText(getBaseContext(),"this is livreur", Toast.LENGTH_SHORT).show();
                        loginLivreur();
                    }else if(role.equals("Distributeur")){

                        loginDistributeur();
                  Toast.makeText(getBaseContext(), "this is distributeur", Toast.LENGTH_SHORT).show();
                    }


                 }

                 break;
             case R.id.textmdp:

                 Intent myIntent = new Intent(MainActivity.this, mail.class);
                 startActivity(myIntent);

                 break;
             default:
                 break;



         }
    }
}
