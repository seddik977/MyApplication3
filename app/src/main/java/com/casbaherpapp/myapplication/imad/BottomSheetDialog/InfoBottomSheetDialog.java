package com.casbaherpapp.myapplication.imad.BottomSheetDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class InfoBottomSheetDialog  extends BottomSheetDialogFragment implements View.OnClickListener , View.OnLongClickListener{
    private Button retourBtn;
    private BDD dataBase;
    private  int id;
    private TextView firstname;
    private TextView lastname;
    private TextView username;
    private TextView password;
    private TextView phone;
    private TextView credit;
    private TextView versement;
    private TextView createddate;

    public InfoBottomSheetDialog() {
        super();
    }
    public static InfoBottomSheetDialog newInstance() {
        return new InfoBottomSheetDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_bottomsheet, container,false);


        return view;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;

    }
    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retourBtn =(Button)view.findViewById(R.id.retour);
        retourBtn.setOnClickListener(this);
        dataBase=new BDD(getActivity());
        dataBase.open();
        id = dataBase.getID();
        dataBase.close();
        firstname=(TextView)view.findViewById(R.id.firstname);
        lastname=(TextView)view.findViewById(R.id.lastname);
        username=(TextView)view.findViewById(R.id.username);
        password=(TextView)view.findViewById(R.id.password);
        phone=(TextView)view.findViewById(R.id.phone);
        credit=(TextView)view.findViewById(R.id.credit);
        versement=(TextView) view.findViewById(R.id.versement);
        createddate=(TextView)view.findViewById(R.id.createddate);


        AndroidNetworking.post("http://www.casbahdz.com/adm/CRUD1.php")
                .addBodyParameter("action","get info distributeur")
                .addBodyParameter("idDist", String.valueOf(id))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("res", String.valueOf(response.length()));
                try {
                    String f_name= response.getString("nom");
                    String l_name=response.getString("prenom");
                    String user_name=response.getString("mail");
                    String pass_word=response.getString("mdp");
                    String phone_number=response.getString("num");
                    String money = response.getString("credit");
                    String versemen = response.getString("versement");
                    String created_date=response.getString("created_date");
                    firstname.setText(f_name);
                    lastname.setText(l_name);
                    username.setText(user_name);
                    password.setText(pass_word);
                    phone.setText(phone_number);
                    credit.setText(money+" DA");
                    versement.setText(versemen+" DA");
                    createddate.setText(created_date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(ANError anError) {
                Log.e("res", anError.getMessage());

            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.retour:
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
}
