package com.casbaherpapp.myapplication.ui.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.MainActivity;
import com.casbaherpapp.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private static final String DATA_INSERT_URL="http://www.casbahdz.com/CRUD.php";
    private int id=-1;
    private View v;
    private BDD bd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        FloatingActionButton fab = ((Main)getContext()).findViewById(R.id.fab);
        fab.hide();
        bd= new BDD(root.getContext());
        v=root;
        id = ((Main)getContext()).getData();

        verif();
        //setDepot();
        setInfos();



        return root;
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
    public void setInfos(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get infos")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                //SHOW RESPONSE FROM SERVER
                                TextView d=((Main)getActivity()).findViewById(R.id.nvendeur);
                                d.setText(id+"");
                                TextView nn=((Main)getActivity()).findViewById(R.id.nom);
                                nn.setText(response.getJSONObject(0).getString("nom"));
                                TextView m=((Main)getActivity()).findViewById(R.id.email);
                                m.setText(response.getJSONObject(0).getString("mail"));
                                TextView mm=((Main)getActivity()).findViewById(R.id.branche);
                                mm.setText(((Main)getContext()).getB());
                                TextView b=((Main)getActivity()).findViewById(R.id.vehicule);
                                b.setText(response.getJSONObject(0).getString("vehicule"));
                                TextView e=((Main)getActivity()).findViewById(R.id.entreprise);
                                e.setText(response.getJSONObject(0).getString("entreprise"));
                                TextView xe=((Main)getActivity()).findViewById(R.id.mobileNumber);
                                xe.setText(response.getJSONObject(0).getString("num"));
                                TextView ve=((Main)getActivity()).findViewById(R.id.type);
                                ve.setText("Vendeur");

                                TextView xce=((Main)getActivity()).findViewById(R.id.credit);
                                xce.setText("VOTRE CREDIT :"+response.getJSONObject(0).getString("credit"));

                                TextView xxce=((Main)getActivity()).findViewById(R.id.versement);
                                xxce.setText("VERSEMENTS :"+response.getJSONObject(0).getString("versement"));

                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}


    public void setDepot(){
        AndroidNetworking.post(DATA_INSERT_URL)
                .addBodyParameter("action","get depot")
                .addBodyParameter("id",id+"")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response != null)
                            try {
                                //SHOW RESPONSE FROM SERVER

                                TextView nn=((Main)getActivity()).findViewById(R.id.iddepot);
                                nn.setText(response.getJSONObject(0).getString("id"));




                            } catch (JSONException e) {


                                e.printStackTrace();
                                Toast.makeText(getContext(), "Erreur ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();


                    }


                });}
}