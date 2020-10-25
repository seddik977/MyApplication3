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
import com.casbaherpapp.myapplication.imad.Listerners.ClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteAllWarnningDialog extends DialogFragment implements  View.OnClickListener {
        private ClickListener clickListener;
        private Button yesDelete;
        private Button nonDelete;
        public static DeleteAllWarnningDialog newInstance(String title,ClickListener clickListener) {
                DeleteAllWarnningDialog deleteAllWarnningDialog = new DeleteAllWarnningDialog(clickListener);
                Bundle args = new Bundle();
                args.putString("title", title);
                deleteAllWarnningDialog.setArguments(args);

                return deleteAllWarnningDialog;
        }

        public DeleteAllWarnningDialog(ClickListener clickListener) {
                                this.clickListener =clickListener;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.delete_all_products_dialog, container, false);
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
                yesDelete = (Button) view.findViewById(R.id.yesDelete);
                nonDelete = (Button) view.findViewById(R.id.nonDelete);
                yesDelete.setOnClickListener(this);
                nonDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
                switch (view.getId()) {

                        case R.id.yesDelete:
                                clickListener.onButtonPressed(view);
                                getDialog().dismiss();
                                break;
                        case R.id.nonDelete:
                                getDialog().dismiss();
                                break;

                        default:
                                break;
                }
        }

        @Override
        public void onResume() {
                super.onResume();
                Window window = getDialog().getWindow();
                int width = getResources().getDimensionPixelSize(R.dimen.dialogdim);
                int height = getResources().getDimensionPixelSize(R.dimen.deletedialogdimH);
                getDialog().getWindow().setLayout(width, height);
        }

}