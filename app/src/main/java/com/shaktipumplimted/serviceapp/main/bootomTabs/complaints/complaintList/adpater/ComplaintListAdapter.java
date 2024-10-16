package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintListAdapter extends RecyclerView.Adapter<ComplaintListAdapter.ViewHolder>implements Filterable {
    Context mContext;
    private List<ComplaintListModel.Datum> searchList;
    private List<ComplaintListModel.Datum> complaintArraylist;
    TextView noDataFound;
    private static ItemClickListener itemClickListener;


    public ComplaintListAdapter(Context context, List<ComplaintListModel.Datum> listdata,TextView noDataFound) {
        this.mContext = context;
        this.complaintArraylist = listdata;
        this.noDataFound = noDataFound;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.complaint_list_item, parent, false);
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
        if(!response.getFdate().equals("00.00.0000")){
            holder.pendingDays.setText(mContext.getResources().getString(R.string.pending_days)+ Utility.differenceBetweenDays(response.getFdate()));
        }else{
            holder.pendingDays.setText(mContext.getResources().getString(R.string.pending_days)+"1");
        }
        holder.complaintStatusTxt.setText(response.getStatus());

       // Log.e("Beneficiary===>",response.getBeneficiary());
        if(response.getBeneficiary()!=null && !response.getBeneficiary().isEmpty()) {
            holder.beneficiaryTxt.setText(response.getBeneficiary());
        }else {
            holder.beneficiaryLinear.setVisibility(View.GONE);
        }

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

        private TextView complaintNo,customerNameTxt,mobileNoTxt,addressTxt,pendingDays,complaintStatusTxt,beneficiaryTxt;
        private CardView complaintItem;
        LinearLayout  beneficiaryLinear;
        public ViewHolder(View itemView) {
            super(itemView);
            complaintItem = itemView.findViewById(R.id.complaintItem);
            complaintNo = itemView.findViewById(R.id.complaintNo);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            mobileNoTxt = itemView.findViewById(R.id.mobileNoTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            pendingDays = itemView.findViewById(R.id.pendingDays);
            complaintStatusTxt = itemView.findViewById(R.id.complaintStatusTxt);
            beneficiaryTxt = itemView.findViewById(R.id.beneficiaryTxt);
            beneficiaryLinear = itemView.findViewById(R.id.beneficiaryLinear);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(ComplaintListModel.Datum response, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    complaintArraylist = searchList;
                } else {
                    List<ComplaintListModel.Datum> filteredList = new ArrayList<>();
                    for (ComplaintListModel.Datum row : searchList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCmpno().toLowerCase().contains(charString.toLowerCase()) || row.getCstname().toLowerCase().contains(charString.toLowerCase())
                                || row.getMblno().toLowerCase().contains(charString.toLowerCase()) || row.getStatus().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    complaintArraylist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = complaintArraylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                complaintArraylist = (ArrayList<ComplaintListModel.Datum>) filterResults.values;
                if (complaintArraylist.size() > 0) {
                    noDataFound.setVisibility(View.GONE);
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }

}
