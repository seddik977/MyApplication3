package com.casbaherpapp.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class produits extends AppCompatActivity  {

    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int[] id_l;
    private String[] produit,prix_u,prix_f;
    private RecyclerView mRecyclerView;
    private Adapter3 mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String b;
    private EditText theFilter,n,a,z,nm;
    private Integer[] ind;

    private BDD bd;

    private Button premier,dernier,left,right;
    private EditText center;
    private int cptpventes=1;

    public void init(){
        premier = findViewById(R.id.premier);
        dernier = findViewById(R.id.dernier);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        center = findViewById(R.id.center);

        center.setText("1");

        bd.open();

        cptpventes=(bd.getcountp()/30) ;
        if((bd.getcountp()/30)!=((double)bd.getcountp()/30)){
            cptpventes++;
        }
        bd.close();


        premier.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        center.setText("1");
                        theFilter.setText("");

                        bdd1();

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

        center.setInputType(InputType.TYPE_NULL);

        center.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                        final NumberPicker task = new NumberPicker(produits.this);

                        task.setMinValue(1);
                        task.setMaxValue(cptpventes);
                        task.setWrapSelectorWheel(true);
                        AlertDialog dialog = new AlertDialog.Builder(produits.this)
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


        center.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    final NumberPicker task = new NumberPicker(produits.this);

                    task.setMinValue(1);
                    task.setMaxValue(cptpventes);
                    task.setWrapSelectorWheel(true);
                    AlertDialog dialog = new AlertDialog.Builder(produits.this)
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





    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produits);
        bd = new BDD(produits.this);
        progress = new ProgressDialog(produits.this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);


        mRecyclerView = findViewById(R.id.recycler);
        theFilter = (EditText) findViewById(R.id.searchFilter);

       bdd1();
        filt();
init();


    }

    public void bdd1(){

        bd.open();

        Cursor c =bd.getpr();
        int j =c.getCount();
        id_l= new int[j];
        produit= new String[j];
        prix_f= new String[j];
        prix_u= new String[j];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));
            produit[i] = c.getString(c.getColumnIndex("nom"));
            prix_f[i] = c.getString(c.getColumnIndex("prix_f"));
            prix_u[i] = c.getString(c.getColumnIndex("prix_u"));



            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(produits.this, "Pas de produits ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

    }

    public void bddindex(int index){

        bd.open();

        Cursor c =bd.getprindex(index);
        int j =c.getCount();
        id_l= new int[j];
        produit= new String[j];
        prix_f= new String[j];
        prix_u= new String[j];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));
            produit[i] = c.getString(c.getColumnIndex("nom"));
            prix_f[i] = c.getString(c.getColumnIndex("prix_f"));
            prix_u[i] = c.getString(c.getColumnIndex("prix_u"));



            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(produits.this, "Pas de produits ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

    }

    public void bddsearch(String index){

        bd.open();

        Cursor c =bd.getprsearch(index);
        int j =c.getCount();
        id_l= new int[j];
        produit= new String[j];
        prix_f= new String[j];
        prix_u= new String[j];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));
            produit[i] = c.getString(c.getColumnIndex("nom"));
            prix_f[i] = c.getString(c.getColumnIndex("prix_f"));
            prix_u[i] = c.getString(c.getColumnIndex("prix_u"));



            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(produits.this, "Pas de produits ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

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
                    ind = new Integer[id_l.length];

                    ArrayList<Item2> exampleList = new ArrayList<>();
                    exampleList.add(new Item2("", "Produit", "Prix détail", "Prix gros"));

                    for (int i = 0; i < id_l.length; i++) {
                            ind[cpt] = i;
                            cpt++;


                            exampleList.add(new Item2("", produit[i], prix_u[i], prix_f[i]));


                    }


                    mRecyclerView = findViewById(R.id.recycler);

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(produits.this);
                    mAdapter = new Adapter3(exampleList);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                }
                else{
                    int cpt= Integer.parseInt(center.getText().toString());


                    bddindex((cpt-1)*30);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void ajout(){

        int cpt=0,total=0,Q=0,qu=0;
        String q="",p="";
        int m=0;
        String[] pv;
        pv=new String[id_l.length];
        ArrayList<Item2> exampleList = new ArrayList<>();
        exampleList.add(new Item2("","Produit", "Prix détail", "Prix gros"));

        for(int i=0;i<id_l.length;i++) {

            exampleList.add(new Item2("",produit[i],prix_u[i],prix_f[i]));

        }




        mRecyclerView = findViewById(R.id.recycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(produits.this);
        mAdapter = new Adapter3(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




    }






}
