package com.casbaherpapp.myapplication.imad.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Adapters.HistoryAdapter;
import com.casbaherpapp.myapplication.imad.Adapters.OrderItemAdapter;
import com.casbaherpapp.myapplication.imad.Entities.Order;
import com.casbaherpapp.myapplication.imad.Entities.Product;
import com.casbaherpapp.myapplication.imad.OrderProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class OrderItemsDialog extends DialogFragment implements  View.OnClickListener  {
    private Context context;
    private int orderId;
    private Button closeButton;
    private String role;
    private RecyclerView orderItemsRecyclerView;
    private ArrayList<Product> orderProductItems;
    public OrderItemsDialog(Context context,String role,int orderId) {
        this.orderId =orderId;
        this.context = context;
        this.role = role;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public static OrderItemsDialog newInstance(Context context, String title,String role, int orderId){
        OrderItemsDialog oid = new OrderItemsDialog(context,role,orderId);
        Bundle args = new Bundle();
        args.putString("title", title);
        oid.setArguments(args);
        return  oid;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_items_fragment_dialog, container,false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //set the dialog to non-modal and disable dim out fragment behind
        Window window = dialog.getWindow();

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        /*window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/
        return dialog;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        closeButton = (Button) view.findViewById(R.id.closeBtn);
        closeButton.setOnClickListener(this);
        orderProductItems = new ArrayList<>();
        orderItemsRecyclerView = (RecyclerView) view.findViewById(R.id.orderItemRecyclerView);
        orderItemsRecyclerView.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.check_border));
        orderItemsRecyclerView.addItemDecoration(dividerItemDecoration);
        orderItemsRecyclerView.setLayoutManager(layoutManager);
        if (role.equals("Distributeur")) {

            AndroidNetworking.post("http://www.casbahdz.com/adm/CommandeDist/commande_dist_crud.php")
                    .addBodyParameter("action","6").addBodyParameter("data", String.valueOf(getOrderId()))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for(int i =0;i<response.length();i++){
                                Product product =new Product();
                                try {
                                    product.setNom(response.getJSONObject(i).getString("nom"));
                                    product.setFamille(response.getJSONObject(i).getString("famille"));
                                    product.setPrixVente(response.getJSONObject(i).getDouble("prix_vente"));
                                    product.setId(response.getJSONObject(i).getInt("id_produit"));
                                    product.setNumberDesFardeaux(response.getJSONObject(i).getInt("nombre_fardeaux"));
                                    product.setNumberDesPalettes(response.getJSONObject(i).getInt("nombre_palettes"));
                                    orderProductItems.add(product);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            orderItemsRecyclerView.setAdapter(new OrderItemAdapter(getContext(),orderProductItems));
                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                        }


                    });




        }
        if (role.equals("Livreur")) {


            AndroidNetworking.post("http://www.casbahdz.com/adm/CommandeLivreur/commande_livreur_crud.php")
                    .addBodyParameter("action","6").addBodyParameter("data", String.valueOf(getOrderId()))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for(int i =0;i<response.length();i++){
                                Product product =new Product();
                                try {
                                    product.setNom(response.getJSONObject(i).getString("nom"));
                                    product.setFamille(response.getJSONObject(i).getString("famille"));
                                    product.setPrixVente(response.getJSONObject(i).getDouble("prix_vente"));
                                    product.setId(response.getJSONObject(i).getInt("id_produit"));
                                    product.setNumberDesFardeaux(response.getJSONObject(i).getInt("nombre_fardeaux"));
                                    product.setNumberDesPalettes(response.getJSONObject(i).getInt("nombre_palettes"));
                                    orderProductItems.add(product);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            orderItemsRecyclerView.setAdapter(new OrderItemAdapter(getContext(),orderProductItems));
                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                        }


                    });

        }

        }


    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen.orderitemwidth);
        int height = getResources().getDimensionPixelSize(R.dimen.orderitemheight);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
                switch (view.getId()){
                    case R.id.closeBtn:
                        getDialog().dismiss();
                        break;

                    default:
                        break;
                }
    }
}
