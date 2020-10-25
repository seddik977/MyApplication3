package com.casbaherpapp.myapplication.ui.tools;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.casbaherpapp.myapplication.liv;
import com.casbaherpapp.myapplication.ui.gallery.GalleryFragment;
import com.casbaherpapp.myapplication.ui.gallery.GalleryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.casbaherpapp.myapplication.ui.gventes.gvente.saveFile;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    private GalleryViewModel galleryViewModel;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    public ProgressDialog progress ;
    private int[] id_l,bon;
    private String[] pvente,id_pvente,heure,faite,motif,photo,date,valide,payement,reste;
    private String[][] produit,quantite,quantite_u,montant_bon,endomage,QV,QVU,IDP;
    private Integer[] ind;
    private EditText theFilter;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context con;
    private View v;


private Button D;
    private int id=-2,year,dayOfMonth,month;
private BDD bd;
    private boolean  shouldRefreshOnResume=false;
    public void onResume(){


        super.onResume();
        //update your fragment
        if(shouldRefreshOnResume) {
            NavigationView navigationView = ((Main)getContext()).findViewById(R.id.nav_view);
            navigationView.getMenu().performIdentifierAction(R.id.nav_tools,1);}
    }
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bd = new BDD(getContext());

        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
       /* final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
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


       /* final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        con=root.getContext();
        v=root;
        id = ((Main)getContext()).getData();
        //progress.show();
        mRecyclerView = root.findViewById(R.id.recycler2);
        theFilter = (EditText) root.findViewById(R.id.searchFilter);

        verif();
        get_liv();
        filt();
        Onclick();
        D = root.findViewById(R.id.datex);
        Calendar calendar = Calendar.getInstance();
       year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int day) {
                                m++;
                                String d=y+"-"+m+"-"+day;
                                String dd=convertDate(day)+"-"+convertDate(m)+"-"+y;

                                D.setText(dd+"");
                                theFilter.setText("");
                                ind=null;
                                year=y;
                                month=m-1;
                                dayOfMonth=day;
//progress.show();
                                get_liv(d);
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();}
        });

        return root;
    }
    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector mGestureDetector;
        private GalleryFragment.RecyclerItemClickListener.OnItemClickListener mListener;
        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        public RecyclerItemClickListener(Context context, GalleryFragment.RecyclerItemClickListener.OnItemClickListener listener) {
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
    public void ajout(){

        int cpt=0,total=0,Q=0,qu=0,p=0;
        String q="";
        Double m=0.0;
        String[] pv;
        pv=new String[id_l.length];
        ArrayList<Item> exampleList = new ArrayList<>();
        for(int i=0;i<id_l.length;i++) {
            for(int j=0;j<produit[i].length;j++) {
                if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                    q=q+QV[i][j] + " F,  " + QVU[i][j] + " U,";
                    p=p+1;}

                if((QV[i][j]!=null) && (QVU[i][j]!=null)){
           Q = Q + Integer.parseInt(QV[i][j]);
           qu = qu + Integer.parseInt(QVU[i][j]);}





        }
            if(faite[i].equals("oui") ){
                cpt++;

            }
            if(payement[i]!=null){
                m=m+Double.parseDouble(payement[i]);
                total = total + Integer.parseInt(payement[i]);}
            exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
            q="";
            p=0;
            m=0.0;
        }

        TextView t = v.findViewById(R.id.totalP);
        TextView tt = v.findViewById(R.id.totalQQ);
        t.setText("Total : "+total+" DA");
        tt.setText("QT: "+Q+"F, "+qu+"U");


        mRecyclerView = v.findViewById(R.id.recycler2);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        mAdapter = new Adapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




    }

    public void Onclick(){

        mRecyclerView.addOnItemTouchListener(            new GalleryFragment.RecyclerItemClickListener(con, new GalleryFragment.RecyclerItemClickListener.OnItemClickListener() {
            public void onItemClick(View v, int position){
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
                List<String> r= new ArrayList<>() ;
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
                myIntent.putExtra("payement", payement[position]+"");
                myIntent.putExtra("reste", reste[position]+"");



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
                if(!photo[position].equals("") && !photo[position].toLowerCase().equals("null")){                    //String s = photo[position].replace("data:image/png;base64,","");

                    byte[] decodedString = Base64.decode(photo[position], Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    c="cc";
                }
                if(decodedByte!=null)
                saveFilee(getContext(),decodedByte,"myImage.jpg");

                myIntent.putExtra("photo", c+"");
                myIntent.putExtra("tab", 2+"");
                myIntent.putExtra("user", "non");

                startActivity(myIntent);


            }


        }));

    }
    public static void saveFilee(Context context, Bitmap b, String picName){
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
    public void get_liv(){
        progress.show();
        final int[] x = {0};
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get ventes")
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
                                    photo[i] = "";

                                    date[i] = response.getJSONObject(i).getString("date");
                                    valide[i] = response.getJSONObject(i).getString("valide");
                                    payement[i] = response.getJSONObject(i).getString("payement");
                                    reste[i] = response.getJSONObject(i).getString("reste");


                                    String date_m =response.getJSONObject(i).getString("date_m");

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
                                                                Log.e( "BDD",l+"_"+j);
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
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        id_l= new int[0];
                        bon= new int[0];
                        pvente= new String[0];
                        id_pvente= new String[0];

                        produit= new String[0][0];
                        quantite= new String[0][0];
                        montant_bon= new String[0][0];
                        heure= new String[0];
                        faite= new String[0];
                        motif= new String[0];
                        photo= new String[0];

                        payement= new String[0];
                        endomage= new String[0][0];
                        QV= new String[0][0];
                        reste= new String[0];

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


                int cpt=0,total=0,Q=0,qu=0,p=0;
                String q="";
                ind= new Integer[id_l.length];
                Double m=0.0;
                String[] pv;
                pv=new String[id_l.length];
                ArrayList<Item> exampleList = new ArrayList<>();
                for(int i=0;i<id_l.length;i++) {
                    if(pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase()) || id_pvente[i].toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        ind[cpt]=i;
                        cpt++;
                    for(int j=0;j<produit[i].length;j++) {
                        if((quantite[i][j]!=null) && (produit[i][j]!=null)){


                            q=q+QV[i][j] + " F,  " + QVU[i][j] + " U,";
                            p=p+1;}

                        if((QV[i][j]!=null) && (QVU[i][j]!=null)){
                            Q = Q + Integer.parseInt(QV[i][j]);
                            qu = qu + Integer.parseInt(QVU[i][j]);}





                    }
                        if(payement[i]!=null){
                            m=m+Double.parseDouble(payement[i]);
                            total = total + Integer.parseInt(payement[i]);}
                        exampleList.add(new Item("Bon :"+bon[i]+ ", heure : " +heure[i], "Nb de produits :"+p+", total :"+payement[i]+ " DA", "", "N°:"+id_pvente[i]+", "+pvente[i] ,faite[i]));
                    q="";
                    p=0;
                    m=0.0;}
                }



                mRecyclerView = v.findViewById(R.id.recycler2);

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(con);
                mAdapter = new Adapter(exampleList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    public void get_liv(String d){
        progress.show();
        final int[] x = {0};
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get ventesA")
                .addBodyParameter("id",id+"")
                .addBodyParameter("date",d+"")
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
                                   photo[i] = "";

                                    date[i] = response.getJSONObject(i).getString("date");
                                    valide[i] = response.getJSONObject(i).getString("valide");
                                    payement[i] = response.getJSONObject(i).getString("payement");
                                    reste[i] = response.getJSONObject(i).getString("reste");


                                    String date_m =response.getJSONObject(i).getString("date_m");

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
                                                                Log.e( "BDD",l+"_"+j);
                                                                IDP[l][j]=response.getJSONObject(j).getString("id");
                                                                produit[l][j]=response.getJSONObject(j).getString("produit");
                                                                quantite[l][j]=response.getJSONObject(j).getString("quantite");
                                                                quantite_u[l][j]=response.getJSONObject(j).getString("quantite_u");
                                                                montant_bon[l][j]=response.getJSONObject(j).getString("montant_bon");
                                                                endomage[l][j]=response.getJSONObject(j).getString("endomage");
                                                                QV[l][j]=response.getJSONObject(j).getString("quantite_v");
                                                                QVU[l][j]=response.getJSONObject(j).getString("quantite_u_v");



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
                        progress.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                        id_l= new int[0];
                        bon= new int[0];
                        pvente= new String[0];
                        id_pvente= new String[0];

                        produit= new String[0][0];
                        quantite= new String[0][0];
                        montant_bon= new String[0][0];
                        heure= new String[0];
                        faite= new String[0];
                        motif= new String[0];
                        photo= new String[0];

                        payement= new String[0];
                        endomage= new String[0][0];
                        QV= new String[0][0];
                        reste= new String[0];

                        ajout();
                    }


                });


    }
}