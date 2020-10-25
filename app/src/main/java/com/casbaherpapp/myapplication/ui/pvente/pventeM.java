package com.casbaherpapp.myapplication.ui.pvente;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class pventeM extends ViewModel {

    private MutableLiveData<String> mText;

    public pventeM() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}