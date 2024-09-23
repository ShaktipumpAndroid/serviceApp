package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.PhotoViewerActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.adapter.PhotoListAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.model.PhotoListModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintPhotoListActivity extends AppCompatActivity implements PhotoListAdapter.ItemClickListener {

    Toolbar toolbar;
    RecyclerView photoList;
    List<PhotoListModel>photoArrayList;

    PhotoListAdapter photoListAdapter;
    TextView noPhotoAvailable;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_photo_list);

        Init();
        listner();
    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        pullToRefresh.setOnRefreshListener(() -> {
            if(Utility.isInternetOn(getApplicationContext())) {
                getList();
            }else {
                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
            }
        });
    }

    private void Init() {
        photoList = findViewById(R.id.photoList);
        noPhotoAvailable = findViewById(R.id.noPhotoAvailable);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complaintPhoto));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if(Utility.isInternetOn(getApplicationContext())) {
            getList();
        }else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
        }

    }

    private void getList() {
        photoArrayList = new ArrayList<>();
        photoArrayList.add(new PhotoListModel("01","https://fastly.picsum.photos/id/0/5000/3333.jpg?hmac=_j6ghY5fCfSD6tvtcV74zXivkJSPIfR9B8w34XeQmvU"));
        photoArrayList.add(new PhotoListModel("02","https://fastly.picsum.photos/id/16/2500/1667.jpg?hmac=uAkZwYc5phCRNFTrV_prJ_0rP0EdwJaZ4ctje2bY7aE"));
        photoArrayList.add(new PhotoListModel("03","https://fastly.picsum.photos/id/65/4912/3264.jpg?hmac=uq0IxYtPIqRKinGruj45KcPPzxDjQvErcxyS1tn7bG0"));
        photoArrayList.add(new PhotoListModel("04","https://fastly.picsum.photos/id/76/4912/3264.jpg?hmac=VkFcSa2Rbv0R0ndYnz_FAmw02ON1pPVjuF_iVKbiiV8"));

        if(photoArrayList.size()>0) {
            photoListAdapter = new PhotoListAdapter(this, photoArrayList);
            photoList.setHasFixedSize(true);
            photoList.setAdapter(photoListAdapter);
            photoListAdapter.ItemClick(this);
            noPhotoAvailable.setVisibility(View.GONE);
            photoList.setVisibility(View.VISIBLE);
        }else {
            noPhotoAvailable.setVisibility(View.VISIBLE);
            photoList.setVisibility(View.GONE);
        }
    }

    @Override
    public void SetOnItemClickListener(PhotoListModel response, int position) {
        Intent intent = new Intent(getApplicationContext(), PhotoViewerActivity.class);
        startActivity(intent);

    }
}