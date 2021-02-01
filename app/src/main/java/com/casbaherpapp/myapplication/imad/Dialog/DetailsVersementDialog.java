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
import android.widget.TextView;

import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Entities.VersementItem;


import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.NumberFormat;
public class DetailsVersementDialog extends DialogFragment implements  View.OnClickListener{
  private Button closeBtn;
  private TextView infocomptable;
  private TextView ancientcredit;
  private TextView noveaucredit;
  private TextView derniermontant;
  private TextView montanttotal;
  private TextView evenement;
  private  TextView date;
 private  VersementItem versementItem;
  public DetailsVersementDialog(VersementItem versementItem){
      this.versementItem = versementItem;
  }
    public static DetailsVersementDialog newInstance(String title, VersementItem versementItem){
    DetailsVersementDialog detailsVersementDialog =new DetailsVersementDialog(versementItem);
        Bundle args = new Bundle();
        args.putString("title", title);
        return  detailsVersementDialog;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_versement_dialog, container,false);
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
        NumberFormat nf = NumberFormat.getInstance();


        super.onViewCreated(view, savedInstanceState);
        infocomptable = (TextView)view.findViewById(R.id.infocomptable);
        ancientcredit =(TextView)view.findViewById(R.id.ancientcredit);
        noveaucredit =(TextView)view.findViewById(R.id.noveaucredit);
        derniermontant =(TextView)view.findViewById(R.id.montantpaye);
        montanttotal = (TextView)view.findViewById(R.id.montanttotal);
        evenement =(TextView)view.findViewById(R.id.evenement);
        date =(TextView)view.findViewById(R.id.date);
        closeBtn  = (Button)view.findViewById(R.id.fermer);
        closeBtn.setOnClickListener(this);
        infocomptable.setText(versementItem.getComptableFirstName()+" "+versementItem.getComptableLastName());
       // ancientcredit.setText(versementItem.getAncientCredit()+" DA");

        double AncienCredit= versementItem.getAncientCredit();
        AncienCredit=(double)((int)(AncienCredit*100))/100;
        String AncienC = nf.format(AncienCredit);
        AncienC=AncienC.replace(","," ");
        ancientcredit.setText(AncienC+" DA");


        //noveaucredit.setText(versementItem.getNoveauCredit()+" DA");


        double NoveauCredit= versementItem.getNoveauCredit();
        NoveauCredit=(double)((int)(NoveauCredit*100))/100;
        String NouveauC = nf.format(NoveauCredit);
        NouveauC=NouveauC.replace(","," ");
        noveaucredit.setText(NouveauC+" DA");



       // derniermontant.setText(versementItem.getDernierVersement()+" DA");

        double DernierVersement= versementItem.getDernierVersement();
        DernierVersement=(double)((int)(DernierVersement*100))/100;
        String DernierV = nf.format(DernierVersement);
        DernierV=DernierV.replace(","," ");
        derniermontant.setText(DernierV+" DA");




        //montanttotal.setText(versementItem.getVersementTotal()+" DA");

        double VersementTotal= versementItem.getVersementTotal();
        VersementTotal=(double)((int)(VersementTotal*100))/100;
        String VersementT= nf.format(DernierVersement);
        VersementT=VersementT.replace(","," ");
        montanttotal.setText(VersementT+" DA");





        if(versementItem.getAction().equals("versement")){
            evenement.setText("Versement de credit");
        }else{
            evenement.setText("Modification de credit");

        }
        date.setText(versementItem.getCreatedDate());

    }
    @Override
    public void onClick(View v) {
                switch (v.getId()){
                    case R.id.fermer:
                                    getDialog().dismiss();
                        break;


                    default:
                        break;
                }
    }
}
