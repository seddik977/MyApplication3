package com.casbaherpapp.myapplication.imad.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Dialog.OrderProductDialog;
import com.casbaherpapp.myapplication.imad.Entities.Product;
import com.casbaherpapp.myapplication.imad.Listerners.ClickListener;
import com.casbaherpapp.myapplication.imad.Listerners.ShopingCartListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> implements Filterable{

    private ArrayList<Product> products;
    private ArrayList<Product> backUpProducts;
    private LayoutInflater mInflater;
    private final ClickListener listener;
    private Context context;
    private TextView  prix_total;
    private ShopingCartListener shopingCartListener;
    private FragmentManager fm;
    private  OrderProductDialog orderProductDialog;
    // data is passed into the constructor
    public CardListAdapter(Context context, FragmentManager fm, ArrayList<Product> products, TextView prix_total,
                           ClickListener listener, ShopingCartListener shopingCartListener) {
        this.mInflater = LayoutInflater.from(context);
        this.fm=fm;
        this.listener = listener;
        this.context = context;
        this.products = products;
        this.backUpProducts = products;
        this.shopingCartListener = shopingCartListener;
        this.prix_total = prix_total;
        backUpProducts = new ArrayList<>();
        backUpProducts.addAll(products);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item_layout, parent, false);
        return new ViewHolder(view,listener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

       Product p = products.get(position);
        holder.nomProduit.setText(p.getNom());
        holder.prix.setText(String.valueOf(p.getPrix_usine()+" DA"));
        holder.nombreF.setText(p.getFardeau()+"F");
        holder.nombreP.setText(p.getPalette()+"P");
        holder.familyProduit.setText(p.getFamille());
        if(p.getQuantiteUsine()==0){
        holder.quantiteUsine.setText("Produit non disponible");
        holder.commander.setVisibility(View.GONE);
        }
        holder.productId=p.getId();
      Picasso.get().load("http://casbahdz.com/adm/images/"+p.getId()+".png")
              .error(R.drawable.ic_baseline_broken_image_24).
              resize(100,200).
            into(holder.imageproduit);

        for(int i=0;i<backUpProducts.size();i++){
            if(backUpProducts.get(i).getId()==p.getId()) {
                if (backUpProducts.get(i).isSelected()) {
                    holder.commander.setVisibility(View.GONE);
                    holder.verifiedCart.setVisibility(View.VISIBLE);
                } else {
                    holder.commander.setVisibility(View.VISIBLE);
                    holder.verifiedCart.setVisibility(View.GONE);
                }
            }
        }



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return products.size();
    }

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }
    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

        ArrayList<Product> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(backUpProducts);


            } else {
                for (Product product:backUpProducts) {
                    if (product.getNom().toLowerCase().contains(charSequence.toString().toLowerCase()) || product.getFamille().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(product);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            products.clear();
            products.addAll((Collection<? extends Product>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void resetRecyclerView() {


        products.clear();
        products.addAll(backUpProducts);
        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView nomProduit;
        TextView prix;
        TextView nombreF;
        TextView nombreP;
        ImageView imageproduit;
        ImageView verifiedCart;
        TextView familyProduit;
        Button commander;
        TextView quantiteUsine;
        int productId;
        ClickListener listener;
        private WeakReference<ClickListener> listenerRef;
        ViewHolder(View itemView,ClickListener listener) {
            super(itemView);
            nomProduit = itemView.findViewById(R.id.nomProduit);
            imageproduit = itemView.findViewById(R.id.produitImage);
            verifiedCart = (ImageView)itemView.findViewById(R.id.verifiedCart);
            commander = itemView.findViewById(R.id.commander);
            familyProduit  = itemView.findViewById(R.id.familyProduit);
            prix = itemView.findViewById(R.id.prix);
            nombreF = itemView.findViewById(R.id.nombreF);
            nombreP  = itemView.findViewById(R.id.nombreP);
            quantiteUsine =itemView.findViewById(R.id.quantiteUsine);
            nomProduit.setOnClickListener(this);
            commander.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.commander:

                     orderProductDialog = OrderProductDialog.newInstance("titl", productId, new ShopingCartListener() {
                        @Override
                        public void addProductToCart(Product product) {

                        }

                        @Override
                        public void removeProductFromCart(int id, int position) {

                        }

                        @Override
                        public void removeAllProductFromCart() {

                        }

                        @Override
                        public void editerProduct(int nbreFardeaux, int nbrePalettes, int id) {
                                                        for(int i=0;i<backUpProducts.size();i++) {
                                                            if (backUpProducts.get(i).getId() == productId) {
                                                                int numberBouteille = nbreFardeaux * backUpProducts.get(i).getFardeau() +nbrePalettes * backUpProducts.get(i).getPalette() * backUpProducts.get(i).getFardeau();
                                                                backUpProducts.get(i).setNumberBouteille(numberBouteille);
                                                                backUpProducts.get(i).setNumberDesFardeaux(nbreFardeaux);
                                                                backUpProducts.get(i).setNumberDesPalettes(nbrePalettes);

                                                                double prix = numberBouteille * backUpProducts.get(i).getPrix_usine();
                                                                DecimalFormat df2 = new DecimalFormat("#.##");
                                                                prix = Double.valueOf(df2.format(prix));
                                                                if (prix != 0) {
                                                                    backUpProducts.get(i).setPrixVente(prix);
                                                                    backUpProducts.get(i).setSelected(true);

                                                                    orderProductDialog.dismiss();


                                                                    shopingCartListener.addProductToCart(backUpProducts.get(i));
                                                                    notifyDataSetChanged();

                                                                }

                                                            }
                                                        }

                        }

                    });
                    orderProductDialog.show(getFm(),"titl");

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

    // convenience method for getting data at click position
    Product getProduct(int index) {
        return backUpProducts.get(index);
    }

    public ArrayList<Product> getProducts(){
        return backUpProducts;
    }
    public void showUpProduct(int id){
        for(int i=0;i<getProducts().size();i++){

            if(getProducts().get(i).getId()==id){
                getProducts().get(i).setSelected(false);
                notifyDataSetChanged();

            }
        }

    }
    public void resetProductList(){
        for(Product product:getProducts()){
            product.setSelected(false);
            product.setPrixVente(0);
        }
        notifyDataSetChanged();
    }
}