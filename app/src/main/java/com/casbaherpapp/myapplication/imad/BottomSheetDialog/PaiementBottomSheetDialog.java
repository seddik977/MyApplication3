package com.casbaherpapp.myapplication.imad.BottomSheetDialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Adapters.HistoriqueDistVersementAdapter;
import com.casbaherpapp.myapplication.imad.Entities.VersementItem;
import com.casbaherpapp.myapplication.imad.Listerners.ProgressLoaderListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PaiementBottomSheetDialog   extends BottomSheetDialogFragment implements View.OnClickListener , View.OnLongClickListener{

    private Button retourBtn;
    private RecyclerView historiqueVersementRV;
    private ArrayList<VersementItem> versementItems;
    private HistoriqueDistVersementAdapter historiqueDistVersementAdapter;
    private ProgressDialog  progressDialog;
    private BDD dataBase;
    private  int id;

    public PaiementBottomSheetDialog() {
        super();
    }
    public static PaiementBottomSheetDialog newInstance() {
        return new PaiementBottomSheetDialog();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        retourBtn =(Button)view.findViewById(R.id.retour);
        retourBtn.setOnClickListener(this);
        historiqueVersementRV= (RecyclerView)view.findViewById(R.id.historiqueVersementRV);
        historiqueVersementRV.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getApplicationContext().getResources().getDrawable(R.drawable.check_border));
        historiqueVersementRV.addItemDecoration(dividerItemDecoration);
        historiqueVersementRV.setLayoutManager(layoutManager);
        versementItems = new ArrayList<VersementItem>();
        dataBase=new BDD(getActivity());
        dataBase.open();
        id = dataBase.getID();
        dataBase.close();
        historiqueDistVersementAdapter = new HistoriqueDistVersementAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), versementItems,id, new ProgressLoaderListener() {
            @Override
            public void showProgress() {
                progressDialog.show();
            }

            @Override
            public void closeProgress() {
                progressDialog.dismiss();
            }
        });
        historiqueVersementRV.setAdapter(historiqueDistVersementAdapter);

        AndroidNetworking.post("http://www.casbahdz.com/adm/CommandeDist/commande_dist_crud.php")
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
                                historiqueDistVersementAdapter.getVersementItems().add(versementItem);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        historiqueDistVersementAdapter.notifyDataSetChanged();




                    }





                    @Override
                    public void onError(ANError anError) {
                        Log.e("message",anError.getMessage());
                    }
                });











    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paiement_bottomsheet, container,false);


        return view;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;

    }
    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }













    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.retour:
                getDialog().dismiss();
                break;

            default:
                break;


        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
