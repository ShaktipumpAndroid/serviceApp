package com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;

import java.util.ArrayList;
import java.util.List;

public class UnSyncCompListAdapter extends RecyclerView.Adapter<UnSyncCompListAdapter.ViewHolder> {
    Context mContext;
    private List<ComplaintListModel.Datum> searchList;
    private List<ComplaintListModel.Datum> complaintArraylist;
    private static ItemClickListener itemClickListener;


    public UnSyncCompListAdapter(Context context, List<ComplaintListModel.Datum> listdata) {
        this.mContext = context;
        this.complaintArraylist = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.uncync_complaint_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ComplaintListModel.Datum response = complaintArraylist.get(position);

        holder.complaintNo.setText(response.getCmpno());
        holder.customerNameTxt.setText(response.getCstname());
        holder.mobileNoTxt.setText(response.getMblno());
        holder.addressTxt.setText(response.getCaddress());
        holder.complaintItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.SetOnItemClickListener(response,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return complaintArraylist.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView complaintNo,customerNameTxt,mobileNoTxt,addressTxt;
        private CardView complaintItem;
        LinearLayout sync_linear;
        public ViewHolder(View itemView) {
            super(itemView);
            complaintItem = itemView.findViewById(R.id.complaintItem);
            complaintNo = itemView.findViewById(R.id.complaintNo);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            mobileNoTxt = itemView.findViewById(R.id.mobileNoTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            sync_linear = itemView.findViewById(R.id.sync_linear);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(ComplaintListModel.Datum response, int position);
    }


}
