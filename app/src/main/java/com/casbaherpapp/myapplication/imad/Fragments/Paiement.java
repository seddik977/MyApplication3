package com.casbaherpapp.myapplication.imad.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Entities.Historique;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Paiement extends Fragment implements View.OnClickListener{
    private Button historyBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_paiement, container, false);
        historyBtn = (Button)root.findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener(this);
        return  root;
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){

                case R.id.historyBtn:
                    Intent myIntent = new Intent(getContext(), Historique.class);
                    startActivity(myIntent);
                    break;
                default:
                    break;



            }
    }
}
