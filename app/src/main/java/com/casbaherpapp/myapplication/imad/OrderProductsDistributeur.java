package com.casbaherpapp.myapplication.imad;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Adapters.CardListAdapter;
import com.casbaherpapp.myapplication.imad.BottomSheetDialog.CartBottomSheetDialog;
import com.casbaherpapp.myapplication.imad.BottomSheetDialog.HistoryBottomSheetDialog;
import com.casbaherpapp.myapplication.imad.BottomSheetDialog.InfoBottomSheetDialog;
import com.casbaherpapp.myapplication.imad.BottomSheetDialog.PaiementBottomSheetDialog;
import com.casbaherpapp.myapplication.imad.Dialog.FilterDialogFragment;
import com.casbaherpapp.myapplication.imad.Entities.Product;
import com.casbaherpapp.myapplication.imad.Listerners.ClickListener;
import com.casbaherpapp.myapplication.imad.Listerners.FilterFamilyListener;
import com.casbaherpapp.myapplication.imad.Listerners.ShopingCartListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
public class OrderProductsDistributeur extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    public TextView prixTotal;
    private ProgressDialog nDialog;
    public  double total =0;
    private BDD dataBase;
    private static final double TVA = 1.19;
    private ArrayList<Product> products;
    private RecyclerView recyclerView;
    private CardListAdapter adapter;
    private static final String DATA_URL = "http://www.casbahdz.com/adm/CommandeDist/commande_dist_crud.php";//developed by IMAD
    private TextView textCartItemCount;
    private  int cartItemCount = 0 ;
    private TextView filterTitle;
     private HistoryBottomSheetDialog  historyBottomSheetDialog ;
     private  FilterDialogFragment filterDialogFragment;
        private  CartBottomSheetDialog cartBottomSheetDialog;
        private FragmentManager fm;
        private String category="petit";
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_products);

        prixTotal =(TextView) findViewById(R.id.tv_total);
        filterTitle=(TextView) findViewById(R.id.filterTitle);
        products = new ArrayList<Product>();
          recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderProductsDistributeur.this));
        dataBase = new BDD(OrderProductsDistributeur.this);
        dataBase.open();
        category = dataBase.getcategory();
        dataBase.close();
        get_produit_disponible();
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
        },category);



        //fetch products from database and show it

        nDialog = new ProgressDialog(OrderProductsDistributeur.this);
        nDialog.setMessage("Chargement..");
        nDialog.setTitle("Chargement des donnees");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu_dist, menu);
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
                        final MaterialAlertDialogBuilder builder =new MaterialAlertDialogBuilder(OrderProductsDistributeur.this,R.style.AlertDialogTheme);

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


            case R.id.action_disconnect:
                dataBase.open();
                dataBase.dec();
                dataBase.close();

                Intent intent = this.getBaseContext().getPackageManager().getLaunchIntentForPackage(this.getBaseContext().getPackageName() );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                return true;

            case R.id.action_payment:

                PaiementBottomSheetDialog paiementBottomSheetDialog=PaiementBottomSheetDialog.newInstance();
                paiementBottomSheetDialog.show(fm,"paiement");

                return true;
            case R.id.action_info:

            InfoBottomSheetDialog infoBottomSheetDialog =InfoBottomSheetDialog.newInstance();
            infoBottomSheetDialog.show(fm,"info");
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
            adapter = new CardListAdapter(OrderProductsDistributeur.this,getSupportFragmentManager(), products, prixTotal, new ClickListener() {
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
            Toast.makeText(OrderProductsDistributeur.this, "Pas de produits", Toast.LENGTH_SHORT).show();
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
    private void get_produit_disponible() {
        dataBase.open();
        int id =dataBase.getID();
        dataBase.close();
        AndroidNetworking.post(DATA_URL)
                .addBodyParameter("action","0")
                .addBodyParameter("id", String.valueOf(id))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        nDialog.dismiss();

                        if (response != null){
                            int id;
                            String nom;
                            String famille;
                            int fardeau;
                            int palette;
                            double prix_usine;
                            int quantite_u;



                            try {


                                for(int i = 0 ; i<response.length();i++) {

                                    id = response.getJSONObject(i).getInt("id");
                                    nom = response.getJSONObject(i).getString("nom");
                                    famille = response.getJSONObject(i).getString("famille");
                                    fardeau = response.getJSONObject(i).getInt("fardeau");
                                    palette = response.getJSONObject(i).getInt("palette");
                                    prix_usine = response.getJSONObject(i).getDouble("prix_usine");
                                    quantite_u = response.getJSONObject(i).getInt("quantite_u");
                                    category = response.getJSONObject(i).getString("category");
                                   Log.e("category",category);
                                    Product p = new Product(
                                         id,
                                           nom,
                                          famille,
                                           fardeau,
                                            palette,
                                           prix_usine,
                                            quantite_u
                                    );



                                    products.add(p);
                                }
                                if(products.size()!=0){
                                    adapter = new CardListAdapter(OrderProductsDistributeur.this,getSupportFragmentManager(), products, prixTotal, new ClickListener() {
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
                                    Toast.makeText(OrderProductsDistributeur.this, "Pas de produits", Toast.LENGTH_SHORT).show();
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }



                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {


                        Log.e("imad",anError.getMessage());


                    }


                });
    }

}


