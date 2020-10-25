package com.casbaherpapp.myapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class qr extends AppCompatActivity  {

    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int[] id_l,id_l1;
    private String[] produit,prix_u,prix_f;
    private RecyclerView mRecyclerView, mRecyclerView1;
    private Adapter2 mAdapter;
    private Adapter3 mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] nom,num,adresse,zone,ID,credit,montant,quantite,quantite_u,endomage,QV,QVU,produits,IDP,quantite_f;

    private String b,payement,reste;
    private Integer[] ind,ind1,select,selected;
    private Button ajout,supprimer,modifier,appel,liv;
    private TextInputEditText n,a,z,nm;
    private    AutoCompleteTextView w;
    private BDD bd;
    private int id=-1,yaw=0,idp,idpv=-1,posp,pospv,s=0,id_livraison,bon,idselect;
    private String pvente="";
    private FloatingActionButton f;

    private FusedLocationProviderClient fusedLocationClient;
    private Spinner P;
    private boolean veri=false,verii=false;
    private List<String> arraySpinner;

    public void onCreate(Bundle savedInstanceState) {
        bd = new BDD(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(qr.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr);
        bd = new BDD(qr.this);
        progress = new ProgressDialog(this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);



        appel= findViewById(R.id.imprimer);






        Intent i = getIntent();

        Bundle extras = i.getExtras();
        if(extras.containsKey("id")) {
            id = Integer.parseInt(i.getStringExtra("id"));
            try {
                // generate a 150x150 QR code
                Bitmap bm= TextToImageEncode(id+"");

                if(bm != null) {
                    ImageView image=findViewById(R.id.qr);
                    image.setImageBitmap(bm);
                }
            } catch (WriterException e) {

            }


        }
    /*    if(extras.containsKey("branche")) {
            b = i.getStringExtra("branche");


        }
        if(extras.containsKey("nom")) {

            n.setText(i.getStringExtra("nom"));

        }*/

Onclick();


    }


    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    150, 150, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 150, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
    public void Onclick(){




        appel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                    }
                });

    }






    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(qr.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(qr.this, PERMISSIONS_STORAGE, 9);
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
            Toast.makeText(qr.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }



    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(qr.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+nm.getText()));
            startActivity(callIntent);
        }else{
            Toast.makeText(qr.this, "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }



}
