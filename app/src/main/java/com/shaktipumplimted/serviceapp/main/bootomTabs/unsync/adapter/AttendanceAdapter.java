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
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    Context mContext;
    private List<MarkAttendanceModel> searchList;
    private List<MarkAttendanceModel> attendanceArrayList;
    private static ItemClickListener itemClickListener;


    public AttendanceAdapter(Context context, List<MarkAttendanceModel> listdata) {
        this.mContext = context;
        this.attendanceArrayList = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mark_attendance_item, parent, false);
        return new AttendanceAdapter.ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(AttendanceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MarkAttendanceModel response = attendanceArrayList.get(position);

        holder.attendanceDateTxt.setText(response.getAttendanceDate());
        if(response.getAttendanceStatus().equals(Constant.attendanceIN)) {
            holder.attendanceInTimeTxt.setText(response.getAttendanceTime());
            holder.attendanceOutLinear.setVisibility(View.GONE);
        }
        if(response.getAttendanceStatus().equals(Constant.attendanceOut)) {
            holder.attendanceOutTimeTxt.setText(response.getAttendanceTime());
            holder.attendanceInLinear.setVisibility(View.GONE);
        }

        holder.attendanceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.SetOnItemClickListener(response,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return attendanceArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView attendanceDateTxt,attendanceInTimeTxt,attendanceOutTimeTxt;
        private CardView attendanceItem;
        LinearLayout attendanceInLinear,attendanceOutLinear,sync_linear;
        public ViewHolder(View itemView) {
            super(itemView);
            attendanceItem = itemView.findViewById(R.id.attendanceItem);
            attendanceDateTxt = itemView.findViewById(R.id.attendanceDateTxt);
            attendanceInTimeTxt = itemView.findViewById(R.id.attendanceInTimeTxt);
            attendanceOutTimeTxt = itemView.findViewById(R.id.attendanceOutTimeTxt);
            attendanceInLinear = itemView.findViewById(R.id.attendanceInLinear);
            attendanceOutLinear = itemView.findViewById(R.id.attendanceOutLinear);
            sync_linear = itemView.findViewById(R.id.sync_linear);
            sync_linear.setVisibility(View.VISIBLE);
        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(MarkAttendanceModel response, int position);
    }
}
