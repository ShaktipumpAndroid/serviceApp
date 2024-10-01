package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.PhotoViewerActivity;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.adapter.PhotoListAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.model.PhotoListModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintPhotoListActivity extends AppCompatActivity implements PhotoListAdapter.ItemClickListener {

    Toolbar toolbar;
    RecyclerView photoList;
    List<PhotoListModel.Response>photoArrayList;

    PhotoListAdapter photoListAdapter;
    TextView noPhotoAvailable;
    SwipeRefreshLayout pullToRefresh;
    APIInterface apiInterface;
    ComplaintListModel.Datum compListModel;
    int lastPage = 1;
    private String mMaxoffset = "";

    NestedScrollView nestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_photo_list);

        Init();
        listner();
        retrieveValue();
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
        apiInterface = APIClient.getRetrofit(this).create(APIInterface.class);
        photoList = findViewById(R.id.photoList);
        noPhotoAvailable = findViewById(R.id.noPhotoAvailable);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        toolbar = findViewById(R.id.toolbar);
        nestedScrollView =  findViewById(R.id.nestedScrollView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complaintPhoto));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);




        if (nestedScrollView != null) {
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                        lastPage = lastPage + 1;
                        if (Integer.parseInt(mMaxoffset) >= lastPage) {
                            if (Utility.isInternetOn(getApplicationContext())) {
                                getList();
                            } else {

                                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
                            }
                        } else {
                            Utility.ShowToast( getResources().getString(R.string.no_more_data),getApplicationContext());
                        }
                    }
                }
            });

        }
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            compListModel = (ComplaintListModel.Datum) getIntent().getSerializableExtra(Constant.complaintData);
            if(Utility.isInternetOn(getApplicationContext())) {
                getList();
            }else {
                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
            }
        }
    }

    private void getList() {

        photoArrayList = new ArrayList<>();
        Utility.showProgressDialogue(this);
        Call<PhotoListModel> call3 = apiInterface.getComplaintPhotoList(Utility.getSharedPreferences(getApplicationContext(), Constant.accessToken),
                "GO0016", String.valueOf(lastPage),"000001");
        call3.enqueue(new Callback<PhotoListModel>() {
            @Override
            public void onResponse(@NonNull Call<PhotoListModel> call, @NonNull Response<PhotoListModel> response) {
                Log.e("url===>", String.valueOf(call.request().url()));
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    PhotoListModel photoListModel = response.body();
                    if (photoListModel.getStatus().equals(Constant.TRUE)) {
                        if(photoListModel.getResponse().size()>0) {
                            photoArrayList.addAll(photoListModel.getResponse());
                            pullToRefresh.setRefreshing(false);
                        }
                        mMaxoffset = photoListModel.getCount();
                        Log.e("mMaxoffset==>",mMaxoffset);
                      //  totalPage = Integer.parseInt(photoListModel.getCount());
                        setAdapter();
                    }else if (photoListModel.getStatus().equals(Constant.FAILED)){
                        Utility.logout(getApplicationContext());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoListModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setAdapter() {
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
    public void SetOnItemClickListener(PhotoListModel.Response response, int position) {
        Intent intent = new Intent(getApplicationContext(), PhotoViewerActivity.class);
        intent.putExtra(Constant.imagePath,response.getImage1());
        intent.putExtra(Constant.Images,"1");
        startActivity(intent);

    }
}