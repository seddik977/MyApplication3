package com.casbaherpapp.myapplication.imad.BottomSheetDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Adapters.HistoryAdapter;
import com.casbaherpapp.myapplication.imad.Entities.Order;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener , View.OnLongClickListener {

    private Button retourBtn;

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private BDD dataBase;
    private MaterialDatePicker materialDatePicker;
    private ArrayList<Order> orders  =new  ArrayList<>();

    public HistoryBottomSheetDialog() {
        super();
    }
    public static HistoryBottomSheetDialog newInstance() {
        return new HistoryBottomSheetDialog();
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
        View view = inflater.inflate(R.layout.history_bottomsheet, container,false);


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBase = new BDD(getActivity());
        dataBase.open();
      int id = dataBase.getID();
    dataBase.close();
        retourBtn =(Button)view.findViewById(R.id.retour);
        retourBtn.setOnClickListener(this);

        historyRecyclerView = (RecyclerView)view.findViewById(R.id.historyRecyclerView);
        historyRecyclerView .hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
        DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.check_border));
        historyRecyclerView .addItemDecoration(dividerItemDecoration);
        historyRecyclerView .setLayoutManager(layoutManager);
        historyAdapter = new HistoryAdapter(getActivity().getSupportFragmentManager(),getContext(),orders);
        historyRecyclerView .setAdapter(historyAdapter);
      AndroidNetworking.post("http://www.casbahdz.com/adm/CommandeLivreur/commande_livreur_crud.php")
                .addBodyParameter("action","5").addBodyParameter("data", String.valueOf(id))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {

                    @Override
                    public void onResponse(JSONArray response) {
                          for(int i=0;i<response.length();i++){

                            try {
                                Order order = new Order();
                                double prixTotal = response.getJSONObject(i).getDouble("total_prix");
                               int id =  response.getJSONObject(i).getInt("id");
                                  int state= response.getJSONObject(i).getInt("state");
                                String createdDate = response.getJSONObject(i).getString("created_date");

                                order.setPrixtTotal(prixTotal);
                                order.setVerified(state==1?true:false);
                                order.setIdOrder(id);
                                order.setCreatedDate(createdDate);

                                        historyAdapter.getOrders().add(order);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        historyAdapter.notifyDataSetChanged();


                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {



                        anError.printStackTrace();

                    }


                });



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
