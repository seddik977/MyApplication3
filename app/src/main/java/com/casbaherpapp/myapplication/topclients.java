package com.casbaherpapp.myapplication;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class topclients extends AppCompatActivity  {

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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topclients);
        bd = new BDD(topclients.this);
        progress = new ProgressDialog(topclients.this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);


        mRecyclerView = findViewById(R.id.recycler);
        theFilter = (EditText) findViewById(R.id.searchFilter);

       get_livv();
       filt();



    }

    public void bdd1(){

        bd.open();

        Cursor c =bd.gettop();
        int j =c.getCount();
        id_l= new int[j];
        produit= new String[j];
        prix_f= new String[j];
        prix_u= new String[j];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));
            produit[i] = c.getString(c.getColumnIndex("nom"));
            prix_f[i] = c.getString(c.getColumnIndex("somme"));
            prix_u[i] = "";



            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(topclients.this, "Pas de produits ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

    }
    public void filt(){

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int k, int i1, int i2) {


                int cpt=0;
                ind= new Integer[id_l.length];

                ArrayList<Item2> exampleList = new ArrayList<>();
                exampleList.add(new Item2("","ID_CLIENT", "NOM", "SOMME"));

                for(int i=0;i<id_l.length;i++) {
                    if(produit[i].toLowerCase().contains(charSequence.toString().toLowerCase()) || (id_l[i]+"").toLowerCase().contains(charSequence.toString().toLowerCase())){
                        ind[cpt]=i;
                        cpt++;


                        exampleList.add(new Item2("",id_l[i]+"",produit[i],prix_f[i]));

                    }
                }



                mRecyclerView = findViewById(R.id.recycler);

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(topclients.this);
                mAdapter = new Adapter3(exampleList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);


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
        exampleList.add(new Item2("","ID_CLIENT", "NOM", "SOMME"));

        for(int i=0;i<id_l.length;i++) {

            exampleList.add(new Item2("",id_l[i]+"",produit[i],prix_f[i]));

        }




        mRecyclerView = findViewById(R.id.recycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(topclients.this);
        mAdapter = new Adapter3(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




    }


    public void get_livv(){
        progress.show();
        final int[] x = {0};
        bd.open();
        int id=bd.getID();
        bd.close();
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get top")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                id_l= new int[response.length()];
                                produit= new String[response.length()];
                                prix_f= new String[response.length()];
                                prix_u= new String[response.length()];



                                bd.open();

                                //SHOW RESPONSE FROM SERVER
                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");

                                    produit[i] = response.getJSONObject(i).getString("nom");
                                    prix_f[i] = response.getJSONObject(i).getString("somme");
                                    prix_u[i] = "";


                                }
                                progress.dismiss();
                                bd.close();
                                ajout();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(topclients.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(topclients.this, "", Toast.LENGTH_SHORT).show();

                        id_l= new int[0];
                        produit= new String[0];
                        prix_f= new String[0];
                        prix_u= new String[0];


                        ajout();
                    }


                });


    }





}
