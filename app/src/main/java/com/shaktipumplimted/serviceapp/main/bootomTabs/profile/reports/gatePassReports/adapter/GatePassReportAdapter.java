package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports.adapter;

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
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports.model.GatePassReportModel;

import java.util.ArrayList;
import java.util.List;

public class GatePassReportAdapter extends RecyclerView.Adapter<GatePassReportAdapter.ViewHolder> {
    Context mContext;
    private List<GatePassReportModel.Response> searchList;
    private List<GatePassReportModel.Response> travelList;
    private static ItemClickListener itemClickListener;


    public GatePassReportAdapter(Context context, List<GatePassReportModel.Response> listdata) {
        this.mContext = context;
        this.travelList = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.gatepass_report_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final GatePassReportModel.Response response = travelList.get(position);

        holder.invoiceTxt.setText(":- "+ response.getChlnInv());
        holder.materialCodeTxt.setText(":- "+response.getMatnr());
        holder.materialNameTxt.setText(":- "+response.getMaktx());
        holder.quantityTxt.setText(":- "+response.getChQty());

     }


    @Override
    public int getItemCount() {
        return travelList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView invoiceTxt,materialCodeTxt,materialNameTxt,quantityTxt;
        private CardView gatePassItem;
        public ViewHolder(View itemView) {
            super(itemView);
            gatePassItem = itemView.findViewById(R.id.gatePassItem);
            invoiceTxt = itemView.findViewById(R.id.invoiceTxt);
            materialCodeTxt = itemView.findViewById(R.id.materialCodeTxt);
            materialNameTxt = itemView.findViewById(R.id.materialNameTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(GatePassReportModel response, int position);
    }
}
