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
import android.widget.TextView;

import com.casbaherpapp.myapplication.R;
import com.casbaherpapp.myapplication.imad.Dialog.DetailsVersementDialog;
import com.casbaherpapp.myapplication.imad.Entities.VersementItem;

import java.util.ArrayList;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoriqueVersementAdapter extends RecyclerView.Adapter<HistoriqueVersementAdapter.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;
    private Context context;
    private FragmentManager fm;
    private ArrayList<VersementItem> versementItems;
    public HistoriqueVersementAdapter(Context context,FragmentManager fm,ArrayList<VersementItem> versementItems){
        this.mInflater = LayoutInflater.from(context);
        this.versementItems = versementItems;
        this.fm=fm;
    }

    @Override
    public HistoriqueVersementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.historiqueversement_layout, parent, false);
        return new HistoriqueVersementAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull HistoriqueVersementAdapter.ViewHolder holder, int position) {

                if(getVersementItems().get(position).getAction().equals("versement")){
                    holder.actionImageView.setBackgroundResource(R.drawable.mantoman);
                    holder.eventName.setText("Versement");
                    holder.eventDescription.setText("Vous avez payé "+getVersementItems().get(position).getDernierVersement()+" DA à monsieur le comptable "+getVersementItems().get(position).getComptableFirstName()+" "+getVersementItems().get(position).getComptableLastName());
                    holder.createdDate.setText("Le "+getVersementItems().get(position).getCreatedDate());

                }else{
                    holder.actionImageView.setBackgroundResource(R.drawable.editcredit);
                    holder.eventName.setText("Modification de crédit");
                    holder.eventDescription.setText("Monsieur le comptable "+getVersementItems().get(position).getComptableFirstName()+" "+getVersementItems().get(position).getComptableLastName()+" a modifié votre crédit");
                    holder.createdDate.setText("Le "+getVersementItems().get(position).getCreatedDate());
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
     actionImageView =(ImageView)itemView.findViewById(R.id.actionImageView);
           createdDate =(TextView) itemView.findViewById(R.id.createddate);
            eventDescription =(TextView)itemView.findViewById(R.id.description);
            eventName=(TextView)itemView.findViewById(R.id.event);
            detail =(Button)itemView.findViewById(R.id.detail);
            detail.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.detail:

                    DetailsVersementDialog detailsVersementDialog = DetailsVersementDialog.newInstance("title",versementItems.get(getAdapterPosition()));
                    detailsVersementDialog.show(getFm(),"title");
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

