package com.casbaherpapp.myapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {
    private ArrayList<Item> mExampleList;
    private int selected_item=-1,ver=0;
    private View v;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public ImageView image;
        public ImageView photo;


        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.textC1);
            mTextView2 = itemView.findViewById(R.id.textC2);
            mTextView3 = itemView.findViewById(R.id.textC3);
            mTextView4 = itemView.findViewById(R.id.textC4);
            image = itemView.findViewById(R.id.image1);
            photo = itemView.findViewById(R.id.image);


        }
    }

    public Adapter(ArrayList<Item> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Item currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());
        if(currentItem.getText5().toString().equals("oui")){
            holder.image.setImageResource(R.drawable.oui);

        }
        else{
            holder.image.setImageResource(R.drawable.non);

        }


        View someView = v.findViewById(R.id.ll);// get Any child View

        if(position == selected_item) {
            holder.itemView.setBackgroundColor(Color.parseColor("#7ed6df"));

        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(ver==1){
            holder.image.setImageResource(0);
            if(currentItem.getText5().toString().equals("oui")){
                holder.photo.setImageResource(R.drawable.oui);

            }
            else{
                holder.photo.setImageResource(R.drawable.non);

            }
        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void setSelectedItem(int i){
        selected_item=i;

    }
    public void setV(){
        ver=1;

    }

    public String getText1(){
        return mExampleList.get(1).getText1();


    }

}