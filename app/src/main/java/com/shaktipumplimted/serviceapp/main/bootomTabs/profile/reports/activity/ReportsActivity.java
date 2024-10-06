package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.MarkAttendanceActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports.GatePassReportActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.LocalConveyanceReportActivity;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    LinearLayout localConveyanceLinear,gatePassLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        inIt();
        listner();
    }

    private void inIt() {
        toolbar = findViewById(R.id.toolbar);
        localConveyanceLinear = findViewById(R.id.localConveyanceLinear);
        gatePassLinear = findViewById(R.id.gatePassLinear);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.reports));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

    }

    private void listner() {
        localConveyanceLinear.setOnClickListener(this);
        gatePassLinear.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.localConveyanceLinear:
                Intent intent = new Intent(getApplicationContext(), LocalConveyanceReportActivity.class);
                startActivity(intent);

                break;
            case R.id.gatePassLinear:
                Intent intent1 = new Intent(getApplicationContext(), GatePassReportActivity.class);
                startActivity(intent1);

                break;
        }
    }
}