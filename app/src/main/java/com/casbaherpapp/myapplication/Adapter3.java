package com.casbaherpapp.myapplication;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter3 extends RecyclerView.Adapter<Adapter3.ExampleViewHolder> {
    private ArrayList<Item2> mExampleList;
private View v;
private Integer[] selected_item;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public View v;


        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.id);
            mTextView2 = itemView.findViewById(R.id.textC2);
            mTextView3 = itemView.findViewById(R.id.textC3);




        }
    }

    public Adapter3(ArrayList<Item2> exampleList) {
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
        View someView = v.findViewById(R.id.ll);
        int v=0;
        if(selected_item!=null){
        for(int i=0;i<selected_item.length;i++){
            if(selected_item[i]!=null){
        if(position == selected_item[i]) {
            holder.itemView.setBackgroundColor(Color.parseColor("#7ed6df"));
            v=1;
        }} }}if(v==0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        Log.e( "BDD",position+"");


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void setSelectedItem(Integer[] j){
        selected_item=new Integer[j.length];
        for(int i=0;i<selected_item.length;i++){
            if(j[i]!=null){
        selected_item[i]=j[i];
            Log.e( "BDD",j[i]+"");}
        }
    }


    public String getText1(){
        return mExampleList.get(1).getText1();


    }

}