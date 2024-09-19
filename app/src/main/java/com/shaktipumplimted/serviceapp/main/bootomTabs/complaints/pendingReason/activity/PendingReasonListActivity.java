package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.adapter.PendingReasonAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model.PendingReasonListModel;

import java.util.ArrayList;
import java.util.List;

public class PendingReasonListActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView pendingReasonList;

    List<PendingReasonListModel>pendingReasonArrayList;
    PendingReasonAdapter pendingReasonAdapter;
    TextView noDataFound;
    FloatingActionButton addPendingReasonBtn;
    SwipeRefreshLayout pullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_reason_list);

        Init();
        listner();
    }



    private void Init() {
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

        if(Utility.isInternetOn(getApplicationContext())) {
            getList();
        }else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
        }
    }

    private void getList() {
        pendingReasonArrayList = new ArrayList<>();


        pendingReasonArrayList.add(new PendingReasonListModel("29.08.2024","30.08.2024","Shravan Patidar","This site inverter internal problem and after plan service center person"));
        pendingReasonArrayList.add(new PendingReasonListModel("05.09.2024","07.09.2024","Shravan Patidar","This site problem resolve but pending site image and service bill"));

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
            }else {
                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
            }
        });

        addPendingReasonBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),AddPendingReasonActivity.class);
            startActivity(intent);
        });
    }
}