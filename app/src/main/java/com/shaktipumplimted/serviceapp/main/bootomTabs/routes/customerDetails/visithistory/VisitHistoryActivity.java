package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory.adapter.VisitHistoryAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory.model.VisitHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class VisitHistoryActivity extends AppCompatActivity {

    RecyclerView visitHistoryList;
    Toolbar toolbar;

    List<VisitHistoryModel>visitHistoryArrayList;
    VisitHistoryAdapter visitHistoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_history);

        Init();
        listner();
        getVisitHistoryList();
    }




    private void Init() {
        visitHistoryArrayList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        visitHistoryList = findViewById(R.id.visitHistoryList);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.visitHistory));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

    }
    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getVisitHistoryList() {
        visitHistoryArrayList.add(new VisitHistoryModel("Kishan K M","24.09.2024","Visit"));
        visitHistoryArrayList.add(new VisitHistoryModel("Rahul Gupta","24.09.2024","Visit"));
        visitHistoryArrayList.add(new VisitHistoryModel("Contractor Emp Mukesh Kumar VishwaKarma","23.09.2024","Office work and customer complaint follow then close complaint gate pass old material checking at godown"));
        visitHistoryArrayList.add(new VisitHistoryModel("Amarnath Gupta","23.09.2024","Today visited at kanpur branch taken followup of pending complaints and resolve customer issue on field also coordinate with service center regarding customer complaints"));

        visitHistoryAdapter =  new VisitHistoryAdapter(getApplicationContext(),visitHistoryArrayList);
        visitHistoryList.setHasFixedSize(true);
        visitHistoryList.setAdapter(visitHistoryAdapter);

    }

}