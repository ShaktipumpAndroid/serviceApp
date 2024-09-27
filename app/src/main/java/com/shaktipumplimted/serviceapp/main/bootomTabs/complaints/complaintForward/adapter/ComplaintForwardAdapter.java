package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class ComplaintForwardAdapter extends RecyclerView.Adapter<ComplaintForwardAdapter.ViewHolder>implements Filterable {
    Context mContext;
    private List<CompForwardListModel.Response> searchList;
    private List<CompForwardListModel.Response> CompForwardPersonList;
    TextView noDataFound;
    private static ItemClickListener itemClickListener;


    public ComplaintForwardAdapter(Context context, List<CompForwardListModel.Response> listdata, TextView noDataFound) {
        this.mContext = context;
        this.CompForwardPersonList = listdata;
        this.noDataFound = noDataFound;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.complaint_forward_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CompForwardListModel.Response response = CompForwardPersonList.get(position);

        if(response.getIsSelected().equals("01")){
            holder.codeTitleTxt.setText(mContext.getResources().getString(R.string.service_center_code));
            holder.NameTitleTxt.setText(mContext.getResources().getString(R.string.service_center_name));
        }else if(response.getIsSelected().equals("02")){
            holder.codeTitleTxt.setText(mContext.getResources().getString(R.string.solar_installer_partner_code));
            holder.NameTitleTxt.setText(mContext.getResources().getString(R.string.solar_installer_partner_name));
        }else if(response.getIsSelected().equals("03")){
            holder.codeTitleTxt.setText(mContext.getResources().getString(R.string.freelancer_code));
            holder.NameTitleTxt.setText(mContext.getResources().getString(R.string.freelancer_name));
        }
        holder.codeTxt.setText(response.getPartnerCode());
        holder.nameTxt.setText(response.getPartnerName());

        holder.compForwardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.SetOnItemClickListener(response,position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return CompForwardPersonList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView codeTitleTxt,codeTxt,NameTitleTxt,nameTxt;
        private LinearLayout compForwardItem;
        public ViewHolder(View itemView) {
            super(itemView);
            compForwardItem = itemView.findViewById(R.id.compForwardItem);
            codeTitleTxt = itemView.findViewById(R.id.codeTitleTxt);
            codeTxt = itemView.findViewById(R.id.codeTxt);
            NameTitleTxt = itemView.findViewById(R.id.NameTitleTxt);
            nameTxt = itemView.findViewById(R.id.nameTxt);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(CompForwardListModel.Response response, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    CompForwardPersonList = searchList;
                } else {
                    List<CompForwardListModel.Response> filteredList = new ArrayList<>();
                    for (CompForwardListModel.Response row : searchList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPartnerCode().toLowerCase().contains(charString.toLowerCase()) || row.getPartnerName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    CompForwardPersonList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = CompForwardPersonList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                CompForwardPersonList = (ArrayList<CompForwardListModel.Response>) filterResults.values;
                if (CompForwardPersonList.size() > 0) {
                    noDataFound.setVisibility(View.GONE);
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }

}
