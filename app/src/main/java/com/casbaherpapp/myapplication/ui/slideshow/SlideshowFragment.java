package com.casbaherpapp.myapplication.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.Adapter1;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.Item1;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.MainActivity;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.OrderProducts;
import com.casbaherpapp.myapplication.produits;
import com.casbaherpapp.myapplication.ui.gallery.GalleryFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ProgressDialog progress;
    private Context con;
    private RecyclerView mRecyclerView;
    private Adapter1 mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] quantite,QU,quantite_v,QVU,produit,montant,payement,endomage;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    private  int id=-1,j=0;
    private BDD bd;
    private EditText theFilter;


    private View v;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bd = new BDD(getContext());

        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
       /* final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
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
        id = ((Main)getContext()).getData();
        v=root;

        theFilter=v.findViewById(R.id.searchFilter);
        progress.show();
        verif();
        bd();

        final Button pvvv =v.findViewById(R.id.produits);

        pvvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent myIntent = new Intent(getContext(), produits.class);




                startActivity(myIntent);







            }
        });

        filt();
        progress.dismiss();
        final Button tous_produits = v.findViewById(R.id.commander_produits);
        tous_produits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Remarque")
                        .setMessage("\n" +
                                "Une fois que vous commandez un produit, il n'est pas possible de revenir en arrière ou d'annuler la commande")
                        .setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent myIntent = new Intent(getContext(), OrderProducts.class);
                                startActivity(myIntent);
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        return root;
    }

    public void get_liv(){
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
                                    j=response.length();
                                    int f=Integer.parseInt(response.getJSONObject(i).getString("fardeau"));
                                    produit[i] = response.getJSONObject(i).getString("produit");
                                    quantite[i] = ((int)Float.parseFloat(response.getJSONObject(i).getString("quantite_f")))+"";
                                    int qu=Integer.parseInt(response.getJSONObject(i).getString("quantite_u"));
                                    QU[i] = String.valueOf(qu-(f*Integer.parseInt(quantite[i])));

                                    montant[i] = response.getJSONObject(i).getString("valeur");

                                    bd.InsertStock(id,produit[i],quantite[i],QU[i],montant[i],f);




                                }
                                bd.close();
                                ajout();
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
                        bd();


                    }


                });

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


    public void filt(){

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int k, int i1, int i2) {


                ArrayList<Item1> exampleList = new ArrayList<>();
                double total = 0;
                int q = 0, qu = 0;
                for (int i = 0; i < j; i++) {
                    //exampleList.add(new Item1(quantite[i]+" F,  "+QU[i]+" U", montant[i]+" DA", quantite_v[i]+" F,  "+QVU[i]+" U",payement[i]+" DA",produit[i],"Endomagés : "+endomage[i]+" U"));
                    if(produit[i].toLowerCase().contains(charSequence.toString().toLowerCase())){
                        exampleList.add(new Item1(quantite[i] + " F,  " + QU[i] + " U", "", "", "", produit[i], ""));
                        total = total + Double.parseDouble(montant[i]);
                        q = q + Integer.parseInt(quantite[i]);
                        qu = qu + Integer.parseInt(QU[i]);
                    }
                }
                TextView t = v.findViewById(R.id.total);
                TextView tt = v.findViewById(R.id.totalQ);
                t.setText("");
                tt.setText("QT : " + q + " F,  " + qu + " U");

                mRecyclerView = v.findViewById(R.id.recycler1);

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(con);
                mAdapter = new Adapter1(exampleList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    public void bd(){


        bd.open();

        Cursor c =bd.getmag(id);

        produit= new String[c.getCount()];
        quantite= new String[c.getCount()];
        montant= new String[c.getCount()];
        QU= new String[c.getCount()];



        j=c.getCount();
        int i=0;
        while (c.moveToNext()){


            produit[i] = c.getString(c.getColumnIndex("produit"));
            quantite[i] = c.getString(c.getColumnIndex("quantite_f"))+"";
            montant[i] = c.getString(c.getColumnIndex("valeur"));
            QU[i] = c.getString(c.getColumnIndex("quantite_u"));



            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(getContext(), "Magasin est vide ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }



    }

    public void ajout(){
        ArrayList<Item1> exampleList = new ArrayList<>();
        double total=0; int q=0, qu=0;
        for(int i=0;i<j;i++) {
            //exampleList.add(new Item1(quantite[i]+" F,  "+QU[i]+" U", montant[i]+" DA", quantite_v[i]+" F,  "+QVU[i]+" U",payement[i]+" DA",produit[i],"Endomagés : "+endomage[i]+" U"));
            exampleList.add(new Item1(quantite[i]+" F,  "+QU[i]+" U", "", "","",produit[i],""));
            total= total+Double.parseDouble(montant[i]);
            q=q+Integer.parseInt(quantite[i]);
            qu=qu+Integer.parseInt(QU[i]);

        }
        TextView t = v.findViewById(R.id.total);
        TextView tt = v.findViewById(R.id.totalQ);
        t.setText("");
        tt.setText("QT : "+q+" F,  "+qu+" U");

        mRecyclerView = v.findViewById(R.id.recycler1);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter1(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(            new GalleryFragment.RecyclerItemClickListener(con, new GalleryFragment.RecyclerItemClickListener.OnItemClickListener() {
            public void onItemClick(View v, int position){
              /*  Intent myIntent = new Intent(getActivity(), liv.class);
                myIntent.putExtra("id_l", id_l[position]+"");
                myIntent.putExtra("pvente", pvente[position]+"");
                myIntent.putExtra("quantite", quantite[position]+"");
                myIntent.putExtra("montant", montant_bon[position]+"");
                myIntent.putExtra("produit", produit[position]+"");


                startActivity(myIntent);*/


            }


        }));


        progress.dismiss();
    }
}