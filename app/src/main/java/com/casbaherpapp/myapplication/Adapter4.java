package com.casbaherpapp.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter4 extends RecyclerView.Adapter<Adapter4.ExampleViewHolder> {
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






        }
    }

    public Adapter4(ArrayList<Item2> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card3, parent, false);


        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Item2 currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());








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