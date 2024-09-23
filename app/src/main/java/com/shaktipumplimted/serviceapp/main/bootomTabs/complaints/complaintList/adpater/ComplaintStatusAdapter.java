package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintStatusAdapter  extends RecyclerView.Adapter<ComplaintStatusAdapter.ViewHolder>{
    Context mContext;
    private List<ComplaintStatusModel.Datum> searchList;
    private List<ComplaintStatusModel.Datum> complaintStatusArrayList;
    TextView noDataFound;
    private static ItemClickListener itemClickListener;


    public ComplaintStatusAdapter(Context context, List<ComplaintStatusModel.Datum> listdata) {
        this.complaintStatusArrayList = listdata;
        this.mContext = context;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.complaint_status_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ComplaintStatusModel.Datum response = complaintStatusArrayList.get(position);
        holder.statusTxt.setText(response.getDomvalueL());

        if(response.isSelected()){
            holder.compStatusCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
            holder.statusTxt.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.compStatusCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
            holder.statusTxt.setTextColor(mContext.getResources().getColor(R.color.grayish));
        }

        holder.compStatusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.SetOnItemClickListener(response,position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return complaintStatusArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTxt;
        private CardView compStatusCard;
        public ViewHolder(View itemView) {
            super(itemView);
            compStatusCard = itemView.findViewById(R.id.compStatusCard);
            statusTxt = itemView.findViewById(R.id.statusTxt);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(ComplaintStatusModel.Datum response, int position);
    }
}
