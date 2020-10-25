package com.casbaherpapp.myapplication.imad.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.casbaherpapp.myapplication.BDD;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Listerners.FilterFamilyListener;
import com.casbaherpapp.myapplication.imad.OrderProducts;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener , View.OnLongClickListener{

    private  Context _context;
    private AutoCompleteTextView filterAutoCompleteTextView;
    private ArrayAdapter<String> filterAdapter;
    private Button appliquer;
    private  Button quitter;
    private BDD dataBase;
    private FilterFamilyListener filterFamilyListener;
    private String familyName;
    public FilterDialogFragment(Context context,FilterFamilyListener filterFamilyListener){
        familyName = "Tous";
        this.filterFamilyListener = filterFamilyListener;
        this._context =context;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static FilterDialogFragment newInstance(Context context,String title,FilterFamilyListener filterFamilyListener) {
        FilterDialogFragment frag = new FilterDialogFragment(context,filterFamilyListener);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_dialog_fragment, container,false);
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
        appliquer = (Button) view.findViewById(R.id.appliquer);
        quitter = (Button) view.findViewById(R.id.quitter);
        appliquer.setOnClickListener(this);
        quitter.setOnClickListener(this);
        filterAutoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.FilterAutoCompleteTextVew);
        final ArrayList<String> familyNames = new ArrayList<String>();

        dataBase = new BDD(getActivity());
        dataBase.open();
        Cursor c = dataBase.getProductsFamillyName();
        familyNames.add("Tous");
        while (c.moveToNext()){
            familyNames.add(c.getString(c.getColumnIndex("famille")));
        }
        filterAdapter = new ArrayAdapter<>(getActivity(),R.layout.filter_list_item,familyNames);
        filterAutoCompleteTextView.setAdapter(filterAdapter);

        dataBase.close();

        filterAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             familyName = filterAdapter.getItem(i).toString();
            }
        });

    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }









        @Override
    public void onClick(View view) {
switch (view.getId())
{
    case R.id.appliquer:
              filterFamilyListener.setFamilyFilter(familyName);
              getDialog().dismiss();
        break;
    case R.id.quitter:
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
