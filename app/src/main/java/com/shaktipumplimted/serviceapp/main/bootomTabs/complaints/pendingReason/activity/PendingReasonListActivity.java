package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.adapter.PendingReasonAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model.PendingReasonListModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingReasonListActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView pendingReasonList;

    List<PendingReasonListModel.Data.ComplainAction>pendingReasonArrayList;
    PendingReasonAdapter pendingReasonAdapter;
    TextView noDataFound;
    FloatingActionButton addPendingReasonBtn;
    SwipeRefreshLayout pullToRefresh;

    ComplaintListModel.Datum  complaintListModel;

    APIInterface apiInterface;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_reason_list);

        Init();
        listner();
        retrieveValue();
    }




    private void Init() {
        apiInterface = APIClient.getRetrofit(this).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        pendingReasonList = findViewById(R.id.pendingReasonList);
        noDataFound = findViewById(R.id.noDataFound);
        addPendingReasonBtn = findViewById(R.id.addPendingReasonBtn);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingReasonList));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


    }

    private void retrieveValue() {
        if(getIntent().getExtras()!=null){
            complaintListModel = (ComplaintListModel.Datum) getIntent().getSerializableExtra(Constant.complaintData);
            if(Utility.isInternetOn(getApplicationContext())) {
                getList();
            }else {
                setAdapter();
                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
            }
        }
    }

    private void getList() {
        pendingReasonArrayList = new ArrayList<>();

        Utility.showProgressDialogue(this);
        Call<PendingReasonListModel> call3 = apiInterface.getPendingReasonHistory(Utility.getSharedPreferences(this, Constant.accessToken),complaintListModel.getCmpno());
        call3.enqueue(new Callback<PendingReasonListModel>() {
            @Override
            public void onResponse(@NonNull Call<PendingReasonListModel> call, @NonNull Response<PendingReasonListModel> response) {
                Utility.hideProgressDialogue();
                pullToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    PendingReasonListModel pendingReasonListModel = response.body();

                    if (pendingReasonListModel.getStatus().equals(Constant.TRUE)) {


                        if (pendingReasonListModel.getData().getComplainAction().size() > 0) {

                            pendingReasonArrayList = pendingReasonListModel.getData().getComplainAction();

                        }

                        setAdapter();

                    }else if (pendingReasonListModel.getStatus().equals(Constant.FALSE)){
                        pendingReasonList.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }else if (pendingReasonListModel.getStatus().equals(Constant.FAILED)){
                        Utility.logout(getApplicationContext());

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<PendingReasonListModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                pullToRefresh.setRefreshing(false);
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setAdapter() {
        pullToRefresh.setRefreshing(false);
        if(pendingReasonArrayList.size()>0) {
            pendingReasonAdapter = new PendingReasonAdapter(this, pendingReasonArrayList);
            pendingReasonList.setHasFixedSize(true);
            pendingReasonList.setAdapter(pendingReasonAdapter);
            pendingReasonList.setVisibility(View.VISIBLE);
            noDataFound.setVisibility(View.GONE);
        }else {
            pendingReasonList.setVisibility(View.GONE);
            noDataFound.setVisibility(View.VISIBLE);

        }
    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        pullToRefresh.setOnRefreshListener(() -> {
            if(Utility.isInternetOn(getApplicationContext())) {
                getList();
                pullToRefresh.setRefreshing(true);
            }else {
                setAdapter();
                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
            }
        });

        addPendingReasonBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),AddPendingReasonActivity.class);
            intent.putExtra(Constant.complaintData,complaintListModel);
            startActivity(intent);
        });
    }
}