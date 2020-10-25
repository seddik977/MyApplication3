package com.casbaherpapp.myapplication.imad.Listerners;

import android.view.View;

public interface ClickListener {
    void onPositionClicked(int position);
    void onButtonPressed(View view);
    void onLongClicked(int position);
}
