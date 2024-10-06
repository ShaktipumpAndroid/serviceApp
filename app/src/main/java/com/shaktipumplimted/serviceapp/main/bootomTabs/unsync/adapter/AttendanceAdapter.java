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
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.AttendanceDataModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;

import org.json.JSONException;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    Context mContext;
    private List<MarkAttendanceModel> attendanceModelList;
    private static ItemClickListener itemClickListener;

    public AttendanceAdapter(Context mContext, List<MarkAttendanceModel> dsrList) {
        this.mContext = mContext;
        this.attendanceModelList = dsrList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.dsr_item, parent, false);
        return new AttendanceAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        MarkAttendanceModel markAttendanceModel = attendanceModelList.get(position);
        holder.dateTxt.setText(Utility.getFormattedDate("yyyyMMdd","dd/MM/yyyy",markAttendanceModel.getAttendanceDate()));
//        holder.dsrActivityTxt.setText(markAttendanceModel.getAttendanceStatus());
//        holder.dsrOutcomeTxt.setText(dsrDetailsModel.getDsrOutcome());
//        holder.dsrAgendaTxt.setText(dsrDetailsModel.getDsrPurpose());

        holder.dsr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    itemClickListener.SetOnItemClickListener(markAttendanceModel,position);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendanceModelList.size();
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
        void SetOnItemClickListener(MarkAttendanceModel response, int position) throws JSONException;
    }


}
