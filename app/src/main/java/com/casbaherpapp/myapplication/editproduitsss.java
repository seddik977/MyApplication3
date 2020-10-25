package com.casbaherpapp.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.ui.gventes.gvente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class editproduitsss extends AppCompatActivity  {

    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int[] id_l,id_l1;
    private String[] produit,prix_u,prix_uu,prix_f,code_bar,fardeau;
    private RecyclerView mRecyclerView1;
    private Adapter2 mAdapter;
    private int codebar=0;
    private Adapter3 mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] nom,num,adresse,zone,ID,credit,montant,quantite,quantite_u,endomage,QV,QVU,produits,IDP,quantite_f,quantite_u_d;
private ArrayList<String> idpp;
    private String b,payement,reste;
    private EditText theFilter1,n,a,z,nm;
    private Integer[] ind,ind1,select,selected;
    private int positionindex=-1;
    private BDD bd;
    private int id=-1,id_ll=-1,yaw=0,idp,idpv=-1,posp,pospv,s=0,id_livraison,bon,idselect,bonn=1;
    private String pvente="";
    private FloatingActionButton f;
public void onBackPressed(){
    if(s==0){
        Toast.makeText(editproduitsss.this, "Choisissez au moins un produit", Toast.LENGTH_SHORT).show();


    }
    else{
        editproduitsss.super.onBackPressed();
    }


}


    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.editproduits);
        bd = new BDD(editproduitsss.this);
        progress = new ProgressDialog(editproduitsss.this);
        progress.setTitle("Chargement");
        progress.setMessage("Attendez SVP...");
        progress.setCancelable(false);


        mRecyclerView1 = findViewById(R.id.recycler1);
        theFilter1 = (EditText) findViewById(R.id.searchFilter1);


        Intent i = getIntent();

        Bundle extras = i.getExtras();
        if(extras.containsKey("id")) {
            id = Integer.parseInt(i.getStringExtra("id"));


        }
        if(extras.containsKey("id_l")) {
            id_ll = Integer.parseInt(i.getStringExtra("id_l"));


        }
         idpp = null;
        if (extras.containsKey("idp")) {
           idpp = i.getStringArrayListExtra("idp");
        }



        select=new Integer[50];
        selected=new Integer[50];
        montant=new String[50];
        quantite=new String[50];
        quantite_u=new String[50];
        QV=new String[50];
        QVU=new String[50];
        endomage=new String[50];
        produits=new String[50];
        IDP= new String[50];



        bdd1();
        filt1();

        onclick();
        b();
        bd.open();
        idpv=bd.getidpvente(id_ll);
bd.close();


    }

   /* public void total(){
        double total=0;
        for (int i=0;i<s;i++){

            total=total+Double.parseDouble(montant[i]);


        }
     TextView t=findViewById(R.id.totalp);
        t.setText(total+" DA");

    }*/


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if(codebar==1){
                    codebar=0;
                for(int i=0;i<id_l1.length;i++){
                    Log.e("CODE",contents+" "+code_bar[i]);
             if(contents.equals(code_bar[i])){
                    final int m=i;
                    if(ind1!=null)


                    idp=id_l1[i];
                    posp=i;
                    final int posi=i;
                    if(! vselect(id_l1[i])) {
                        final NumberPicker task = new NumberPicker(editproduitsss.this);

                        //Set TextView text color
                        Context context = editproduitsss.this;
                        LinearLayout layout = new LinearLayout(context);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        task.setMinValue(0);
                        task.setMaxValue(Integer.parseInt(quantite_f[i]));
                        task.setWrapSelectorWheel(true);
                        final NumberPicker taskk = new NumberPicker(editproduitsss.this);

                        //Set TextView text color

                        taskk.setMinValue(0);
                        taskk.setMaxValue(Integer.parseInt(quantite_u_d[i]));
                        taskk.setWrapSelectorWheel(true);

                        TextView t= new TextView(editproduitsss.this);
                        t.setText("Quantité à vendre en unité");
                        TextView tt= new TextView(editproduitsss.this);
                        tt.setText("Quantité à vendre en fardeau");
                        layout.addView(tt);
                        layout.addView(task);
                        layout.addView(t);
                        layout.addView(taskk);
                        AlertDialog dialog = new AlertDialog.Builder(editproduitsss.this)
                                .setTitle(""+produit[i])
                                .setMessage("")
                                .setView(layout)

                                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String quan = String.valueOf(task.getValue());
                                        String quant = String.valueOf(taskk.getValue());

                                        if((Integer.parseInt(quan)+Integer.parseInt(quant)!=0 )&& !quan.isEmpty()  && !quant.isEmpty()){

                                            select[s] = id_l1[posi];
                                            bd.open();
                                            String type=bd.gettype(idpv);
                                            Double promotion = bd.getpromotion(id_l1[posi],type);
                                            if(type.equals("Grossiste"))
                                            montant[s]=(Double.parseDouble(prix_f[posi])-promotion)*((Integer.parseInt(quan))*Integer.parseInt(fardeau[posi])+(Integer.parseInt(quant)))+"";
                                            else
                                                montant[s]=(Double.parseDouble(prix_uu[posi])-promotion)*((Integer.parseInt(quan))*Integer.parseInt(fardeau[posi])+(Integer.parseInt(quant)))+"";
                                            bd.close();

                                            produits[s]=produit[posi];
                                            selected[s]=m;

                                            quantite[s]=quan;

                                            QV[s]=quan;
                                            quantite_u[s]=quant;
                                            QVU[s]="0";
                                            endomage[s]="0";

                                            reste="0";
                                            payement="0";
                                            select1(posi);
                                            Log.e("S="+s,produits[s]);


                                            s++;
                                            //total();



                                        }
                                        else{

                                        }


                                    }
                                })
                                .setNegativeButton("Annuler", null)
                                .create();
                        dialog.show();



                    }
                    else{




                    }}}
                }

else{
                    for (int i = 0; i < id_l.length; i++) {
                        if (contents.equals(id_l[i] + "")) {


                            idpv = id_l[i];
                            pospv = i;


                            TextView t = findViewById(R.id.pvente);
                            t.setText(nom[i]);
                            pvente = nom[i];




                        }

                    }

                }



            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }
    public void b(){



        ImageButton bbbx = findViewById(R.id.codebar);
        bbbx.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
codebar=1;
                        theFilter1 = (EditText) findViewById(R.id.searchFilter1);
                        theFilter1.setText("");

                        try {

                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "PRODUCT_MODE"); // "PRODUCT_MODE for bar codes

                            startActivityForResult(intent, 0);

                        } catch (Exception e) {

                            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                            startActivity(marketIntent);

                        }




                    }
                });






        f= findViewById(R.id.ajout);

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s==0){
                    Toast.makeText(editproduitsss.this, "Choisissez au moins un produit", Toast.LENGTH_SHORT).show();


                }
                else{
                    progress.show();

                    int id_liv=1,idp=1;
                    bd.open();
                    Cursor c=bd.getbonliv(id);

                    while (c.moveToNext()){

                        bonn=c.getInt(c.getColumnIndex("bon"))+1;
                    }
                    bd.close();

                    dd();

             /*       AndroidNetworking.post(DATA_INSERT_URL)
                            .addBodyParameter("action","add livraisonsP")
                            .addBodyParameter("id_livreur",id+"")
                            .addBodyParameter("id_pvente",idpv+"")
                            .addBodyParameter("pvente",pvente)
                            .addBodyParameter("bon",bonn+"")
                            .addBodyParameter("user","oui")
                            .setTag("test")
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    if(response != null)
                                        try {









                                            bd.open();

                                            id_livraison = Integer.parseInt(response.getJSONObject(0).getString("id"));
                                            bon = Integer.parseInt(response.getJSONObject(0).getString("bon"));
                                            String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                            String heure=new SimpleDateFormat("HH:mm").format(new Date());
                                            bd.open();
                                            bd.InsertLiva(id,id_livraison,idpv,bonn,pvente,heure,"non","",date,"non","","oui");
                                            bd.close();
                                            //SHOW RESPONSE FROM SERVER
                                            for(int i=0;i<s;i++) {

                                             Log.e("i",i+"");

                                                // bd.InsertLiv(id,id_l[i],pvente[i],produit[i],quantite[i],quantite_u[i],montant_bon[i],heure[i],faite[i],endomage[i],payement[i],motif[i],QV[i],QVU[i],date[i],valide[i],date_m);
                                                final int l=i;
                                                AndroidNetworking.post(DATA_INSERT_URL)
                                                        .addBodyParameter("action","add produits")
                                                        .addBodyParameter("id_livreur",id+"")
                                                        .addBodyParameter("id_livraison",id_livraison+"")
                                                        .addBodyParameter("id_pvente",idpv+"")
                                                        .addBodyParameter("quantite",quantite[i]+"")
                                                        .addBodyParameter("quantite_u",quantite_u[i]+"")
                                                        .addBodyParameter("produit",produits[i])
                                                        .addBodyParameter("montant",montant[i])
                                                        .setTag("test")
                                                        .setPriority(Priority.HIGH)
                                                        .build()
                                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {

                                                                if(response != null)
                                                                    try {

                                                                       bd.open();
                                                                       Log.e("CCCCCCCcc", response.getJSONObject(0).getInt("id")+"s="+s+"l="+l);
                                                                        bd.Insertproduitvv(response.getJSONObject(0).getInt("id"),id,id_livraison,idpv,produits[yaw],quantite[yaw],quantite_u[yaw],montant[yaw],"0","0","0","0","0");
                                                                        bd.close();
                                                                        yaw++;
                                                                        if(yaw==s){
                                                                            yaw=0;
                                                                            Main.setCreditt(1);

                                                                            onBackPressed();

                                                                            progress.dismiss();
                                                                            bd.close();
                                                                        }


                                                                        //SHOW RESPONSE FROM SERVER


                                                                       // String responseString= response.get(0).toString();


                                                                    } catch (JSONException e) {
                                                                        Log.e( "WAHI",e.toString());
                                                                         progress.dismiss();
                                                                         bd.close();
                                                                        e.printStackTrace();
                                                                        Toast.makeText(editventes.this, "Il y a pas de livraisons", Toast.LENGTH_SHORT).show();
                                                                    }

                                                            }

                                                            //ERROR
                                                            @Override
                                                            public void onError(ANError anError) {

                                                                Log.e( "WAHI",anError.toString());
                                                                dd();
                                                                progress.dismiss();


                                                            }


                                                        });



                                            }


                                            //bd();
                                            // String responseString= response.get(0).toString();


                                        } catch (JSONException e) {

                                            progress.dismiss();
                                            e.printStackTrace();
                                            Toast.makeText(editventes.this, "Peut etre elle existe une livraison pour le mm pvente ", Toast.LENGTH_SHORT).show();
                                        }

                                }

                                //ERROR
                                @Override
                                public void onError(ANError anError) {
                                    //bd();
                                    Log.e( "WAH",anError.toString());
                                    dd();

                                }


                            });*/





                }


            }
        });


    }


    public void dd(){
        String  date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String heure=new SimpleDateFormat("HH:mm").format(new Date());
int bon =1,id_liv=1,idp=1;
bd.open();
        Cursor cc=bd.getp(id);

        while (cc.moveToNext()){
            idp=cc.getInt(cc.getColumnIndex("id"))+1;
        }

        idpv=bd.getidpvente(id_ll);
        for(int i=0;i<s;i++) {
if(montant[i]!=null) {
    bd.deletep(produits[i], id_ll);
    bd.Insertproduitvv(idp, id, id_ll, idpv, produits[i], quantite[i], quantite_u[i], montant[i], "0", "0", "0", "0", "0");
}

            if(i==s-1){


                progress.dismiss();
                bd.close();
                Main.setidCreditt(id_ll);
                Main.exit(1);
onBackPressed();


            }

idp++;
        }

progress.dismiss();
bd.close();



    }

    public void getp(){




                                    AndroidNetworking.post(DATA_INSERT_URL)
                                            .addBodyParameter("action","get produitv")
                                            .addBodyParameter("id",id+"")

                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                    if(response != null)
                                                        try {
                                                            for(int i=s-1;i==0;i--){
                                                                Log.e("III",i+"");
                                                                if(i==s)
                                                            IDP[i]=response.getJSONObject(0).getInt("id")+"";
                                                                else{
                                                                    IDP[i]=response.getJSONObject(0).getInt("id")-1+"";
                                                                }

                                                                if(i==0){
                                                                    click();
                                                                }


                                                            }




                                                            //SHOW RESPONSE FROM SERVER


                                                            // String responseString= response.get(0).toString();


                                                        } catch (JSONException e) {
                                                            Log.e( "WAHI",e.toString());



                                                            e.printStackTrace();
                                                            Toast.makeText(editproduitsss.this, "Il y a pas de livraisons", Toast.LENGTH_SHORT).show();
                                                        }

                                                }

                                                //ERROR
                                                @Override
                                                public void onError(ANError anError) {

                                                    Log.e( "WAHI",anError.toString());


                                                }


                                            });



                                }

    public void onclick(){



        mRecyclerView1.addOnItemTouchListener(            new gvente.RecyclerItemClickListener(getApplicationContext(), new gvente.RecyclerItemClickListener.OnItemClickListener() {
            public void onItemClick(View v, int position){
            final int m=position;
                if(ind1!=null)
                    position=ind1[position];

                idp=id_l1[position];
                posp=position;
                final int posi=position;
               if(! vselect(id_l1[position])) {
                   final NumberPicker task = new NumberPicker(editproduitsss.this);

                   //Set TextView text color
                   Context context = editproduitsss.this;
                   LinearLayout layout = new LinearLayout(context);
                   layout.setOrientation(LinearLayout.VERTICAL);
                   task.setMinValue(0);
                   task.setMaxValue(Integer.parseInt(quantite_f[position]));
                   task.setWrapSelectorWheel(true);
                   final NumberPicker taskk = new NumberPicker(editproduitsss.this);

                   //Set TextView text color

                   taskk.setMinValue(0);
                   taskk.setMaxValue(Integer.parseInt(quantite_u_d[position]));
                   taskk.setWrapSelectorWheel(true);

                   TextView t= new TextView(editproduitsss.this);
                  t.setText("Quantité à vendre en unité");
                   TextView tt= new TextView(editproduitsss.this);
                   tt.setText("Quantité à vendre en fardeau");
                   layout.addView(tt);
                   layout.addView(task);
                   layout.addView(t);
                   layout.addView(taskk);
                   AlertDialog dialog = new AlertDialog.Builder(editproduitsss.this)
                           .setTitle(""+produit[position])
                           .setMessage("")
                           .setView(layout)

                           .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   String quan = String.valueOf(task.getValue());
                                   String quant = String.valueOf(taskk.getValue());

                                   if((Integer.parseInt(quan)+Integer.parseInt(quant)!=0 )&& !quan.isEmpty()  && !quant.isEmpty()){

                                       select[s] = id_l1[posi];
                                       bd.open();
                                       String type=bd.gettype(idpv);
                                       Double promotion = bd.getpromotion(id_l1[posi],type);
                                       if(type.equals("Grossiste"))
                                           montant[s]=(Double.parseDouble(prix_f[posi])-promotion)*((Integer.parseInt(quan))*Integer.parseInt(fardeau[posi])+(Integer.parseInt(quant)))+"";
                                       else
                                           montant[s]=(Double.parseDouble(prix_uu[posi])-promotion)*((Integer.parseInt(quan))*Integer.parseInt(fardeau[posi])+(Integer.parseInt(quant)))+"";
                                       bd.close();
                                       produits[s]=produit[posi];
                                       selected[s]=m;

                                       quantite[s]=quan;

                                       QV[s]=quan;
                                       quantite_u[s]=quant;
                                       QVU[s]="0";
                                       endomage[s]="0";

                                       reste="0";
                                       payement="0";
                                       select1(posi);
                                       Log.e("S="+s,produits[s]);


                                       s++;
                                      // total();



                                   }
                                   else{

                                   }


                               }
                           })
                           .setNegativeButton("Annuler", null)
                           .create();
                   dialog.show();



               }
               else{




               }


            }


        }));



    }
    public void faire(int v){
        for(int i=0;i<s;i++){

                if (select[i] == v) {
                    bd.open();
                    Log.e("PP",produits[i]+" "+id_ll);
                    bd.deletep(produits[i],id_ll);
                    bd.close();

                    for (int j = i; j < s; j++) {
                        if (select[j + 1] != null)
                            select[j] = select[j + 1];
                        montant[j]=montant[j+1];
                        quantite[j]=quantite[j+1];
                        QV[j]=QV[j+1];
                        quantite_u[j]=quantite_u[j+1];
                        QVU[j]="0";
                        produits[j]=produits[j+1];
                        selected[j]=selected[j+1];
                        endomage[j]="0";

                        reste="0";
                        payement="0";


                    }
                    s--;


                }
               // total();



        }



    }
    public boolean vselect(int v){
        for(int i=0;i<s;i++){
            if(select[i]!=null) {
                if (select[i] == v)
                    return true;
            }

        }
        return false;


    }
    public void click(){




                Intent myIntent = new Intent(editproduitsss.this, liv.class);
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





                for(int i=0;i<s;i++) {
                    if (produits[i] != null)
                        pr.add(produits[i]);
                    if (quantite[i] != null)
                        q.add(quantite[i]);
                    if (quantite_u[i] != null)
                        qu.add(quantite_u[i]);
                    if (montant[i] != null)
                        mon.add(montant[i]);
                    if (QV[i] != null)
                        qv.add(QV[i]);
                    if (QVU[i] != null)
                        quv.add(QVU[i]);

                    if (endomage[i] != null)
                        end.add(endomage[i]);
                    if (IDP[i] != null){

                        idp.add(IDP[i]+"");}




                }
                myIntent.putExtra("id_l", id_livraison+"");
        myIntent.putExtra("bon", bon+"");

        myIntent.putExtra("idlivreur", id+"");

                myIntent.putExtra("pvente", pvente+"");

        myIntent.putExtra("payement", payement+"");
        myIntent.putExtra("reste", reste+"");


                myIntent.putStringArrayListExtra("produit", (ArrayList<String>) pr);

                myIntent.putStringArrayListExtra("idp", (ArrayList<String>) idp);


                myIntent.putStringArrayListExtra("quantite", (ArrayList<String>)q);
                myIntent.putStringArrayListExtra("quantite_u", (ArrayList<String>)qu);

                myIntent.putStringArrayListExtra("montant", (ArrayList<String>) mon);
                myIntent.putStringArrayListExtra("quantite_v", (ArrayList<String>) qv);
                myIntent.putStringArrayListExtra("quantite_u_v", (ArrayList<String>) quv);

                myIntent.putExtra("motif", "");
                myIntent.putStringArrayListExtra("endomage", (ArrayList<String>) end);





                startActivity(myIntent);

        finish();




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




                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    id_l[i] = response.getJSONObject(i).getInt("id");
                                    nom[i] = response.getJSONObject(i).getString("nom");
                                    num[i] = response.getJSONObject(i).getString("num");
                                    adresse[i] = response.getJSONObject(i).getString("lieu");
                                    zone[i] = response.getJSONObject(i).getString("zone");
                                    if(!response.getJSONObject(i).getString("reste").equals("null"))
                                        credit[i] = response.getJSONObject(i).getString("reste");
                                    else
                                        credit[i]=0+"";
                                }
                                progress.dismiss();


                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(editproduitsss.this, "Nom d'utilisateur où le mot de passe sont incorrectes ", Toast.LENGTH_SHORT).show();
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
                        bdd();
                    }


                });


    }

    public void bdd(){

        bd.open();
Log.e("BDD1","_____");
        Cursor c =bd.getpvente();
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

        }
        else {
            Toast.makeText(editproduitsss.this, "Magasin est vide ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

    }

    public void filt(){




    }

    public void select1(int f){
        Parcelable recylerViewState = null;
        if(mRecyclerView1.getLayoutManager()!=null){
            recylerViewState = mRecyclerView1.getLayoutManager().onSaveInstanceState();
        }
        String charSequence=theFilter1.getText().toString();

        int cpt=0;
        ind1= new Integer[id_l1.length];

        ArrayList<Item2> exampleList = new ArrayList<>();
        for(int i=0;i<id_l1.length;i++) {
            if(produit[i].toLowerCase().contains(charSequence.toString().toLowerCase())){
                ind1[cpt]=i;
                cpt++;


                exampleList.add(new Item2("",produit[i],"",prix_u[i]));

            }
        }



        mRecyclerView1 = findViewById(R.id.recycler1);

        mRecyclerView1.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(editproduitsss.this);
        mAdapter1 = new Adapter3(exampleList);

        mAdapter1.setSelectedItem(selected);

        mRecyclerView1.setLayoutManager(mLayoutManager);
        mRecyclerView1.setAdapter(mAdapter1);
        mRecyclerView1.getLayoutManager().onRestoreInstanceState(recylerViewState);

    }

    public void get_pvente1(){
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

                                id_l1= new int[response.length()];
                                produit= new String[response.length()];
                                prix_f= new String[response.length()];
                                prix_u= new String[response.length()];

                                quantite_f= new String[response.length()];





                                bd.open();

                                for(int i=0;i<response.length();i++) {
                                    id_l1[i] = response.getJSONObject(i).getInt("id");
                                    produit[i] = response.getJSONObject(i).getString("nom");
                                    prix_u[i] = (int)Double.parseDouble(response.getJSONObject(i).getString("quantite_f"))+" F";
                                    prix_f[i] = response.getJSONObject(i).getString("prix_f");
                                    quantite_f[i] = (int)Double.parseDouble(response.getJSONObject(i).getString("quantite_f"))+"";

                                 // bd.Insertproduit(id_l1[i],produit[i],prix_u[i],prix_f[i],quantite_f[i]);

                                }
                                progress.dismiss();
                                ajout1();

                                //SHOW RESPONSE FROM SERVER

                                bd.close();

                                //bd();
                                // String responseString= response.get(0).toString();


                            } catch (JSONException e) {

                                progress.dismiss();
                                e.printStackTrace();
                                Toast.makeText(editproduitsss.this, "Ton stock est vide", Toast.LENGTH_SHORT).show();
                            }

                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        progress.dismiss();
                        anError.printStackTrace();
                        id_l1= new int[0];
                        prix_u= new String[0];
                        produit= new String[0];
                        prix_f= new String[0];

                        bdd1();
                    }


                });


    }
    public void bdd1(){

        bd.open();

        Cursor c =bd.getproduit();
         int j =c.getCount();
        id_l1= new int[j];
        produit= new String[j];
        prix_f= new String[j];
        prix_u= new String[j];
        prix_uu= new String[j];

        quantite_f= new String[j];
        quantite_u_d= new String[j];

        code_bar= new String[j];
        fardeau= new String[j];



        int i=0,cpt=0;
        while (c.moveToNext()){

            id_l1[i] = c.getInt(c.getColumnIndex("id"));
            produit[i] = c.getString(c.getColumnIndex("nom"));
            quantite_f[i] = c.getString(c.getColumnIndex("quantite_f"))+"";

            prix_f[i] = c.getString(c.getColumnIndex("prix_f"));
            prix_u[i] = c.getString(c.getColumnIndex("quantite_f"))+" F";
          code_bar[i] = c.getString(c.getColumnIndex("code_bar"));
          fardeau[i] = c.getString(c.getColumnIndex("fardeaup"));
            quantite_u_d[i] = c.getInt(c.getColumnIndex("quantite_f"))*(Integer.parseInt(fardeau[i]))+c.getInt(c.getColumnIndex("quantite_u"))+"";

            prix_uu[i] = c.getString(c.getColumnIndex("prix_uu"));

            for(int x=0;x<idpp.size();x++){
                Log.e("CC",produit[i]+" "+idpp.get(x));
                if(produit[i].equals(idpp.get(x))){
                    produits[cpt]=produit[i];
                    select[cpt]= id_l1[i];
                    selected[cpt]=i;
                cpt++;}

            }

     s=cpt;
            i++;
        }

        bd.close();
        if(i!=0){
            ajout1();
        }
        else {
            Toast.makeText(editproduitsss.this, "Magasin est vide ou vous n'avez pas téléchargé les missions", Toast.LENGTH_SHORT).show();
        }

    }
    public void filt1(){

        theFilter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int k, int i1, int i2) {


                int cpt=0,cp=0;
                ind1= new Integer[id_l1.length];
                Integer[] m= new Integer[select.length];

                ArrayList<Item2> exampleList = new ArrayList<>();
                for(int i=0;i<id_l1.length;i++) {
                    if(produit[i].toLowerCase().contains(charSequence.toString().toLowerCase())){
                        for(int j=0;j<s;j++){
                            if(id_l1[i]==select[j]){
                                m[cp]=cpt;
                                cp++;
                            }
                        }
                        ind1[cpt]=i;
                        cpt++;


                        exampleList.add(new Item2("",produit[i],"",prix_u[i]));

                    }
                }



                mRecyclerView1 = findViewById(R.id.recycler1);

                mRecyclerView1.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(editproduitsss.this);
                mAdapter1 = new Adapter3(exampleList);
                mAdapter1.setSelectedItem(m);

                mRecyclerView1.setLayoutManager(mLayoutManager);
                mRecyclerView1.setAdapter(mAdapter1);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void ajout1(){

        int cpt=0,total=0,Q=0,qu=0;
        String q="",p="";
        int m=0;
        String[] pv;
        pv=new String[id_l1.length];
        ArrayList<Item2> exampleList = new ArrayList<>();

        for(int i=0;i<id_l1.length;i++) {


            exampleList.add(new Item2("",produit[i],"",prix_u[i]));

        }




        mRecyclerView1 = findViewById(R.id.recycler1);

        mRecyclerView1.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(editproduitsss.this);
        mAdapter1 = new Adapter3(exampleList);

        mRecyclerView1.setLayoutManager(mLayoutManager);
        mRecyclerView1.setAdapter(mAdapter1);


select1(0);

    }






}
