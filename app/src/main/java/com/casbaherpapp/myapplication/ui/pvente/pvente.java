package com.casbaherpapp.myapplication.ui.pvente;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.Adapter2;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.Item2;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.MainActivity;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.ajoutpvente;
import com.casbaherpapp.myapplication.editventes;
import com.casbaherpapp.myapplication.modpvente;
import com.casbaherpapp.myapplication.produits;
import com.casbaherpapp.myapplication.qr;
import com.casbaherpapp.myapplication.topclients;
import com.casbaherpapp.myapplication.ui.gallery.GalleryFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class pvente extends Fragment {

    private pventeM shareViewModel;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int[] id_l;
    private String[] nom,num,adresse,zone,ID,credit,wilaya,type,mod,id_livreur;
    private RecyclerView mRecyclerView;
    private Adapter2 mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context con;
    private View v;
    private int id=-1;
    private String b;
    private TextInputEditText n,a,z,nm;
    private    AutoCompleteTextView w;
    private EditText theFilter;
    private Integer[] ind;
    private Button ajout,supprimer,modifier,appel,liv;
    private BDD bd;
    private int idp=-1, pos=-1,idpv=-1;
    private FusedLocationProviderClient fusedLocationClient;
    private Spinner P;
    private boolean veri=false,verii=false;
    private List<String> arraySpinner;
    private boolean  shouldRefreshOnResume=false;

    private Button premier,dernier,left,right;
    private EditText center;
    private int cptpventes=1;



    public void onResume(){


        super.onResume();
        //update your fragment
        if(shouldRefreshOnResume && theFilter.getText().toString().equals("")) {
            NavigationView navigationView = ((Main)getContext()).findViewById(R.id.nav_view);
            navigationView.getMenu().performIdentifierAction(R.id.nav_pvente,1);
        if(Main.getCreditt()==1){
            navigationView.getMenu().performIdentifierAction(R.id.nav_gallery,1);
            Main.setCreditt(0);
        }
        }
    }
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bd = new BDD(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


        shareViewModel =
                ViewModelProviders.of(this).get(pventeM.class);
        final View root = inflater.inflate(R.layout.fragment_pvente, container, false);
       /* final TextView textView = root.findViewById(R.id.text_share);
        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        FloatingActionButton fab = ((Main)getContext()).findViewById(R.id.fab);
        fab.hide();
        progress = new ProgressDialog(getContext());
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        con=root.getContext();
        v=root;
        id = ((Main)getContext()).getData();
        b = ((Main)getContext()).getB();



        mRecyclerView = root.findViewById(R.id.recycler3);
        theFilter = (EditText) root.findViewById(R.id.searchFilter);
        verif();
        //get_pvente();

        bdd();
        filt();
        init();
        Onclick();

        return root;
    }
    public void init(){
        premier = v.findViewById(R.id.premier);
        dernier = v.findViewById(R.id.dernier);
        left = v.findViewById(R.id.left);
        right = v.findViewById(R.id.right);
        center = v.findViewById(R.id.center);

        center.setText("1");

        bd.open();

        cptpventes=(bd.getcountpventes()/30) ;
        if((bd.getcountpventes()/30)!=((double)bd.getcountpventes()/30)){
            cptpventes++;
        }
        bd.close();


        premier.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        center.setText("1");
                        theFilter.setText("");

                        bdd();

                    }
                });

        right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        theFilter.setText("");

                        int cpt= Integer.parseInt(center.getText().toString());
                        cpt++;
                        if(cpt<=(cptpventes)){
                            center.setText(cpt+"");

                            bddindex((cpt-1)*30);}

                    }
                });


        left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        theFilter.setText("");

                        int cpt= Integer.parseInt(center.getText().toString());
                        cpt--;

                        if(cpt>=1){
                            center.setText(cpt+"");

                            bddindex((cpt-1)*30);}

                    }
                });


        dernier.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        theFilter.setText("");

                        center.setText(cptpventes+"");

                        bddindex((cptpventes-1)*30);


                    }
                });
center.setFocusable(false);
       center.setInputType(InputType.TYPE_NULL);
        center.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View vv, boolean hasFocus) {
                if (hasFocus) {



                    final NumberPicker task = new NumberPicker(vv.getContext());

                    task.setMinValue(1);
                    task.setMaxValue(cptpventes);
                    task.setWrapSelectorWheel(true);
                    AlertDialog dialog = new AlertDialog.Builder(vv.getContext())
                            .setTitle("Pagination")
                            .setMessage("Choisissez une page")
                            .setView(task)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String quan = String.valueOf(task.getValue());
                                    if (Integer.parseInt(quan) != 0 && !quan.isEmpty()) {
                                        theFilter.setText("");
                                        center.setText(task.getValue()+"");
                                        bddindex((task.getValue()-1)*30);

                                    }


                                }
                            })
                            .setNegativeButton("Annuler", null)
                            .create();
                    dialog.show();




                }
            }
        });
        center.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                        final NumberPicker task = new NumberPicker(v.getContext());

                        task.setMinValue(1);
                        task.setMaxValue(cptpventes);
                        task.setWrapSelectorWheel(true);
                        AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                                .setTitle("Pagination")
                                .setMessage("Choisissez une page")
                                .setView(task)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String quan = String.valueOf(task.getValue());
                                        if (Integer.parseInt(quan) != 0 && !quan.isEmpty()) {
                                            theFilter.setText("");
                                            center.setText(task.getValue()+"");
                                            bddindex((task.getValue()-1)*30);

                                        }


                                    }
                                })
                                .setNegativeButton("Annuler", null)
                                .create();
                        dialog.show();




                    }
                });






    }


    public void bdd(){

        bd.open();
        Cursor c =bd.getpvente();
        int j =c.getCount();

        id_l= new int[j];
        nom= new String[j];
        zone= new String[j];
        num= new String[j];
        adresse= new String[j];
        credit= new String[j];
        wilaya= new String[j];
        type= new String[j];
        id_livreur= new String[j];
        mod= new String[j];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));

            nom[i] = c.getString(c.getColumnIndex("nom"));
            num[i] = c.getString(c.getColumnIndex("num"))+"";
            adresse[i] = c.getString(c.getColumnIndex("adresse"));
            zone[i] = c.getString(c.getColumnIndex("zone"));
            wilaya[i] = c.getString(c.getColumnIndex("wilaya"));
            type[i] = c.getString(c.getColumnIndex("type"));
            id_livreur[i] = c.getString(c.getColumnIndex("id_livreur"));

            credit[i] = c.getString(c.getColumnIndex("credit"));
            mod[i] = c.getString(c.getColumnIndex("modifier"));





            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(getContext(), "Pas de pventes où vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

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
                                    Intent intent1 = new Intent(v.getContext(), MainActivity.class);
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
    public void select(int f){
        String charSequence=theFilter.getText().toString();
        int cpt=0;
        ind= new Integer[id_l.length];

        ArrayList<Item2> exampleList = new ArrayList<>();
        for(int i=0;i<id_l.length;i++) {
            if(i<50){
            if(nom[i].toLowerCase().contains(charSequence.toString().toLowerCase()) || (id_l[i]+"").contains(charSequence)){
                ind[cpt]=i;
                cpt++;


                exampleList.add(new Item2("CAS"+id+"-"+id_l[i] + "", nom[i], "", credit[i]));

            }}
        }


        mRecyclerView = v.findViewById(R.id.recycler3);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter2(exampleList);
       // mAdapter.setSelectedItem(f);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




    }

    public void ajout(){

        int cpt=0,total=0,Q=0,qu=0;
        String q="",p="";
        int m=0;
        String[] pv;
        pv=new String[id_l.length];
        ArrayList<Item2> exampleList = new ArrayList<>();

      TextView totall= v.findViewById(R.id.totalQ);

      bd.open();
        totall.setText("NB :"+bd.getpvente1());
        bd.close();

        for(int i=0;i<id_l.length;i++) {
            if(i<50) {
                if (mod[i] != null) {
                    if (mod[i].equals("oui"))
                        exampleList.add(new Item2("", nom[i], "", credit[i]));
                    else
                        exampleList.add(new Item2("CAS"+id+"-"+id_l[i] + "", nom[i], "", credit[i]));

                } else
                    exampleList.add(new Item2("CAS"+id+"-"+id_l[i] + "", nom[i], "", credit[i]));
            }
        }




        mRecyclerView = v.findViewById(R.id.recycler3);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter2(exampleList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




    }
    public void dd(){

        int bon =1,id_liv=1,idp=1;
        bd.open();
        Cursor c=bd.getpventee();

        while (c.moveToNext()){
            id_liv = c.getInt(c.getColumnIndex("id"))+1;
        }
        bd.open();

        int id=bd.getID();
        bd.close();
        bd.insertpvente(b,id_liv,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),P.getSelectedItem().toString()+"",z.getText().toString(),"0",id+"");



        progress.dismiss();
        bd.close();

    }
    public void ddd(int id){
        bd.open();

        int idd=bd.getID();
        bd.close();
        int bon =1,id_liv=1,idp=1;
        bd.open();


        bd.insertpvente(b,id,n.getText().toString(),nm.getText().toString(),a.getText().toString(),w.getText().toString(),P.getSelectedItem().toString()+"",z.getText().toString(),"0",idd+"");



        progress.dismiss();
        bd.close();

    }

    public void Onclick(){


        Button qr=v.findViewById(R.id.top);
        qr.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent myIntent = new Intent(getContext(), topclients.class);


                        myIntent.putExtra("id", idp + "");

                        startActivity(myIntent);

                    }





                });


        mRecyclerView.addOnItemTouchListener(            new GalleryFragment.RecyclerItemClickListener(con, new GalleryFragment.RecyclerItemClickListener.OnItemClickListener() {
            public void onItemClick(View v, int position){
                int m=position;
                if(ind!=null)
                    position=ind[position];



                idp=id_l[position];
                Intent myIntent = new Intent(getContext(), modpvente.class);

                myIntent.putExtra("idp", idp+"");
                myIntent.putExtra("branche", b+"");
                myIntent.putExtra("zone", zone[position]+"");
                myIntent.putExtra("adresse", adresse[position]+"");
                myIntent.putExtra("nom", nom[position]+"");
                myIntent.putExtra("num", num[position]+"");
                myIntent.putExtra("type", type[position]+"");
                myIntent.putExtra("wilaya", wilaya[position]+"");
                myIntent.putExtra("id", id+"");
                startActivity(myIntent);


                pos=position;
                //select(m);






            }


        }));

        FloatingActionButton bb = v.findViewById(R.id.add);
        bb.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

theFilter.setText("");

                        Intent myIntent = new Intent(getContext(), ajoutpvente.class);

                        myIntent.putExtra("branche", b+"");



                        myIntent.putExtra("id", id+"");

                        startActivity(myIntent);




                    }
                });



       ImageButton bbb = v.findViewById(R.id.qr);
        bbb.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        try {

                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                            startActivityForResult(intent, 0);

                        } catch (Exception e) {

                            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                            startActivity(marketIntent);

                        }




                    }
                });




     /*   ajout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progress.show();
                        ajout.setEnabled(false);
                        if(n.getText().toString().isEmpty() || w.getText().toString().isEmpty() ||nm.getText().toString().isEmpty() || a.getText().toString().isEmpty() || z.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Remplissez tous les champs SVP!", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            ajout.setEnabled(true);

                        }
                        else{
                            //dd();


                                    fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {

                                        public void onSuccess(final Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {


                                                   veri=true;
                                                                    String longg = location.getLongitude()+"";
                                                                     String latt = location.getLatitude()+"";
                                                AndroidNetworking.post(DATA_INSERT_URL)
                                                        .addBodyParameter("action","add F)
                                                        .addBodyParameter("branche",b+"")
                                                        .addBodyParameter("lieu",a.getText()+"")
                                                        .addBodyParameter("zone",z.getText()+"")
                                                        .addBodyParameter("nom",n.getText()+"")
                                                        .addBodyParameter("num",nm.getText()+"")
                                                        .addBodyParameter("wilaya",w.getText()+"")
                                                        .addBodyParameter("type",P.getSelectedItem().toString()+"")
                                                        .addBodyParameter("lat",latt)
                                                        .addBodyParameter("longg",longg)

                                                        .setTag("test")
                                                        .setPriority(Priority.MEDIUM)
                                                        .build()
                                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {

                                                                if(response != null)
                                                                    try {

                                                                        progress.dismiss();
                                                                        bd.open();
                                                                        a.setText("");
                                                                        z.setText("");
                                                                        n.setText("");
                                                                        nm.setText("");
                                                                        w.setText("");




                                                                        veri=false;

                                                                        //SHOW RESPONSE FROM SERVER

                                                                        bd.close();
                                                                        ajout.setEnabled(true);

                                                                        //bd();
                                                                        String responseString= response.get(0).toString();
                                                                        get_pvente();


                                                                    } catch (JSONException e) {
                                                                        ajout.setEnabled(true);
                                                                        veri=false;

                                                                        progress.dismiss();
                                                                        e.printStackTrace();
                                                                        Toast.makeText(getContext(), "Changer le nom, il existe un pvente de mm nom ", Toast.LENGTH_SHORT).show();
                                                                    }

                                                            }

                                                            //ERROR
                                                            @Override
                                                            public void onError(ANError anError) {
                                                                progress.dismiss();
                                                                ajout.setEnabled(true);
                                                                veri=false;
                                                                Toast.makeText(getContext(), "Erreur de connexion ou bien Changer le nom, il existe un pvente de mm nom ", Toast.LENGTH_SHORT).show();


                                                                anError.printStackTrace();

                                                            }


                                                        });



                                            }
                                        }
                                    });
                                    if(veri==false){
                            AndroidNetworking.post(DATA_INSERT_URL)
                                    .addBodyParameter("action","add pvente")
                                    .addBodyParameter("branche",b+"")
                                    .addBodyParameter("lieu",a.getText()+"")
                                    .addBodyParameter("zone",z.getText()+"")
                                    .addBodyParameter("nom",n.getText()+"")
                                    .addBodyParameter("num",nm.getText()+"")
                                    .addBodyParameter("wilaya",w.getText()+"")
                                    .addBodyParameter("type",P.getSelectedItem().toString()+"")
                                    .setTag("test")
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                        public void onResponse(JSONArray response) {

                                            if(response != null)
                                                try {

                                                    progress.dismiss();
                                                    bd.open();




                                                    //SHOW RESPONSE FROM SERVER

                                                    bd.close();
                                                    ajout.setEnabled(true);

                                                    //bd();
                                                    String responseString= response.get(0).toString();
                                                    get_pvente();


                                                } catch (JSONException e) {
                                                    ajout.setEnabled(true);

                                                    progress.dismiss();
                                                    e.printStackTrace();
                                                    Toast.makeText(getContext(), "Changer le nom, il existe un pvente de mm nom ", Toast.LENGTH_SHORT).show();
                                                }

                                        }

                                        //ERROR
                                        @Override
                                        public void onError(ANError anError) {
                                            progress.dismiss();
                                            ajout.setEnabled(true);
                                            Toast.makeText(getContext(), "Erreur de connexion ou bien Changer le nom, il existe un pvente de mm nom ", Toast.LENGTH_SHORT).show();

                                            anError.printStackTrace();

                                        }


                                    });}


                        }


                    }
                });*/


    }

    public void bddindex(int index){
        idpv=-1;
        ind=null;




        bd.open();
        Cursor c =bd.getpventes(index);
        int j =c.getCount();

        id_l= new int[j];
        nom= new String[j];
        zone= new String[j];
        num= new String[j];
        adresse= new String[j];
        credit= new String[j];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));

            nom[i] = c.getString(c.getColumnIndex("nom"));
            num[i] = c.getString(c.getColumnIndex("num"))+"";
            adresse[i] = c.getString(c.getColumnIndex("adresse"));
            zone[i] = c.getString(c.getColumnIndex("zone"));
            credit[i] = c.getString(c.getColumnIndex("credit"));




            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(getActivity(), "Magasin est vide ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");

                for (int i=0;i<id_l.length;i++){
                    if(contents.equals(id_l[i]+"")){
                    idp=id_l[i];
                    Intent myIntent = new Intent(getContext(), modpvente.class);

                    myIntent.putExtra("idp", idp+"");
                    myIntent.putExtra("branche", b+"");
                    myIntent.putExtra("zone", zone[i]+"");
                    myIntent.putExtra("adresse", adresse[i]+"");
                    myIntent.putExtra("nom", nom[i]+"");
                    myIntent.putExtra("num", num[i]+"");
                    myIntent.putExtra("type", type[i]+"");

                    myIntent.putExtra("wilaya", wilaya[i]+"");


                    myIntent.putExtra("id", id+"");

                    startActivity(myIntent);


                    }

                }



            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }
    public void get_pvente(){
        progress.show();

        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get pventes")
                .addBodyParameter("branche",b+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {

                                id_l= new int[response.length()];
                                nom= new String[response.length()];
                                zone= new String[response.length()];
                                num= new String[response.length()];
                                adresse= new String[response.length()];
                                credit= new String[response.length()];
                                wilaya= new String[response.length()];
                                type= new String[response.length()];
                                id_livreur= new String[response.length()];




                                bd.open();


                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");
                                    nom[i] = response.getJSONObject(i).getString("nom");
                                    num[i] = response.getJSONObject(i).getString("num");
                                    wilaya[i] = response.getJSONObject(i).getString("wilaya");
                                    type[i] = response.getJSONObject(i).getString("type");
                                   id_livreur[i] = response.getJSONObject(i).getString("id_livreur");

                                    adresse[i] = response.getJSONObject(i).getString("lieu");
                                    zone[i] = response.getJSONObject(i).getString("zone");
                                    if(!response.getJSONObject(i).getString("reste").equals("null"))
                                  credit[i] = response.getJSONObject(i).getString("reste");
                                else
                                credit[i]=0+"";

                               bd.insertpvente(b,id_l[i],nom[i],num[i],adresse[i],wilaya[i],type[i],zone[i],credit[i],id_livreur[i]);
                                }
                                progress.dismiss();
                                ajout();

                                //SHOW RESPONSE FROM SERVER

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
                        id_l= new int[0];
                        nom= new String[0];
                       adresse= new String[0];
                        zone= new String[0];
                        num= new String[0];
                        credit= new String[0];
                        wilaya= new String[0];

                        ajout();
                    }


                });


    }
    public void filt(){

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int k, int i1, int i2) {
                if(charSequence.toString().length()>0) {
                    bddsearch(charSequence.toString());

                    int cpt = 0;
                    int m = -1;
                    ind = new Integer[id_l.length];

                    ArrayList<Item2> exampleList = new ArrayList<>();

                    for (int i = 0; i < id_l.length; i++) {
                            if (cpt < 30) {
                                if (id_l[i] == idp)
                                    m = cpt;
                                ind[cpt] = i;
                                cpt++;


                                exampleList.add(new Item2("CAS" + id + "-" + id_l[i] + "", nom[i], "", credit[i]));
                            }

                    }


                    mRecyclerView = v.findViewById(R.id.recycler3);

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(con);
                    mAdapter = new Adapter2(exampleList);
                    //select(m);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                }
                else{


                    int cpt= Integer.parseInt(center.getText().toString());


                    bddindex((cpt-1)*30);}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    public void bddsearch(String value){



        bd.open();
        Cursor c =bd.getpventessearch(value);
        int j =c.getCount();

        id_l= new int[j];
        nom= new String[j];
        zone= new String[j];
        num= new String[j];
        adresse= new String[j];
        credit= new String[j];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));

            nom[i] = c.getString(c.getColumnIndex("nom"));
            num[i] = c.getString(c.getColumnIndex("num"))+"";
            adresse[i] = c.getString(c.getColumnIndex("adresse"));
            zone[i] = c.getString(c.getColumnIndex("zone"));
            credit[i] = c.getString(c.getColumnIndex("credit"));




            i++;
        }

        bd.close();


    }























    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 9);
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
            Toast.makeText(getContext(), "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }



    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+num[pos]));
            con.startActivity(callIntent);
        }else{
            Toast.makeText(getContext(), "Vous n'avez pas donné la permission.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getidp(){

        return idp;


    }
}