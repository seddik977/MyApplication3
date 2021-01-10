package com.casbaherpapp.myapplication.imad.BottomSheetDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Adapters.ShopingCartAdapter;
import com.casbaherpapp.myapplication.imad.Dialog.DeleteAllWarnningDialog;
import com.casbaherpapp.myapplication.imad.Dialog.DeliverySuccessDialog;
import com.casbaherpapp.myapplication.imad.Listerners.ClickListener;
import com.casbaherpapp.myapplication.imad.Listerners.ShopingCartListener;
import com.casbaherpapp.myapplication.imad.Entities.Product;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener , View.OnLongClickListener{
    private BDD dataBase;
   private Button retourBtn;

   private RecyclerView cartRecyclerView;
   private ShopingCartAdapter shopingCartAdapter;
   private Button orderButton;

   private ArrayList<Product> shopingProducts = new ArrayList<>();
    private  ShopingCartListener shopingCartListener;
    private Button emptyShopingCart;
    private String category;
    private static final String DATA_URL = "http://www.casbahdz.com/adm/CommandeLivreur/commande_livreur_crud.php";
    private static final String DATA_URL_DIST = "http://www.casbahdz.com/adm/CommandeDist/commande_dist_crud.php";//developed by IMAD
    public CartBottomSheetDialog( ShopingCartListener shopingCartListener,String category) {
        super();
        this.shopingCartListener = shopingCartListener;
        this.category=category;
    }

    public static CartBottomSheetDialog newInstance(ShopingCartListener shopingCartListener,String category) {
        return new CartBottomSheetDialog(shopingCartListener,category);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase = new BDD(getActivity());
        shopingCartAdapter =new ShopingCartAdapter(getContext(),getActivity().getSupportFragmentManager(),shopingProducts,shopingCartListener);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
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
        View view = inflater.inflate(R.layout.cart_bottomsheet, container,false);


        return view;
    }


        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        cartRecyclerView = (RecyclerView)view.findViewById(R.id.cartRecyclerView);

Log.e("category",category);
        cartRecyclerView.hasFixedSize();
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.check_border));


        cartRecyclerView.addItemDecoration(dividerItemDecoration);
        cartRecyclerView.setLayoutManager(layoutManager);



        cartRecyclerView.setAdapter(shopingCartAdapter);
        retourBtn =(Button)view.findViewById(R.id.retour);
        orderButton =(Button) view.findViewById(R.id.orderButton);
        emptyShopingCart = (Button)view.findViewById(R.id.emptyShopingCart);
        retourBtn.setOnClickListener(this);
        orderButton.setOnClickListener(this);
        emptyShopingCart.setOnClickListener(this);

            super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.retour:
                getDialog().dismiss();
                break;
            case R.id.orderButton:


                double prixTotal = 0;

                if(category.equals("grand")){

                    for (Product pro : shopingProducts) {
                        pro.setPrixVente(pro.getPrixVente()*1.19);
                        prixTotal = pro.getPrixVente()+prixTotal;
                    }


                }else{

                    for (Product pro : shopingProducts) {
                        prixTotal = pro.getPrixVente()+prixTotal;
                    }

                }
                dataBase.open();
                final int id = dataBase.getID();
                final String role =dataBase.getrole();
                dataBase.close();
                final MaterialAlertDialogBuilder builder =new MaterialAlertDialogBuilder(getActivity(),R.style.AlertDialogTheme);
                final double finalPrixTotal = prixTotal;
                builder
                        .setTitle("Remarque")
                        .setMessage("La société Casbah confirme qu'une fois que vous commandez vos produits, il n'y a aucun moyen d'annuler la commande ou de rendre les produits commandés, veuillez bien réfléchir avant de passer une commande")
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {



                                    getDialog().dismiss();

                            }
                        }).setPositiveButton("Commandez quand même", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


if(role.equals("Distributeur")){
    AndroidNetworking.post(DATA_URL_DIST).addBodyParameter("action", "1").
            addBodyParameter("totalPrix", String.valueOf(finalPrixTotal)).
            addBodyParameter("idDist", String.valueOf(id)).
            setTag("test")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsParsed(new TypeToken<Integer>() {
            }, new ParsedRequestListener<Integer>() {

                @Override
                public void onResponse(Integer idCommande) {

                    for (Product pro : shopingProducts) {
                        int a = 0;
                        AndroidNetworking.post(DATA_URL_DIST).addBodyParameter("action", "2").
                                addBodyParameter("idCommande", String.valueOf(idCommande)).
                                addBodyParameter("idProduit", String.valueOf(pro.getId())).
                                addBodyParameter("prixVente", String.valueOf(pro.getPrixVente())).
                                addBodyParameter("nombreFardeaux", String.valueOf(pro.getNumberDesFardeaux())).
                                addBodyParameter("nombrePalettes", String.valueOf(pro.getNumberDesPalettes())).
                                setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build().getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.e("sucess", String.valueOf(response.length()));
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("error", anError.getMessage());

                            }
                        });
                        Context context = getContext();
                        CharSequence text = "Envoi en cours!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        getDialog().dismiss();






                    }


                    DeliverySuccessDialog deliverySuccessDialog =DeliverySuccessDialog.newInstance("title",shopingCartListener);
                    deliverySuccessDialog.show(getFragmentManager(),"title");
                    shopingCartListener.removeAllProductFromCart();
                }

                @Override
                public void onError(ANError anError) {

                }
            });





}
if(role.equals("Livreur")){


    AndroidNetworking.post(DATA_URL).addBodyParameter("action", "1").
            addBodyParameter("totalPrix", String.valueOf(finalPrixTotal)).
            addBodyParameter("idLivreur", String.valueOf(id)).
            setTag("test")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsParsed(new TypeToken<Integer>() {
            }, new ParsedRequestListener<Integer>() {

                @Override
                public void onResponse(Integer idCommande) {

                    for (Product pro : shopingProducts) {
                        int a = 0;
                        AndroidNetworking.post(DATA_URL).addBodyParameter("action", "2").
                                addBodyParameter("idCommande", String.valueOf(idCommande)).
                                addBodyParameter("idProduit", String.valueOf(pro.getId())).
                                addBodyParameter("prixVente", String.valueOf(pro.getPrixVente())).
                                addBodyParameter("nombreFardeaux", String.valueOf(pro.getNumberDesFardeaux())).
                                addBodyParameter("nombrePalettes", String.valueOf(pro.getNumberDesPalettes())).
                                setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build().getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.e("sucess", String.valueOf(response.length()));
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
                        Context context = getContext();
                        CharSequence text = "Envoi en cours!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        getDialog().dismiss();






                    }


                    DeliverySuccessDialog deliverySuccessDialog =DeliverySuccessDialog.newInstance("title",shopingCartListener);
                    deliverySuccessDialog.show(getFragmentManager(),"title");
                    shopingCartListener.removeAllProductFromCart();
                }

                @Override
                public void onError(ANError anError) {

                }
            });







}

                    }
                }).show();
                break;
            case R.id.emptyShopingCart:
                DeleteAllWarnningDialog deleteAllWarnningDialog =DeleteAllWarnningDialog.newInstance("titl", new ClickListener() {
                    @Override
                    public void onPositionClicked(int position) {

                    }

                    @Override
                    public void onButtonPressed(View view) {
                        shopingCartListener.removeAllProductFromCart();
                    }

                    @Override
                    public void onLongClicked(int position) {

                    }
                });
                        deleteAllWarnningDialog.show(getFragmentManager(),"titl");
                break;

            default:
                break;


        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public ArrayList<Product> getShopingProducts() {
        return shopingCartAdapter.getShopingProducts();
    }

    public void setShopingProducts(ArrayList<Product> shopingProducts) {
        this.shopingProducts = shopingProducts;
        shopingCartAdapter.setShopingProducts(shopingProducts);
    }

    public  void addProductToCart(Product product){
shopingProducts.add(product);

    }

    public RecyclerView getCartRecyclerView() {
        return cartRecyclerView;
    }

    public void setCartRecyclerView(RecyclerView cartRecyclerView) {
        this.cartRecyclerView = cartRecyclerView;
    }

    public void removeProductFromCart(int id, int position) {
        shopingCartAdapter.getShopingProducts().remove(position);
        cartRecyclerView.removeViewAt(position);
        shopingCartAdapter.notifyItemRemoved(position);
        shopingCartAdapter.notifyItemRangeChanged(position, shopingCartAdapter.getShopingProducts().size());
    }

    public ShopingCartListener getShopingCartListener() {
        return shopingCartListener;
    }

    public void setShopingCartListener(ShopingCartListener shopingCartListener) {
        this.shopingCartListener = shopingCartListener;
    }

    public ShopingCartAdapter getShopingCartAdapter() {
        return shopingCartAdapter;
    }

    public void setShopingCartAdapter(ShopingCartAdapter shopingCartAdapter) {
        this.shopingCartAdapter = shopingCartAdapter;
    }
}
