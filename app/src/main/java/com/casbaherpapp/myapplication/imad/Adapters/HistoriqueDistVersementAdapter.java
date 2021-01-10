package com.casbaherpapp.myapplication.imad.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Dialog.DetailsVersementDialog;
import com.casbaherpapp.myapplication.imad.Entities.VersementItem;
import com.casbaherpapp.myapplication.imad.Listerners.ProgressLoaderListener;

import java.util.ArrayList;

public class HistoriqueDistVersementAdapter extends RecyclerView.Adapter<HistoriqueDistVersementAdapter.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private Context context;
    private FragmentManager fm;
    private ArrayList<VersementItem> versementItems;
    private int idDist;
private ProgressLoaderListener progressLoaderListener;
    public HistoriqueDistVersementAdapter(Context context, FragmentManager fm, ArrayList<VersementItem> versementItems, int idDist, ProgressLoaderListener progressLoaderListener){
        this.mInflater = LayoutInflater.from(context);
        this.versementItems = versementItems;
        this.fm=fm;
        this.context = context;
     this.progressLoaderListener = progressLoaderListener;
     this.idDist = idDist;
    }

    @Override
    public HistoriqueDistVersementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.historiqueversement_layout, parent, false);
        return new HistoriqueDistVersementAdapter.ViewHolder(view);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriqueDistVersementAdapter.ViewHolder holder, int position) {
        holder.idEvent = getVersementItems().get(position).getId();
  holder.handshakeYes.setVisibility(View.GONE);
holder.handShakeNon.setVisibility(View.GONE);
   holder.handshakeYes.setBackgroundResource(R.drawable.ic_hand_shake_yes);
        holder.handShakeNon.setBackgroundResource(R.drawable.ic_hand_shake_no);
            Log.e("state", String.valueOf(getVersementItems().get(position).getValideState()));
                if(getVersementItems().get(position).getAction().equals("versement")){
                    holder.actionImageView.setBackgroundResource(R.drawable.mantoman);
                    holder.eventName.setText("Versement");
                    holder.eventDescription.setText("Vous avez payé "+getVersementItems().get(position).getDernierVersement()+" DA à monsieur le comptable "+getVersementItems().get(position).getComptableFirstName()+" "+getVersementItems().get(position).getComptableLastName());
                    holder.createdDate.setText("Le "+getVersementItems().get(position).getCreatedDate());

                    if(getVersementItems().get(position).getValideState()==0){

                    }else if(getVersementItems().get(position).getValideState()==1){
                        holder.validationLayout.setVisibility(View.GONE);
                        holder.handshakeYes.setVisibility(View.VISIBLE);
                    }else if (getVersementItems().get(position).getValideState()==2){
                        holder.validationLayout.setVisibility(View.GONE);
                        holder.handShakeNon.setVisibility(View.VISIBLE);
                    }

                }else{
                    holder.actionImageView.setBackgroundResource(R.drawable.editcredit);
                    holder.eventName.setText("Modification de crédit");
                    holder.eventDescription.setText("Monsieur le comptable "+getVersementItems().get(position).getComptableFirstName()+" "+getVersementItems().get(position).getComptableLastName()+" a modifié votre crédit");
                    holder.createdDate.setText("Le "+getVersementItems().get(position).getCreatedDate());
                    holder.validationLayout.setVisibility(View.GONE);
                }


    }

    @Override
    public int getItemCount() {
        return versementItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private ImageView actionImageView;
  private TextView createdDate;
  private TextView eventDescription;
  private TextView eventName;
  private  Button detail;
  private Button ouiBtn;
  private Button nonBtn;
  private LinearLayout validationLayout;
  private ImageView handshakeYes;
  private ImageView handShakeNon;
private  int idEvent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
     actionImageView =(ImageView)itemView.findViewById(R.id.actionImageView);
           createdDate =(TextView) itemView.findViewById(R.id.createddate);
            eventDescription =(TextView)itemView.findViewById(R.id.description);
            eventName=(TextView)itemView.findViewById(R.id.event);
            detail =(Button)itemView.findViewById(R.id.detail);
            detail.setOnClickListener(this);
            validationLayout = (LinearLayout)itemView.findViewById(R.id.validationLayout);
            ouiBtn = (Button)itemView.findViewById(R.id.ouiBtn);
            nonBtn = (Button)itemView.findViewById(R.id.nonBtn);
            ouiBtn.setOnClickListener(this);
            nonBtn.setOnClickListener(this);
            handshakeYes = (ImageView)itemView.findViewById(R.id.handshakeyes);
            handShakeNon = (ImageView)itemView.findViewById(R.id.handshakenon);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.detail:

                    DetailsVersementDialog detailsVersementDialog = DetailsVersementDialog.newInstance("title",versementItems.get(getAdapterPosition()));
                    detailsVersementDialog.show(getFm(),"title");
                    break;

                case R.id.ouiBtn:


                   progressLoaderListener.showProgress();
                   AndroidNetworking.post("http://www.casbahdz.com/adm/CRUD1.php")
                            .addBodyParameter("action","valider versement distributeur").addBodyParameter("id_event", String.valueOf(idEvent))
                            .addBodyParameter("id_dist", String.valueOf(idDist))
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                 Log.e("string",response);
                                    validationLayout.setVisibility(View.GONE);
                                    handshakeYes.setVisibility(View.VISIBLE);
                                    progressLoaderListener.closeProgress();
                                }
                                @Override
                                public void onError(ANError anError) {
                                     Log.e("error",anError.getMessage());
                                    progressLoaderListener.closeProgress();
                                }
                            });
                    break;
                case R.id.nonBtn:
                    progressLoaderListener.showProgress();
                    AndroidNetworking.post("http://www.casbahdz.com/adm/CRUD1.php")
                            .addBodyParameter("action","refuser versement distributeur").addBodyParameter("id_event", String.valueOf(idEvent))
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {

                                    validationLayout.setVisibility(View.GONE);
                                    handShakeNon.setVisibility(View.VISIBLE);
                                    progressLoaderListener.closeProgress();
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("error",anError.getMessage());
                                    progressLoaderListener.closeProgress();
                                }
                            });
                    break;
                default:
                    break;
            }

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<VersementItem> getVersementItems() {
        return versementItems;
    }

    public void setVersementItems(ArrayList<VersementItem> versementItems) {
        this.versementItems = versementItems;
    }
}

