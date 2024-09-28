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
    private List<PendingReasonListModel.Data.ComplainAction> searchList;
    private List<PendingReasonListModel.Data.ComplainAction> PendingReasonList;
    private static ItemClickListener itemClickListener;


    public PendingReasonAdapter(Context context, List<PendingReasonListModel.Data.ComplainAction> listdata) {
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
        final PendingReasonListModel.Data.ComplainAction response = PendingReasonList.get(position);

        holder.dateTxt.setText(response.getAedtm());
        holder.followUpDateTxt.setText(response.getFdate());
        holder.employeeTxt.setText(response.getEname());
        holder.remarkTxt.setText(response.getAction());
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
        void SetOnItemClickListener(PendingReasonListModel.Data.ComplainAction response, int position);
    }

}
