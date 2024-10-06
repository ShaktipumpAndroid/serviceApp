package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.model.LocalConveyanceReportModel;

import java.util.ArrayList;
import java.util.List;

public class LocalConveyanceReportAdapter extends RecyclerView.Adapter<LocalConveyanceReportAdapter.ViewHolder> {
    Context mContext;
    private List<LocalConveyanceReportModel.Response> searchList;
    private List<LocalConveyanceReportModel.Response> travelList;
    private static ItemClickListener itemClickListener;


    public LocalConveyanceReportAdapter(Context context, List<LocalConveyanceReportModel.Response> listdata) {
        this.mContext = context;
        this.travelList = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.local_conveyance_report_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final LocalConveyanceReportModel.Response response = travelList.get(position);

        holder.startDateTxt.setText(":- "+ Utility.getFormattedDate("dd.MM.yyyy","dd MMM yyyy",response.getBegda())+" "+
                Utility.getFormattedTime("HH:mm:ss","hh:mm a",response.getStartTime()));
        holder.endDateTxt.setText(":- "+ Utility.getFormattedDate("dd.MM.yyyy","dd MMM yyyy",response.getEndda())+" "+
                Utility.getFormattedTime("HH:mm:ss","hh:mm a",response.getEndTime()));
        holder.startAddressTxt.setText(":- "+response.getStartLocation());
        holder.endAddressTxt.setText(":- "+response.getEndLocation());
        holder.travelModeTxt.setText(":- "+response.getTravelMode());
        holder.distanceTxt.setText(":- "+response.getDistance()+" km");

     }


    @Override
    public int getItemCount() {
        return travelList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView startDateTxt,endDateTxt,startAddressTxt,endAddressTxt,travelModeTxt,distanceTxt;
        private CardView travelItem;
        public ViewHolder(View itemView) {
            super(itemView);
            travelItem = itemView.findViewById(R.id.travelItem);
            startDateTxt = itemView.findViewById(R.id.startDateTxt);
            endDateTxt = itemView.findViewById(R.id.endDateTxt);
            startAddressTxt = itemView.findViewById(R.id.startAddressTxt);
            endAddressTxt = itemView.findViewById(R.id.endAddressTxt);
            travelModeTxt = itemView.findViewById(R.id.travelModeTxt);
            distanceTxt = itemView.findViewById(R.id.distanceTxt);
        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(LocalConveyanceReportModel response, int position);
    }
}
