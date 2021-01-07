package com.casbaherpapp.myapplication.imad.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Dialog.OrderItemsDialog;
import com.casbaherpapp.myapplication.imad.Entities.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<Order> orders;
    private LayoutInflater mInflater;
    private Context context;
    private FragmentManager fm;
    private String role;
    private RecyclerView parentRecyclerView;
    public HistoryAdapter(FragmentManager fm,Context context, ArrayList<Order> orders,String role) {
        this.orders = orders;
        this.role=role;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.fm =fm;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);


      holder.totalPrixTextView.setText(String.valueOf(orders.get(position).getPrixtTotal()+"DA"));


        Date date = null;
      try {
           date = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(orders.get(position).getCreatedDate());
          holder.orderDateTextView.setText(new SimpleDateFormat("EEE d MMM y", Locale.FRENCH).format(date));
       } catch (ParseException e) {
            e.printStackTrace();
        }




        if(orders.get(position).isVerified()){
            holder.waitingAnimation.setVisibility(View.GONE);
            holder.orderStatuTextView.setText("Vérifié");
            holder.orderStatuTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else{
            holder.checkAnimation.setVisibility(View.GONE);
            holder.orderStatuTextView.setText("En attente");
            holder.orderStatuTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        holder.orderId = orders.get(position).getIdOrder();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        parentRecyclerView =recyclerView;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            public TextView totalPrixTextView;
            public TextView orderDateTextView;
            public TextView orderStatuTextView;

            public Button show;
            public  LottieAnimationView waitingAnimation;
            public LottieAnimationView checkAnimation;
            private int orderId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            totalPrixTextView =(TextView)itemView.findViewById(R.id.totalPrix);
            orderStatuTextView = (TextView)itemView.findViewById(R.id.orderStatu);
            orderDateTextView=(TextView)itemView.findViewById(R.id.orderDate) ;
            waitingAnimation =(LottieAnimationView)itemView.findViewById(R.id.waitingAnimation);
            checkAnimation = (LottieAnimationView) itemView.findViewById(R.id.checkAnimation);


            show = (Button)itemView.findViewById(R.id.show);
            show.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.show:
                            OrderItemsDialog orderItemsDialog =OrderItemsDialog.newInstance(getContext(),"title",role,orderId);
                            orderItemsDialog.show(getFm(),"title");

                            break;



                    }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
