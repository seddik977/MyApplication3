package com.casbaherpapp.myapplication.imad.Dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Listerners.ShopingCartListener;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeliverySuccessDialog extends DialogFragment implements View.OnClickListener , View.OnLongClickListener{
    private Button understandButton;
    private ShopingCartListener shopingCartListener;

    public DeliverySuccessDialog(ShopingCartListener shopingCartListener) {
        this.shopingCartListener =shopingCartListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static DeliverySuccessDialog newInstance(String title,ShopingCartListener shopingCartListener) {
        DeliverySuccessDialog frag = new DeliverySuccessDialog(shopingCartListener);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_success_dialog, container,false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //set the dialog to non-modal and disable dim out fragment behind
        Window window = dialog.getWindow();

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        /*window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/
        return dialog;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        understandButton=(Button)view.findViewById(R.id.understandButton);

        understandButton.setOnClickListener(this);





    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.understandButton:

                getDialog().dismiss();

                break;

                    default:
                        break;



        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int width = getResources().getDimensionPixelSize(R.dimen.dialogdim);
        int height = getResources().getDimensionPixelSize(R.dimen.dialogdim);
        getDialog().getWindow().setLayout(width, height);
    }
}
