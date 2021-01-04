package com.casbaherpapp.myapplication.imad.Entities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.ForegroundService;
import com.casbaherpapp.myapplication.Main;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Adapters.HistoriqueVersementAdapter;
import com.casbaherpapp.myapplication.imad.Adapters.HistoryAdapter;
import com.casbaherpapp.myapplication.imad.Listerners.ClickListener;
import com.casbaherpapp.myapplication.imad.Listerners.ProgressLoaderListener;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Historique extends AppCompatActivity {
private RecyclerView historiqueVersementRecyclerView;
private HistoriqueVersementAdapter historiqueVersementAdapter;
private ArrayList<VersementItem> versementItems;
private MaterialToolbar toolbar;
    private BDD dataBase;
    private  int id;
    private ProgressDialog  progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_ayout);
        progressDialog = new ProgressDialog(Historique.this);
        progressDialog.setCancelable(false);
        dataBase=new BDD(Historique.this);
        dataBase.open();
        id = dataBase.getID();
        dataBase.close();
        toolbar=(MaterialToolbar)findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), Main.class);
                startActivity(myIntent);
            }
        });

        historiqueVersementRecyclerView= (RecyclerView)findViewById(R.id.historiqueVersementRecyclerView);
        historiqueVersementRecyclerView.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.check_border));
        historiqueVersementRecyclerView.addItemDecoration(dividerItemDecoration);
        historiqueVersementRecyclerView.setLayoutManager(layoutManager);
        versementItems = new ArrayList<VersementItem>();
        historiqueVersementAdapter = new HistoriqueVersementAdapter(getApplicationContext(), getSupportFragmentManager(), versementItems,id, new ProgressLoaderListener() {
            @Override
            public void showProgress() {
               progressDialog.show();
            }

            @Override
            public void closeProgress() {
             progressDialog.dismiss();
            }
        });
        historiqueVersementRecyclerView.setAdapter(historiqueVersementAdapter);

        AndroidNetworking.post("http://www.casbahdz.com/adm/CommandeLivreur/commande_livreur_crud.php")
                .addBodyParameter("action","9").addBodyParameter("data", String.valueOf(id))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {


                    @Override
                    public void onResponse(JSONArray response) {

                               Log.e("message", String.valueOf(response.length()));
                    for(int i=0;i<response.length();i++){

                                try {
                                    VersementItem versementItem = new VersementItem();
                                    int id =  response.getJSONObject(i).getInt("id");
                                    String comptableFirstName=response.getJSONObject(i).getString("firstname");
                                    String comptableLastName=response.getJSONObject(i).getString("lastname");
                                    String action=response.getJSONObject(i).getString("action");
                                    String createdDate=response.getJSONObject(i).getString("created_date");
                                    String updatedDate=response.getJSONObject(i).getString("updated_date");
                                    double ancientCredit=response.getJSONObject(i).getDouble("credit_ancient");
                                    double noveauCredit=response.getJSONObject(i).getDouble("credit_noveau");
                                    double dernierVersement=response.getJSONObject(i).getDouble("versement");
                                    double versementTotal=response.getJSONObject(i).getDouble("versement_total");
                                    int valideState=response.getJSONObject(i).getInt("payment_flag");
                                    versementItem.setId(id);
                                    versementItem.setComptableFirstName(comptableFirstName);
                                    versementItem.setComptableLastName(comptableLastName);
                                    versementItem.setAction(action);
                                    versementItem.setCreatedDate(createdDate);
                                    versementItem.setUpdatedDate(updatedDate);
                                    versementItem.setNoveauCredit(noveauCredit);
                                    versementItem.setAncientCredit(ancientCredit);
                                    versementItem.setDernierVersement(dernierVersement);
                                    versementItem.setVersementTotal(versementTotal);
                                    versementItem.setValideState(valideState);
                                    historiqueVersementAdapter.getVersementItems().add(versementItem);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            historiqueVersementAdapter.notifyDataSetChanged();




                        }





                    @Override
                    public void onError(ANError anError) {
                         Log.e("message",anError.getMessage());
                    }
                });



    }

}
