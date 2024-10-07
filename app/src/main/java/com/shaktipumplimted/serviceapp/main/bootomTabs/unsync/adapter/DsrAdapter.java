package com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDetailsModel;

import org.json.JSONException;

import java.util.List;

public class DsrAdapter extends RecyclerView.Adapter<DsrAdapter.ViewHolder> {

    Context mContext;
    private List<DsrDetailsModel> dsrList;
    private static ItemClickListener itemClickListener;

    public DsrAdapter(Context mContext, List<DsrDetailsModel> dsrList) {
        this.mContext = mContext;
        this.dsrList = dsrList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.dsr_item, parent, false);
        return new DsrAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DsrDetailsModel dsrDetailsModel = dsrList.get(position);
        holder.dateTxt.setText(Utility.getFormattedDate("yyyyMMdd","dd/MM/yyyy",dsrDetailsModel.getDate()));
        holder.dsrActivityTxt.setText(dsrDetailsModel.getDsrActivity());
        holder.dsrOutcomeTxt.setText(dsrDetailsModel.getDsrOutcome());
        holder.dsrAgendaTxt.setText(dsrDetailsModel.getDsrPurpose());

        holder.dsr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    itemClickListener.SetOnItemClickListener(dsrDetailsModel,position);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dsrActivityTxt, dsrOutcomeTxt, dsrAgendaTxt, dateTxt;
        private CardView dsr_item;

        public ViewHolder(View itemView) {
            super(itemView);
            dsr_item = itemView.findViewById(R.id.dsr_item);
            dsrActivityTxt = itemView.findViewById(R.id.dsrActivityTxt);
            dsrOutcomeTxt = itemView.findViewById(R.id.dsrOutcomeTxt);
            dsrAgendaTxt = itemView.findViewById(R.id.dsrAgendaTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(DsrDetailsModel response, int position) throws JSONException;
    }


}
