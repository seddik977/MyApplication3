package com.casbaherpapp.myapplication;


import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class intro extends AppCompatActivity {
    private EditText mail;
    private Button send_p;
    private TextInputEditText D,H;
    private String e;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int id_commande=0;
    private FloatingActionButton fab;
    private static final int PERMISSIONS_REQUEST = 100;
    private static final int CAMERA_REQUEST_CODE = 1450;
    private String mCurrentPhotoPath,b;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_PERMISSION_CODE = 1460;
    private FusedLocationProviderClient fusedLocationClient;

    public BDD bd;
    private String longg="",lat="",nom="",num="",wilaya="",commune="",adresse="",heure="",date="",temp="";


    /** Called when the activity is first created. */
    boolean keep;
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        intent = this.getIntent();
        boolean keep = intent.getExtras().getBoolean("keep");
        if(keep==true)
        {
            //execute your code here

        }
    }
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        keep = intent.getExtras().getBoolean("keep");
        if(keep==false)
        {
            intro.this.finish();
        }
    }
    private void launchCamera() {

        Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(intro.this,
                    "com.example.casbahdzcommandes",
                    photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            //Start the camera application
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Preview the image captured by the camera
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            // ivCameraPreview.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if(bitmap!=null){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                byte[] byte_arr = stream.toByteArray();
                temp = Base64.encodeToString(byte_arr, Base64.DEFAULT);}
        }
    }

    public void GPS() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {



                            longg=location.getLongitude() + "";
                            lat= location.getLatitude() + "";



                        }
                    }
                });

    }




    public void GPStask(){
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // finish();

        }
        else {
            //Check whether this app has access to the location permission//


            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

//If the location permission has been granted, then start the TrackerService//

            if (permission == PackageManager.PERMISSION_GRANTED) {
                GPS();
            } else {

//If the app doesn’t currently have access to the user’s location, then request access//

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST);

            }

        }



    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {


//If the permission has been granted...//
        if (EasyPermissions.hasPermissions(intro.this, Manifest.permission.CAMERA)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, intro.this);
            launchCamera();

        }

        else {if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //...then start the GPS tracking service//

            GPS();
        } else  {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(intro.this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }}
    }



    public void onPermissionsGranted(int requestCode, List<String> perms) {
        launchCamera();
    }


    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }




    private void send(){
        send_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send_p.setClickable(false);
                progress.show();
                //GET VALUES
                String email=mail.getText().toString();


                //BASIC CLIENT SIDE VALIDATION
                if((email.length()<1  ))
                {
                    Toast.makeText(getApplicationContext(), "entrer l'email SVP", Toast.LENGTH_SHORT).show();
                    send_p.setClickable(true);
                    progress.dismiss();
                }
                else{
                    mail(email);

                }
            }
        });

    }
    public void mail(String email) {
        e=email;
        if(email==null )
        {
            Toast.makeText(getApplicationContext(), "Remplissez tous les champs SVP", Toast.LENGTH_SHORT).show();
        }
        else
        {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            AndroidNetworking.post(DATA_INSERT_URL)
                    .addBodyParameter("action","send")
                    .addBodyParameter("mail",email)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response != null)
                                try{
                                    send_p.setClickable(true);
                                    progress.dismiss();

                                    //SHOW RESPONSE FROM SERVER
                                    String  password=response.getJSONObject(0).getString("password");
                                    String rep=response.get(0).toString();





                                } catch (JSONException e) {
                                    progress.dismiss();
                                    send_p.setClickable(true);
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Mail n'existe pas", Toast.LENGTH_SHORT).show();
                                }

                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            progress.dismiss();
                            send_p.setClickable(true);
                            Toast.makeText(getApplicationContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        }


                    });

        }

    }


    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }




}