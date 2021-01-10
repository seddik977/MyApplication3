package com.casbaherpapp.myapplication.imad.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Dialog.DeleteProductWarnningDialog;
import com.casbaherpapp.myapplication.imad.Dialog.EditeProductDialog;
import com.casbaherpapp.myapplication.imad.Entities.Product;
import com.casbaherpapp.myapplication.imad.Listerners.ClickListener;
import com.casbaherpapp.myapplication.imad.Listerners.ShopingCartListener;
import com.casbaherpapp.myapplication.imad.Transformation.CropCircleTransformation;
import com.casbaherpapp.myapplication.imad.Transformation.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopingCartAdapter extends RecyclerView.Adapter<ShopingCartAdapter.ViewHolder> implements Filterable {
    private ArrayList<Product> shopingProducts;
    private LayoutInflater mInflater;
    private  ShopingCartListener shopingCartListener;
    private Context context;
    private FragmentManager fm;

  public ShopingCartAdapter(Context context,FragmentManager fm, ArrayList<Product> shopingProducts, ShopingCartListener shopingCartListener){
this.shopingProducts = shopingProducts;
      this.mInflater = LayoutInflater.from(context);
this.shopingCartListener=shopingCartListener;
this.context = context;
this.fm=fm;

  }

    public ShopingCartListener getShopingCartListener() {
        return shopingCartListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shoping_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopingCartAdapter.ViewHolder holder, int position) {
                holder.productName.setText(shopingProducts.get(position).getNom());
                holder.productFamily.setText(shopingProducts.get(position).getFamille());
        holder.fardeauxNumber.setText(String.valueOf(shopingProducts.get(position).getNumberDesFardeaux()));
        holder.paletteNumber.setText(String.valueOf(shopingProducts.get(position).getNumberDesPalettes()));
        holder.prix.setText(String.valueOf(shopingProducts.get(position).getPrixVente()+" DA"));
        Picasso.get().load("http://casbahdz.com/adm/images/"+shopingProducts.get(position).getId()+".png").transform(new RoundedCornersTransformation(30,30))
                .error(R.drawable.ic_baseline_broken_image_24).
                into(holder.productImageViewer);

    }

    @Override
    public int getItemCount() {
        return shopingProducts.size();
    }

    public Context getContext() {
        return context;
    }
    public Activity getActivity(){
     return getActivity();
    }

    public FragmentManager getFm() {
        return fm;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView productName;
            TextView productFamily;
            TextView fardeauxNumber;
            TextView paletteNumber;
            TextView prix;
            Button delete;
            Button editer;
            private ImageView productImageViewer;
        private FragmentManager fm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName=(TextView)itemView.findViewById(R.id.productShopingName);
            productFamily=(TextView)itemView.findViewById(R.id.productShopingFamily);
            fardeauxNumber=(TextView)itemView.findViewById(R.id.nombreDesFardeaux);
            paletteNumber=(TextView)itemView.findViewById(R.id.nombreDesPalettes);
            prix=(TextView)itemView.findViewById(R.id.prix);
            delete =(Button)itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
            editer=(Button)itemView.findViewById(R.id.editer);
            editer.setOnClickListener(this);
            productImageViewer = (ImageView)itemView.findViewById(R.id.productImageView);

        }

        @Override
        public void onClick(View view) {
                switch (view.getId()){
                    case R.id.delete:

                        DeleteProductWarnningDialog deleteProductWarnningDialog = DeleteProductWarnningDialog.newInstance("title", new ClickListener() {
                            @Override
                            public void onPositionClicked(int position) {

                            }

                            @Override
                            public void onButtonPressed(View view) {
                                shopingCartListener.removeProductFromCart(shopingProducts.get(getAdapterPosition()).getId(),getAdapterPosition());

                            }

                            @Override
                            public void onLongClicked(int position) {

                            }
                        });
                        deleteProductWarnningDialog.show(getFm(),"title");
                        break;
                    case R.id.editer:
                        fm=getFm();

                         int nombreDesFardeaux = getShopingProducts().get(getAdapterPosition()).getNumberDesFardeaux();
                         int nombreDesPalettes = getShopingProducts().get(getAdapterPosition()).getNumberDesPalettes();

                        EditeProductDialog deliverySuccessDialog = EditeProductDialog.newInstance("title",nombreDesFardeaux,nombreDesPalettes, getShopingProducts().get(getAdapterPosition()).getId(),shopingCartListener);
                        deliverySuccessDialog.show(fm,"blabla");
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

    @Override
    public Filter getFilter() {
        return null;
    }


    public ArrayList<Product> getShopingProducts() {
        return shopingProducts;
    }

    public void setShopingProducts(ArrayList<Product> shopingProducts) {
        this.shopingProducts = shopingProducts;
        notifyDataSetChanged();
    }
    public  void addProductToCart(Product product){


    }
    public double editerProduct(int nbreFardeaux, int nbrePalettes,int id){
        for (Product product:getShopingProducts()){
            if(product.getId() == id){
                int numberBouteille = nbreFardeaux* product.getFardeau() +nbrePalettes *product.getPalette()*product.getFardeau();
                product.setNumberBouteille(numberBouteille);
                product.setNumberDesFardeaux(nbreFardeaux);
                product.setNumberDesPalettes(nbrePalettes);

                double prix = numberBouteille *product.getPrix_usine();
                DecimalFormat df2 = new DecimalFormat("#.##");
                prix =Double.valueOf(df2.format(prix));
                product.setPrixVente(prix);
                notifyDataSetChanged();


            }


        }
                return getTotalPrice();

    }
    public  double getTotalPrice(){
      double totalPrix = 0;
      for(Product product: getShopingProducts()){


          totalPrix = totalPrix + product.getPrixVente();


      }
  return  totalPrix;

    }


}
