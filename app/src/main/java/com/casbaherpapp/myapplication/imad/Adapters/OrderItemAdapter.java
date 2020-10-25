package com.casbaherpapp.myapplication.imad.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Entities.Product;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> implements Filterable {
    private LayoutInflater mInflater;
    private Context context;
    private  ArrayList<Product> orderProductItems;
    public OrderItemAdapter(Context context, ArrayList<Product> orderProductItems){
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.orderProductItems =orderProductItems;
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_item, parent, false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
     holder.productName.setText(orderProductItems.get(position).getNom());
        holder.productFamily.setText(orderProductItems.get(position).getFamille());
        holder.productPrice.setText(orderProductItems.get(position).getPrixVente()+" DA");
        Picasso.get().load("http://casbahdz.com/adm/images/"+orderProductItems.get(position).getId()+".png")
                .error(R.drawable.ic_baseline_broken_image_24).
                resize(100,200).
                into(holder.productImage);
       holder.nombrePalettes.setText(String.valueOf(orderProductItems.get(position).getNumberDesPalettes()));
        holder.nombreFardeaux.setText(String.valueOf(orderProductItems.get(position).getNumberDesFardeaux()));
    }

    @Override
    public int getItemCount() {
        return orderProductItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder  {
            public TextView productPrice;
            public TextView productFamily;
            public TextView productName;
            public ImageView productImage;
            public TextView nombrePalettes;
            public TextView nombreFardeaux;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productPrice =(TextView)itemView.findViewById(R.id.productPrice);
            productFamily = (TextView)itemView.findViewById(R.id.productFamilly);
            productName = (TextView)itemView.findViewById(R.id.productName);
            productImage=(ImageView)itemView.findViewById(R.id.productImage);
            nombreFardeaux=(TextView) itemView.findViewById(R.id.nombrePalettes);
            nombrePalettes =(TextView)itemView.findViewById(R.id.nombreFardeaux);



        }
    }


}
