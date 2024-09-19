package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList.model.CustomerListModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder>implements Filterable {
    Context mContext;
    private List<CustomerListModel> searchList;
    private List<CustomerListModel> CustomerList;
    TextView noDataFound;
    private static ItemClickListener itemClickListener;


    public CustomerListAdapter(Context context, List<CustomerListModel> listdata, TextView noDataFound) {
        this.mContext = context;
        this.CustomerList = listdata;
        this.noDataFound = noDataFound;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.customer_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final CustomerListModel response = CustomerList.get(position);

        holder.customerNameTxt.setText(response.getCustomerName());
        holder.customerAddressTxt.setText(response.getCustomerAddress());
        holder.customerMobileTxt.setText(response.getCustomerMobile());


        holder.customerListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.SetOnItemClickListener(response,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return CustomerList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView customerNameTxt,customerAddressTxt,customerMobileTxt;
        private CardView customerListItem;
        public ViewHolder(View itemView) {
            super(itemView);
            customerListItem = itemView.findViewById(R.id.customerListItem);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            customerAddressTxt = itemView.findViewById(R.id.customerAddressTxt);
            customerMobileTxt = itemView.findViewById(R.id.customerMobileTxt);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(CustomerListModel response, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    CustomerList = searchList;
                } else {
                    List<CustomerListModel> filteredList = new ArrayList<>();
                    for (CustomerListModel row : searchList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())||
                                row.getCustomerMobile().toLowerCase().contains(charString.toLowerCase())||
                                row.getCustomerEmail().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    CustomerList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = CustomerList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                CustomerList = (ArrayList<CustomerListModel>) filterResults.values;
                if (CustomerList.size() > 0) {
                    noDataFound.setVisibility(View.GONE);
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }

}
