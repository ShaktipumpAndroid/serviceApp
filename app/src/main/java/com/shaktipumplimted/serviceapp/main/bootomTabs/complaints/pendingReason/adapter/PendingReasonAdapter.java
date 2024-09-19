package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model.PendingReasonListModel;

import java.util.ArrayList;
import java.util.List;

public class PendingReasonAdapter extends RecyclerView.Adapter<PendingReasonAdapter.ViewHolder>{
    Context mContext;
    private List<PendingReasonListModel> searchList;
    private List<PendingReasonListModel> PendingReasonList;
    private static ItemClickListener itemClickListener;


    public PendingReasonAdapter(Context context, List<PendingReasonListModel> listdata) {
        this.mContext = context;
        this.PendingReasonList = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.pending_reason_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PendingReasonListModel response = PendingReasonList.get(position);

        holder.dateTxt.setText(response.getDate());
        holder.followUpDateTxt.setText(response.getFollowupDate());
        holder.employeeTxt.setText(response.getEmployee());
        holder.remarkTxt.setText(response.getRemark());
    }


    @Override
    public int getItemCount() {
        return PendingReasonList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dateTxt,followUpDateTxt,employeeTxt,remarkTxt;
        LinearLayout pendingReasonItem;
        public ViewHolder(View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            followUpDateTxt = itemView.findViewById(R.id.followUpDateTxt);
            employeeTxt = itemView.findViewById(R.id.employeeTxt);
            remarkTxt = itemView.findViewById(R.id.remarkTxt);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(PendingReasonListModel response, int position);
    }

}
