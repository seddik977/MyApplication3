package com.casbaherpapp.myapplication.ui.gventes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.casbaherpapp.myapplication.Adapter;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.Item;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.MainActivity;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.editventes;
import com.casbaherpapp.myapplication.liv;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class gvente extends Fragment {
    private BDD bd;
    private gventeM galleryViewModel;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int[] id_l,bon;
    private String[] pvente,id_pvente,heure,faite,motif,photo,date,valide,payement,reste;
    private String[][] produit,quantite,quantite_u,montant_bon,endomage,QV,QVU,IDP;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context con;
    private View v;

    private FloatingActionButton b,a,g,e;
    private EditText theFilter;
    private Integer[] ind;
    private int idl=-1;
    private String faitee="non";




    private int id=-2,pos;
    private boolean  shouldRefreshOnResume=false;
    public void onResume(){


        super.onResume();
        //update your fragment
if(shouldRefreshOnResume) {
        NavigationView navigationView = ((Main)getContext()).findViewById(R.id.nav_view);
        navigationView.getMenu().performIdentifierAction(R.id.nav_gventes,1);}


    }

    public void enter(){
        if(Main.getCreditt()==-1){
            TextView textView = getActivity().findViewById(R.id.searchFilter);
            textView.setText("");
            Main.setCreditt(0);
            Intent myIntent = new Intent(getActivity(), liv.class);
            List<String> pr= new ArrayList<>() ;
            List<String> q= new ArrayList<>() ;
            List<String> qu= new ArrayList<>() ;
            List<String> mon= new ArrayList<>() ;
            List<String> qv= new ArrayList<>() ;
            List<String> quv= new ArrayList<>() ;
            List<String> pay= new ArrayList<>() ;
            List<String> end= new ArrayList<>() ;
            List<String> idp= new ArrayList<>() ;
            List<String> rest= new ArrayList<>() ;
            List<String> b= new ArrayList<>() ;

            int position=0;



            for(int i=0;i<50;i++) {

                if (produit[position][i] != null)
                    pr.add(produit[position][i]);
                if (quantite[position][i] != null)
                    q.add(quantite[position][i]);
                if (quantite_u[position][i] != null)
                    qu.add(quantite_u[position][i]);
                if (montant_bon[position][i] != null)
                    mon.add(montant_bon[position][i]);
                if (QV[position][i] != null)
                    qv.add(QV[position][i]);
                if (QVU[position][i] != null)
                    quv.add(QVU[position][i]);

                if (endomage[position][i] != null)
                    end.add(endomage[position][i]);
                if (IDP[position][i] != null)
                    idp.add(IDP[position][i]);


            }
            myIntent.putExtra("id_l", id_l[position]+"");
            myIntent.putExtra("bon", bon[position]+"");

            myIntent.putExtra("idlivreur", id+"");

            myIntent.putExtra("pvente", pvente[position]+"");
            myIntent.putExtra("reste", reste[position]+"");
            myIntent.putExtra("payement", payement[position]+"");


            myIntent.putStringArrayListExtra("produit", (ArrayList<String>) pr);

            myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idp);


            myIntent.putStringArrayListExtra("quantite", (ArrayList<String>)q);
            myIntent.putStringArrayListExtra("quantite_u", (ArrayList<String>)qu);

            myIntent.putStringArrayListExtra("montant", (ArrayList<String>) mon);
            myIntent.putStringArrayListExtra("quantite_v", (ArrayList<String>) qv);
            myIntent.putStringArrayListExtra("quantite_u_v", (ArrayList<String>) quv);

            myIntent.putExtra("motif", motif[position]+"");

            myIntent.putStringArrayListExtra("endomage", (ArrayList<String>) end);

            Bitmap decodedByte=null;
            String c="";
            if(photo[position]!=null){
                if(!photo[position].equals("") && !photo[position].toLowerCase().equals("null")){                    //String s = photo[position].replace("data:image/png;base64,","");

                    byte[] decodedString = Base64.decode(photo[position], Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    c="cc";
                }}
            if(decodedByte!=null)
                saveFile(getContext(),decodedByte,"myImage.jpg");

            myIntent.putExtra("photo", c+"");


            idl=-1;
            faitee="non";

            startActivity(myIntent);
        }
        else if(Main.getidCreditt()>0){
            TextView textView = getActivity().findViewById(R.id.searchFilter);
            textView.setText("");

            Intent myIntent = new Intent(getActivity(), liv.class);
            List<String> pr= new ArrayList<>() ;
            List<String> q= new ArrayList<>() ;
            List<String> qu= new ArrayList<>() ;
            List<String> mon= new ArrayList<>() ;
            List<String> qv= new ArrayList<>() ;
            List<String> quv= new ArrayList<>() ;
            List<String> pay= new ArrayList<>() ;
            List<String> end= new ArrayList<>() ;
            List<String> idp= new ArrayList<>() ;
            List<String> rest= new ArrayList<>() ;
            List<String> b= new ArrayList<>() ;

            int position=0;
for(int j=0;j<id_l.length;j++){
    if(id_l[j]==Main.getidCreditt())
        position=j;
}
            Main.setidCreditt(0);



            for(int i=0;i<50;i++) {

                if (produit[position][i] != null)
                    pr.add(produit[position][i]);
                if (quantite[position][i] != null)
                    q.add(quantite[position][i]);
                if (quantite_u[position][i] != null)
                    qu.add(quantite_u[position][i]);
                if (montant_bon[position][i] != null)
                    mon.add(montant_bon[position][i]);
                if (QV[position][i] != null)
                    qv.add(QV[position][i]);
                if (QVU[position][i] != null)
                    quv.add(QVU[position][i]);

                if (endomage[position][i] != null)
                    end.add(endomage[position][i]);
                if (IDP[position][i] != null)
                    idp.add(IDP[position][i]);


            }
            myIntent.putExtra("id_l", id_l[position]+"");
            myIntent.putExtra("bon", bon[position]+"");

            myIntent.putExtra("idlivreur", id+"");

            myIntent.putExtra("pvente", pvente[position]+"");
            myIntent.putExtra("reste", reste[position]+"");
            myIntent.putExtra("payement", payement[position]+"");


            myIntent.putStringArrayListExtra("produit", (ArrayList<String>) pr);

            myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idp);


            myIntent.putStringArrayListExtra("quantite", (ArrayList<String>)q);
            myIntent.putStringArrayListExtra("quantite_u", (ArrayList<String>)qu);

            myIntent.putStringArrayListExtra("montant", (ArrayList<String>) mon);
            myIntent.putStringArrayListExtra("quantite_v", (ArrayList<String>) qv);
            myIntent.putStringArrayListExtra("quantite_u_v", (ArrayList<String>) quv);

            myIntent.putExtra("motif", motif[position]+"");
            myIntent.putStringArrayListExtra("endomage", (ArrayList<String>) end);

            Bitmap decodedByte=null;
            String c="";
            if(photo[position]!=null){
                if(!photo[position].equals("") && !photo[position].toLowerCase().equals("null")){                    //String s = photo[position].replace("data:image/png;base64,","");

                    byte[] decodedString = Base64.decode(photo[position], Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    c="cc";
                }}
            if(decodedByte!=null)
                saveFile(getContext(),decodedByte,"myImage.jpg");

            myIntent.putExtra("photo", c+"");


            idl=-1;
            faitee="non";

            startActivity(myIntent);


        }
    }
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(gventeM.class);
        final View root = inflater.inflate(R.layout.fragment_gventes, container, false);
        progress = new ProgressDialog(getContext());
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);
        FloatingActionButton fab = ((Main)getContext()).findViewById(R.id.fab);
        fab.hide();
        bd = new BDD(getContext());

       /* final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        con=root.getContext();
        v=root;

        mRecyclerView = root.findViewById(R.id.recycler);
        theFilter = (EditText) root.findViewById(R.id.searchFilter);
        id = ((Main)getContext()).getData();

        progress.show();
        verif();
        bd();
        filt();
        onclick();


        return root;
    }
    public void filt(){

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int k, int i1, int i2) {

                int cpt=0,pr=-1,cp=0;
                String[] pv;
                String q="";
                int p=0;
                Double m=0.0;
                ind=new Integer[id_l.length];
                ArrayList<Item> exampleList = new ArrayList<>();
                for(int i=0;i<id_l.length;i++) {
                    if(pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase()) || id_pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase())) {

                        ind[cpt]=i;

                        cpt++;
                        if(id_l[i]==idl){
                            pr=cp;}
                        cp++;
                    for(int j=0;j<produit[i].length;j++) {


                        if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                            q=q+quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                            p=p+1;}
                        if(montant_bon[i][j]!=null){
                            m=m+Double.parseDouble(montant_bon[i][j]);}

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
                    }
                        exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
                    q="";
                    p=0;
                    m=0.0;

                }}



                mRecyclerView = v.findViewById(R.id.recycler);

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(con);
                mAdapter = new Adapter(exampleList);
                mAdapter.setV();

                mAdapter.setSelectedItem(pr);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void onclick(){
        FloatingActionButton add= v.findViewById(R.id.ajout);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), editventes.class);
                myIntent.putExtra("id", id+"");
                myIntent.putExtra("branche",  ((Main)getContext()).getB()+"");



                startActivity(myIntent);


            }
        });


        a= v.findViewById(R.id.supprimer);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                a.setEnabled(false);
                if(idl==-1){
                    Toast.makeText(getContext(), "Selectionner une livraison", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    a.setEnabled(true);

                }
                else if(faitee.equals("non")){
                    supprimer();
                }
                else if(faitee.equals("oui")){
                    Toast.makeText(getContext(), "Annuler le bon avant de le supprimer", Toast.LENGTH_SHORT).show();

                }

            }
        });
        e= v.findViewById(R.id.entrer);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                e.setEnabled(false);
                if(idl==-1){
                    Toast.makeText(getContext(), "Selectionner une livraison", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    e.setEnabled(true);

                }
                else{
                    progress.dismiss();
                    e.setEnabled(true);
                    Intent myIntent = new Intent(getActivity(), liv.class);
                    List<String> pr= new ArrayList<>() ;
                    List<String> q= new ArrayList<>() ;
                    List<String> qu= new ArrayList<>() ;
                    List<String> mon= new ArrayList<>() ;
                    List<String> qv= new ArrayList<>() ;
                    List<String> quv= new ArrayList<>() ;
                    List<String> pay= new ArrayList<>() ;
                    List<String> end= new ArrayList<>() ;
                    List<String> idp= new ArrayList<>() ;
                    List<String> rest= new ArrayList<>() ;
                    List<String> b= new ArrayList<>() ;
                   int position=pos;
                    if(ind!=null)
                        position=ind[position];



                    for(int i=0;i<50;i++) {

                        if (produit[position][i] != null)
                            pr.add(produit[position][i]);
                        if (quantite[position][i] != null)
                            q.add(quantite[position][i]);
                        if (quantite_u[position][i] != null)
                            qu.add(quantite_u[position][i]);
                        if (montant_bon[position][i] != null)
                            mon.add(montant_bon[position][i]);
                        if (QV[position][i] != null)
                            qv.add(QV[position][i]);
                        if (QVU[position][i] != null)
                            quv.add(QVU[position][i]);

                        if (endomage[position][i] != null)
                            end.add(endomage[position][i]);
                        if (IDP[position][i] != null)
                            idp.add(IDP[position][i]);


                    }
                    myIntent.putExtra("id_l", id_l[position]+"");
                    myIntent.putExtra("bon", bon[position]+"");

                    myIntent.putExtra("idlivreur", id+"");

                    myIntent.putExtra("pvente", pvente[position]+"");
                    myIntent.putExtra("reste", reste[position]+"");
                    myIntent.putExtra("payement", payement[position]+"");


                    myIntent.putStringArrayListExtra("produit", (ArrayList<String>) pr);

                    myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idp);


                    myIntent.putStringArrayListExtra("quantite", (ArrayList<String>)q);
                    myIntent.putStringArrayListExtra("quantite_u", (ArrayList<String>)qu);

                    myIntent.putStringArrayListExtra("montant", (ArrayList<String>) mon);
                    myIntent.putStringArrayListExtra("quantite_v", (ArrayList<String>) qv);
                    myIntent.putStringArrayListExtra("quantite_u_v", (ArrayList<String>) quv);

                    myIntent.putExtra("motif", motif[position]+"");
                    myIntent.putStringArrayListExtra("endomage", (ArrayList<String>) end);
                    Bitmap decodedByte=null;
                    String c="";
                    if(photo[position]!=null){
                        if(!photo[position].equals("") && !photo[position].toLowerCase().equals("null")){                        //String s = photo[position].replace("data:image/png;base64,","");

                        byte[] decodedString = Base64.decode(photo[position], Base64.DEFAULT);
                         decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
c="cc";
                    }}
                    if(decodedByte!=null)
                        saveFile(getContext(),decodedByte,"myImage.jpg");

                    myIntent.putExtra("photo", c+"");


                   idl=-1;
                    faitee="non";

                    startActivity(myIntent);
                }

            }
        });

        mRecyclerView.addOnItemTouchListener(            new RecyclerItemClickListener(con, new RecyclerItemClickListener.OnItemClickListener() {
            public void onItemClick(View v, int position){
                if(ind!=null)
                    position=ind[position];

                idl=id_l[position];
                faitee=faite[position];
                pos=position;
                select(pos);
               /*
                Intent myIntent = new Intent(getActivity(), liv.class);
                List<String> pr= new ArrayList<>() ;
                List<String> q= new ArrayList<>() ;
                List<String> qu= new ArrayList<>() ;
                List<String> mon= new ArrayList<>() ;
                List<String> qv= new ArrayList<>() ;
                List<String> quv= new ArrayList<>() ;
                List<String> pay= new ArrayList<>() ;
                List<String> end= new ArrayList<>() ;
                List<String> idp= new ArrayList<>() ;


                for(int i=0;i<50;i++) {
                    if (produit[position][i] != null)
                        pr.add(produit[position][i]);
                    if (quantite[position][i] != null)
                        q.add(quantite[position][i]);
                    if (quantite_u[position][i] != null)
                        qu.add(quantite_u[position][i]);
                    if (montant_bon[position][i] != null)
                        mon.add(montant_bon[position][i]);
                    if (QV[position][i] != null)
                        qv.add(QV[position][i]);
                    if (QVU[position][i] != null)
                        quv.add(QVU[position][i]);
                    if (payement[position][i] != null)
                        pay.add(payement[position][i]);
                    if (endomage[position][i] != null)
                        end.add(endomage[position][i]);
                    if (IDP[position][i] != null)
                        idp.add(IDP[position][i]);

                }
                myIntent.putExtra("id_l", id_l[position]+"");
                myIntent.putExtra("idlivreur", id+"");

                myIntent.putExtra("pvente", pvente[position]+"");



                myIntent.putStringArrayListExtra("produit", (ArrayList<String>) pr);

                myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idp);


                myIntent.putStringArrayListExtra("quantite", (ArrayList<String>)q);
                myIntent.putStringArrayListExtra("quantite_u", (ArrayList<String>)qu);

                myIntent.putStringArrayListExtra("montant", (ArrayList<String>) mon);
                myIntent.putStringArrayListExtra("quantite_v", (ArrayList<String>) qv);
                myIntent.putStringArrayListExtra("quantite_u_v", (ArrayList<String>) quv);

                myIntent.putExtra("motif", motif[position]+"");
                myIntent.putStringArrayListExtra("payement", (ArrayList<String>) pay);
                myIntent.putStringArrayListExtra("endomage", (ArrayList<String>) end);




                startActivity(myIntent);

                */


            }


        }));

    }
    public static void saveFile(Context context, Bitmap b, String picName){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "io exception");
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                                    Log.e( "BDDBER",e.toString());

                                    bd.open();
                                    bd.dec();
                                    bd.close();
                                    Intent intent1 = new Intent(v.getContext(),MainActivity.class);
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





    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector mGestureDetector;
        private OnItemClickListener mListener;
        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    public void get_liv(){
        progress.show();
        final int[] x = {0};
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get livraisonsU")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                produit= new String[response.length()][50];
                                quantite= new String[response.length()][50];
                                quantite_u= new String[response.length()][50];
                                IDP= new String[response.length()][50];
                                reste= new String[response.length()];

                                montant_bon= new String[response.length()][50];
                                payement= new String[response.length()];
                                endomage= new String[response.length()][50];
                                QV= new String[response.length()][50];
                                QVU= new String[response.length()][50];
                                id_l= new int[response.length()];
                                bon= new int[response.length()];
                                pvente= new String[response.length()];
                                id_pvente= new String[response.length()];

                                heure= new String[response.length()];
                                faite= new String[response.length()];
                                motif= new String[response.length()];
                                photo= new String[response.length()];

                                date= new String[response.length()];
                                valide= new String[response.length()];



                                bd.open();

                                //SHOW RESPONSE FROM SERVER
                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");
                                    bon[i] = response.getJSONObject(i).getInt("bon");

                                    pvente[i] = response.getJSONObject(i).getString("pvente");
                                    id_pvente[i] = response.getJSONObject(i).getString("id_pvente");

                                    heure[i] = response.getJSONObject(i).getString("heure");
                                    faite[i] = response.getJSONObject(i).getString("faite");

                                    motif[i] = response.getJSONObject(i).getString("motif");
                                    photo[i] = response.getJSONObject(i).getString("photo");

                                    date[i] = response.getJSONObject(i).getString("date");
                                    valide[i] = response.getJSONObject(i).getString("valide");
                                    payement[i] = response.getJSONObject(i).getString("payement");
                                    reste[i] = response.getJSONObject(i).getString("reste");




                                    // bd.InsertLiv(id,id_l[i],pvente[i],produit[i],quantite[i],quantite_u[i],montant_bon[i],heure[i],faite[i],endomage[i],payement[i],motif[i],QV[i],QVU[i],date[i],valide[i],date_m);
                                    final int l=i;
                                    AndroidNetworking.post(DATA_INSERT_URL)
                                            .addBodyParameter("action","get produits")
                                            .addBodyParameter("id",id_l[i]+"")

                                            .setTag("test")
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                    if(response != null)
                                                        try {





                                                            //SHOW RESPONSE FROM SERVER
                                                            for(int j=0;j<response.length();j++) {

                                                                IDP[l][j]=response.getJSONObject(j).getString("id");;

                                                                produit[l][j]=response.getJSONObject(j).getString("produit");;
                                                                quantite[l][j]=response.getJSONObject(j).getString("quantite");;
                                                                quantite_u[l][j]=response.getJSONObject(j).getString("quantite_u");;

                                                                montant_bon[l][j]=response.getJSONObject(j).getString("montant_bon");;
                                                                endomage[l][j]=response.getJSONObject(j).getString("endomage");;
                                                                QV[l][j]=response.getJSONObject(j).getString("quantite_v");;
                                                                QVU[l][j]=response.getJSONObject(j).getString("quantite_u_v");;



                                                            }
                                                            ajout();


                                                            // String responseString= response.get(0).toString();


                                                        } catch (JSONException e) {


                                                            e.printStackTrace();
                                                            Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        bd();
                        progress.dismiss();
                    }


                });


    }
    public void bd(){

        progress.dismiss();
        bd.open();

        Cursor c =bd.getLiva(id);
        id_l= new int[c.getCount()];
        bon= new int[c.getCount()];
        pvente= new String[c.getCount()];
        id_pvente= new String[c.getCount()];

        heure= new String[c.getCount()];
        faite= new String[c.getCount()];
        motif= new String[c.getCount()];
        photo= new String[c.getCount()];

        payement= new String[c.getCount()];
        reste= new String[c.getCount()];

        int count=c.getCount();

        produit= new String[count][50];
        quantite= new String[count][50];
        quantite_u= new String[count][50];
        IDP= new String[count][50];

        montant_bon= new String[count][50];
        endomage= new String[count][50];
        QV= new String[count][50];
        QVU= new String[count][50];



        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));

            pvente[i] =c.getString(c.getColumnIndex("pvente"));
            id_pvente[i] =c.getString(c.getColumnIndex("id_pvente"));

            bon[i]=c.getInt(c.getColumnIndex("bon"));
            heure[i] =c.getString(c.getColumnIndex("heure"));
            faite[i] = c.getString(c.getColumnIndex("faite"));
            motif[i] = c.getString(c.getColumnIndex("motif"));
            photo[i] = c.getString(c.getColumnIndex("photo"));

            reste[i] = c.getString(c.getColumnIndex("reste"));
            payement[i] = c.getString(c.getColumnIndex("payement"));


                Cursor cc = bd.getproduitv(id_l[i]);
                int j=0;
                while (cc.moveToNext()){

                    IDP[i][j]=cc.getInt(cc.getColumnIndex("id"))+"";

                    produit[i][j]=cc.getString(cc.getColumnIndex("produit"));
                    quantite[i][j]=cc.getString(cc.getColumnIndex("quantite"));
                    quantite_u[i][j]=cc.getString(cc.getColumnIndex("quantite_u"));

                    montant_bon[i][j]=cc.getString(cc.getColumnIndex("montant_bon"));
                    endomage[i][j]=cc.getString(cc.getColumnIndex("endomage"));
                    QV[i][j]=cc.getString(cc.getColumnIndex("quantite_v"));
                    QVU[i][j]=cc.getString(cc.getColumnIndex("quantite_u_v"));
                    j++;

            }





            i++;
        }
        ajout();


        bd.close();
        if(i!=0){

        }
        else {
            Toast.makeText(getContext(), "Pas de ventes", Toast.LENGTH_SHORT).show();
        }



    }
   /* public void bd(){

        progress.dismiss();
        bd.open();

        Cursor c =bd.getLiv(id);
        id_l= new int[c.getCount()];
        pvente= new String[c.getCount()];
        produit= new String[c.getCount()];
        quantite= new String[c.getCount()];
        quantite_u= new String[c.getCount()];

        montant_bon= new String[c.getCount()];
        heure= new String[c.getCount()];
        faite= new String[c.getCount()];
        motif= new String[c.getCount()];
        payement= new String[c.getCount()];
        endomage= new String[c.getCount()];
        QV= new String[c.getCount()];
        QVU= new String[c.getCount()];


        int i=0;
        while (c.moveToNext()){

            id_l[i] = c.getInt(c.getColumnIndex("id"));

            pvente[i] =c.getString(c.getColumnIndex("pvente"));
            produit[i] = c.getString(c.getColumnIndex("produit"));
            quantite[i] = c.getString(c.getColumnIndex("quantite"));
            quantite_u[i] = c.getString(c.getColumnIndex("quantite_u"));
            montant_bon[i] = c.getString(c.getColumnIndex("montant_bon"));
            heure[i] =c.getString(c.getColumnIndex("heure"));
            faite[i] = c.getString(c.getColumnIndex("faite"));
            endomage[i] = c.getString(c.getColumnIndex("endomage"));
            payement[i] = c.getString(c.getColumnIndex("payement"));
            motif[i] = c.getString(c.getColumnIndex("motif"));
            QV[i] = c.getString(c.getColumnIndex("quantite_v"));
            QVU[i] = c.getString(c.getColumnIndex("quantite_u_v"));



            i++;
        }

        bd.close();
        if(i!=0){
            ajout();
        }
        else {
            Toast.makeText(getContext(), "Il y a pas de livraisons où erreur de connexion"+c.getCount(), Toast.LENGTH_SHORT).show();
        }



    }*/


   public void supprimer(){




           new AlertDialog.Builder(v.getContext())
                   .setMessage("Etes vous sur de supprimer la vente ?")
                   .setCancelable(false)
                   .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           bd.open();
                           bd.supprimer(idl);
                           bd.close();


                                a.setClickable(true);
                           progress.dismiss();
                           idl=-1;
                           faitee="non";


                           NavigationView navigationView = ((Main)getContext()).findViewById(R.id.nav_view);
                           navigationView.getMenu().performIdentifierAction(R.id.nav_gventes,1);
                       }
                   })
                   .setNegativeButton("Non", null)
                   .show();



















   }


    public void ajout(){
        int cpt=0;
        String[] pv;
        String q="";
        int p=0;
        Double m=0.0;


        ArrayList<Item> exampleList = new ArrayList<>();
        for(int i=0;i<id_l.length;i++) {


            for(int j=0;j<produit[i].length;j++) {


                if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                    q=q+quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                    p=p+1;}
                if(montant_bon[i][j]!=null){
                    m=m+Double.parseDouble(montant_bon[i][j]);}

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
            }
            exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
            q="";
            p=0;
            m=0.0;

        }

        enter();

        mRecyclerView = v.findViewById(R.id.recycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter(exampleList);
        mAdapter.setV();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




    }

    public void select(int n){
        Parcelable recylerViewState = null;
        if(mRecyclerView.getLayoutManager()!=null){
            recylerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        }
        String charSequence=theFilter.getText().toString();
        int cpt=0;
        String[] pv;
        String q="";
        int p=0;
        Double m=0.0;


        ArrayList<Item> exampleList = new ArrayList<>();
        for(int i=0;i<id_l.length;i++) {
            if(pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase())){



                for(int j=0;j<produit[i].length;j++) {


                if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                    q=q+quantite[i][j] + " F,  " + quantite_u[i][j] + " U,";
                    p=p+1;}
                if(montant_bon[i][j]!=null){
                    m=m+Double.parseDouble(montant_bon[i][j]);}

          /*  else{
                for(int j=0;j<pv.length;j++){
                    if(pvente[i].equals(pv[j])){
                        cpt++;
                    }

                }}*/
            }
                exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
            q="";
            p=0;
            m=0.0;

        }}



        mRecyclerView = v.findViewById(R.id.recycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter(exampleList);
        mAdapter.setV();
        mAdapter.setSelectedItem(n);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(recylerViewState);




    }





}