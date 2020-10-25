package com.casbaherpapp.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter2 extends RecyclerView.Adapter<Adapter2.ExampleViewHolder> {
    private ArrayList<Item2> mExampleList;
private View v;
private int selected_item=-1;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView0;

        public View v;


        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.textC1);
            mTextView2 = itemView.findViewById(R.id.textC2);
            mTextView3 = itemView.findViewById(R.id.textC3);
            mTextView0 = itemView.findViewById(R.id.id);





        }
    }

    public Adapter2(ArrayList<Item2> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card2, parent, false);


        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Item2 currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView0.setText(currentItem.getText0());
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        BDD bd= new BDD(v.getContext());
        double seuil=0;
        bd.open();
        if(!bd.getseuil().equals(""))
      seuil= Double.parseDouble(bd.getseuil());
        bd.close();


        View someView = v.findViewById(R.id.ll);// get Any child View
        try {
            if(Double.parseDouble(currentItem.getText3())>seuil )
                holder.itemView.setBackgroundColor(Color.parseColor("#f96d80"));



        } catch(NumberFormatException ex)
        {
        }


        if(position == selected_item) {
            holder.itemView.setBackgroundColor(Color.parseColor("#7ed6df"));
        }
        if(currentItem.getText0().equals(""))
            holder.itemView.setBackgroundColor(Color.RED);



    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void setSelectedItem(int i){
        selected_item=i;
    }

    public String getText1(){
        return mExampleList.get(1).getText1();


    }

}