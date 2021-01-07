package com.casbaherpapp.myapplication.imad;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Adapters.CardListAdapter;
import com.casbaherpapp.myapplication.imad.Entities.Product;
import com.casbaherpapp.myapplication.imad.BottomSheetDialog.CartBottomSheetDialog;
import com.casbaherpapp.myapplication.imad.BottomSheetDialog.HistoryBottomSheetDialog;
import com.casbaherpapp.myapplication.imad.Dialog.FilterDialogFragment;
import com.casbaherpapp.myapplication.imad.Listerners.ClickListener;
import com.casbaherpapp.myapplication.imad.Listerners.FilterFamilyListener;
import com.casbaherpapp.myapplication.imad.Listerners.ShopingCartListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderProducts extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public TextView prixTotal;
    public  double total =0;
    private BDD dataBase;
    private ArrayList<Product> products;
    private RecyclerView recyclerView;
    private CardListAdapter adapter;
  private String category;

    private TextView textCartItemCount;
    private  int cartItemCount = 0 ;
    private TextView filterTitle;
     private HistoryBottomSheetDialog  historyBottomSheetDialog ;
     private  FilterDialogFragment filterDialogFragment;
        private  CartBottomSheetDialog cartBottomSheetDialog;
        private FragmentManager fm;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_products);
            category="petit";
        prixTotal =(TextView) findViewById(R.id.tv_total);
        filterTitle=(TextView) findViewById(R.id.filterTitle);


        products = new ArrayList<Product>();
          recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderProducts.this));

        fm = getSupportFragmentManager();
        cartBottomSheetDialog =CartBottomSheetDialog.newInstance(new ShopingCartListener() {
            @Override
            public void addProductToCart(Product product) {

            }

            @Override
            public void removeProductFromCart(int id,int position) {

               double prix =  cartBottomSheetDialog.getShopingProducts().get(position).getPrixVente();
               total = total - prix;
                prixTotal.setText(String.valueOf(total)+"DA");
                cartBottomSheetDialog.removeProductFromCart(id,position);
                adapter.showUpProduct(id);
                cartItemCount = cartItemCount -1;
                textCartItemCount.setText(String.valueOf(cartItemCount));
                setupBadge();
                if(cartItemCount==0){
                    cartBottomSheetDialog.getDialog().dismiss();
                }


            }

            @Override
            public void removeAllProductFromCart() {
                final  int size = cartBottomSheetDialog.getShopingProducts().size();

                cartBottomSheetDialog.getShopingCartAdapter().getShopingProducts().clear();
                cartBottomSheetDialog.getShopingCartAdapter().notifyItemRangeRemoved(0,size);
                adapter.resetProductList();
                cartItemCount = 0;
                prixTotal.setText("0.0 DA");
                total = 0;
                setupBadge();
                cartBottomSheetDialog.getDialog().dismiss();


            }

            @Override
            public void editerProduct(int nbreFardeaux, int nbrePalettes,int id) {

               total  = cartBottomSheetDialog.getShopingCartAdapter().editerProduct(nbreFardeaux,nbrePalettes,id);
               prixTotal.setText(total + "DA");
            }
        });
        dataBase = new BDD(OrderProducts.this);
        fetchDataFromDB(); //fetch products from database and show it


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return true;
            }
        });

        final MenuItem cartMenuItem = menu.findItem(R.id.cart);

        View actionView = cartMenuItem.getActionView();
         textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(cartMenuItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter:

                filterDialogFragment = FilterDialogFragment.newInstance(getApplicationContext(), "Some Title", new FilterFamilyListener() {
                    @Override
                    public void setFamilyFilter(String familyname) {
                        if(familyname.equals("Tous")){
                            adapter.resetRecyclerView();

                        }else{

                            adapter.getFilter().filter(familyname);
                        }

                        filterTitle.setText(familyname);
                    }
                });
                filterDialogFragment.show(fm, "fragment_edit_name");
                return true;

            case R.id.history:

                 historyBottomSheetDialog =HistoryBottomSheetDialog.newInstance();
                historyBottomSheetDialog.show(fm,"title");



                return true;

            case R.id.cart:

                    if(cartItemCount==0){
                        final MaterialAlertDialogBuilder builder =new MaterialAlertDialogBuilder(OrderProducts.this,R.style.AlertDialogTheme);

                        builder
                                .setTitle("Remarque")
                                .setMessage("Ops!!! malheureusement, vous ne pouvez pas accéder avec un panier vide")
                              .setPositiveButton("Je comprend trés bien", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }}).show();


                            }else{

                        cartBottomSheetDialog.show(fm,"title");

                    }


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (cartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(cartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }






    public void fetchDataFromDB(){

        dataBase.open();

        Cursor c = dataBase.getAllProducts();

Log.e("size", String.valueOf(c.getCount()));


        while (c.moveToNext()){
            Product p = new Product(
                    c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("nom")),
                    c.getString(c.getColumnIndex("famille")),
                    c.getInt(c.getColumnIndex("fardeau")),
                    c.getInt(c.getColumnIndex("palette")),
                    c.getDouble(c.getColumnIndex("prix_usine")),
                    c.getInt(c.getColumnIndex("quantite_u"))
                    );



products.add(p);

        }


        dataBase.close();
        if(products.size()!=0){
            adapter = new CardListAdapter(OrderProducts.this,getSupportFragmentManager(), products, prixTotal, new ClickListener() {
                @Override
                public void onPositionClicked(int position) {
                    // callback performed on click
                }

                @Override
                public void onButtonPressed(View view) {

                }

                @Override
                public void onLongClicked(int position) {
                    // callback performed on click
                }
            }, new ShopingCartListener() {
                @Override
                public void addProductToCart(Product product) {

                cartBottomSheetDialog.addProductToCart(product);

                    total = total + product.getPrixVente();
                    cartItemCount = cartItemCount +1;
                    textCartItemCount.setText(String.valueOf(cartItemCount));
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    prixTotal.setText(df2.format(total) + "DA");
                    setupBadge();

                }

                @Override
                public void removeProductFromCart(int id,int position) {

                }

                @Override
                public void removeAllProductFromCart() {

                }

                @Override
                public void editerProduct(int nbreFardeaux, int nbrePalettes,int id) {

                }
            },category);
            recyclerView.setAdapter(adapter);

        }
        else {
            Toast.makeText(OrderProducts.this, "Pas de produits", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View view) {
switch (view.getId()) {



    default:
        break;
}
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
