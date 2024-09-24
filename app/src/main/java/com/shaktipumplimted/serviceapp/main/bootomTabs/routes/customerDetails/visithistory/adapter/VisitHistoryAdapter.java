package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.model.CompForwardListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory.model.VisitHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class VisitHistoryAdapter extends RecyclerView.Adapter<VisitHistoryAdapter.ViewHolder> {
    Context mContext;
    private List<VisitHistoryModel> searchList;
    private List<VisitHistoryModel> CompForwardPersonList;
    private static ItemClickListener itemClickListener;


    public VisitHistoryAdapter(Context context, List<VisitHistoryModel> listdata) {
        this.mContext = context;
        this.CompForwardPersonList = listdata;

        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.visit_history_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final VisitHistoryModel response = CompForwardPersonList.get(position);

        holder.visitedPersonNameTxt.setText(response.getName());
        holder.visitedDateTxt.setText(response.getVisitDate());
        holder.descriptionTxt.setText(response.getDescription());

    }


    @Override
    public int getItemCount() {
        return CompForwardPersonList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView visitedPersonNameTxt,visitedDateTxt,descriptionTxt;
        private LinearLayout visitedItem;
        public ViewHolder(View itemView) {
            super(itemView);
            visitedItem = itemView.findViewById(R.id.visitedItem);
            visitedPersonNameTxt = itemView.findViewById(R.id.visitedPersonNameTxt);
            visitedDateTxt = itemView.findViewById(R.id.visitedDateTxt);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(CompForwardListModel response, int position);
    }



}
