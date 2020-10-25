package com.casbaherpapp.myapplication.ui.gventes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class gventeM extends ViewModel {

    private MutableLiveData<String> mText;

    public gventeM() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}