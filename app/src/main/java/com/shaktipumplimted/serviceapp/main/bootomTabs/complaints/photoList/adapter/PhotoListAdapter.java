package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.model.PhotoListModel;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder>{
    Context mContext;
    private List<PhotoListModel.Response> searchList;
    private List<PhotoListModel.Response> complaintStatusArrayList;
    private static ItemClickListener itemClickListener;


    public PhotoListAdapter(Context context, List<PhotoListModel.Response> listdata) {
        this.complaintStatusArrayList = listdata;
        this.mContext = context;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.complaint_photo_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PhotoListModel.Response response = complaintStatusArrayList.get(position);

        Glide.with(mContext)
                .load(Utility.getBitmapFromBase64(response.getImage1()))
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.complaintImg);

        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.SetOnItemClickListener(response,position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return complaintStatusArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView complaintImg;
        RelativeLayout imgItem;
        public ViewHolder(View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            complaintImg = itemView.findViewById(R.id.complaintImg);

        }
    }

    public void ItemClick(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void SetOnItemClickListener(PhotoListModel.Response response, int position);
    }
}
