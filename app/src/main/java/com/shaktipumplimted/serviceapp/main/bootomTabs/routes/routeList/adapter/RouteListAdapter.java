package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.routeList.adapter;

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
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.routeList.model.RouteListModel;

import java.util.ArrayList;
import java.util.List;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder>implements Filterable {
    Context mContext;
    private List<RouteListModel> searchList;
    private List<RouteListModel> RouteArraylist;
    TextView noDataFound;
    private static ItemClickListener itemClickListener;


    public RouteListAdapter(Context context, List<RouteListModel> listdata, TextView noDataFound) {
        this.mContext = context;
        this.RouteArraylist = listdata;
        this.noDataFound = noDataFound;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.route_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final RouteListModel response = RouteArraylist.get(position);

        holder.routeNameTxt.setText(response.getRouteName());


        holder.routeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.SetOnItemClickListener(response,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return RouteArraylist.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView routeNameTxt;
        private CardView routeItem;
        public ViewHolder(View itemView) {
            super(itemView);
            routeNameTxt = itemView.findViewById(R.id.routeNameTxt);
            routeItem = itemView.findViewById(R.id.routeItem);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(RouteListModel response, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    RouteArraylist = searchList;
                } else {
                    List<RouteListModel> filteredList = new ArrayList<>();
                    for (RouteListModel row : searchList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getRouteName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    RouteArraylist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = RouteArraylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                RouteArraylist = (ArrayList<RouteListModel>) filterResults.values;
                if (RouteArraylist.size() > 0) {
                    noDataFound.setVisibility(View.GONE);
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }

}
