package com.casbaherpapp.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter1 extends RecyclerView.Adapter<Adapter1.ExampleViewHolder> {
    private ArrayList<Item1> mExampleList;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.textC1);
            mTextView2 = itemView.findViewById(R.id.textC2);
            mTextView3 = itemView.findViewById(R.id.textC3);
            mTextView4 = itemView.findViewById(R.id.textC4);
            mTextView5 = itemView.findViewById(R.id.textC5);
            mTextView6 = itemView.findViewById(R.id.textC6);

        }
    }

    public Adapter1(ArrayList<Item1> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card1, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Item1 currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());
        holder.mTextView5.setText(currentItem.getText5());
        holder.mTextView6.setText(currentItem.getText6());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public String getText1(){
        return mExampleList.get(1).getText1();


    }

}