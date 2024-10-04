package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.adapter;

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
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.AllAttendanceRecordModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.util.ArrayList;
import java.util.List;

public class MarkAttendanceAdapter extends RecyclerView.Adapter<MarkAttendanceAdapter.ViewHolder> {
    Context mContext;
    private List<AllAttendanceRecordModel> searchList;
    private List<AllAttendanceRecordModel> attendanceArrayList;
    private static ItemClickListener itemClickListener;


    public MarkAttendanceAdapter(Context context, List<AllAttendanceRecordModel> listdata) {
        this.mContext = context;
        this.attendanceArrayList = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mark_attendance_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final AllAttendanceRecordModel response = attendanceArrayList.get(position);
        
        holder.attendanceDateTxt.setText(response.getAttendanceDate());
        holder.attendanceInTimeTxt.setText(response.getAttendanceInTime());
        holder.attendanceOutTimeTxt.setText(response.getAttendanceOutTime());

    }


    @Override
    public int getItemCount() {
        return attendanceArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView attendanceDateTxt,attendanceInTimeTxt,attendanceOutTimeTxt;
        private CardView attendanceItem;
        LinearLayout attendanceInLinear,attendanceOutLinear;
        public ViewHolder(View itemView) {
            super(itemView);
            attendanceItem = itemView.findViewById(R.id.attendanceItem);
            attendanceDateTxt = itemView.findViewById(R.id.attendanceDateTxt);
            attendanceInTimeTxt = itemView.findViewById(R.id.attendanceInTimeTxt);
            attendanceOutTimeTxt = itemView.findViewById(R.id.attendanceOutTimeTxt);
            attendanceInLinear = itemView.findViewById(R.id.attendanceInLinear);
            attendanceOutLinear = itemView.findViewById(R.id.attendanceOutLinear);
            
        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(AllAttendanceRecordModel response, int position);
    }
}
