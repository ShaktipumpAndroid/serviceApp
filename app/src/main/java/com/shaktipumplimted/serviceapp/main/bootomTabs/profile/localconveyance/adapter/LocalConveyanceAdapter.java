package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.LocalConveyanceModel;

import java.util.ArrayList;
import java.util.List;

public class LocalConveyanceAdapter extends RecyclerView.Adapter<LocalConveyanceAdapter.ViewHolder> {
    Context mContext;
    private List<LocalConveyanceModel> searchList;
    private List<LocalConveyanceModel> CompForwardPersonList;
    private static ItemClickListener itemClickListener;


    public LocalConveyanceAdapter(Context context, List<LocalConveyanceModel> listdata) {
        this.mContext = context;
        this.CompForwardPersonList = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.local_conveyance_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final LocalConveyanceModel response = CompForwardPersonList.get(position);

        holder.startLatTxt.setText(":- "+response.getStartLatitude());
        holder.startLongTxt.setText(":- "+response.getStartLongitude());
        holder.endLatTxt.setText(":- "+response.getEndLatitude());
        holder.endLongTxt.setText(":- "+response.getEndLongitude());

        holder.localConveyanceItem.setOnClickListener(v -> itemClickListener.SetOnItemClickListener(response,position));
        holder.syncBtn.setOnClickListener(v -> itemClickListener.SetOnItemClickListener(response,position));
    }


    @Override
    public int getItemCount() {
        return CompForwardPersonList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView startLatTxt,startLongTxt,endLatTxt,endLongTxt;
        private CardView localConveyanceItem;
        private RelativeLayout syncBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            localConveyanceItem = itemView.findViewById(R.id.localConveyanceItem);
            startLatTxt = itemView.findViewById(R.id.startLatTxt);
            startLongTxt = itemView.findViewById(R.id.startLongTxt);
            endLatTxt = itemView.findViewById(R.id.endLatTxt);
            endLongTxt = itemView.findViewById(R.id.endLongTxt);
            syncBtn = itemView.findViewById(R.id.syncBtn);
        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(LocalConveyanceModel response, int position);
    }
}
