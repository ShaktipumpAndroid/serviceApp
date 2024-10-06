package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports.adapter.GatePassReportAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports.model.GatePassReportModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.adapter.LocalConveyanceReportAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.model.LocalConveyanceReportModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GatePassReportActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView noDataFound;
    APIInterface apiInterface;
    RecyclerView gatePassReportList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass_report);

        inIt();
        listner();
    }
    private void inIt() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        toolbar = findViewById(R.id.toolbar);
        gatePassReportList = findViewById(R.id.gatePassReportList);
        noDataFound = findViewById(R.id.noDataFound);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.gatePassReport));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if (Utility.isInternetOn(getApplicationContext())) {
            getGatePassReport();
        } else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
        }
    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getGatePassReport() {

        Utility.showProgressDialogue(this);
        Call<GatePassReportModel> call3 = apiInterface.getGatePassReport(Utility.getSharedPreferences(getApplicationContext(), Constant.accessToken));
        call3.enqueue(new Callback<GatePassReportModel>() {
            @Override
            public void onResponse(@NonNull Call<GatePassReportModel> call, @NonNull Response<GatePassReportModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    GatePassReportModel gatePassReportModel = response.body();

                    if (gatePassReportModel.getStatus().equals(Constant.TRUE)) {

                        if (gatePassReportModel.getResponse().size() > 0) {
                            GatePassReportAdapter gatePassReportAdapter = new GatePassReportAdapter(getApplicationContext(), gatePassReportModel.getResponse());
                            gatePassReportList.setHasFixedSize(true);
                            gatePassReportList.setAdapter(gatePassReportAdapter);

                            noDataFound.setVisibility(View.GONE);
                            gatePassReportList.setVisibility(View.VISIBLE);
                        }else {
                            noDataFound.setVisibility(View.VISIBLE);
                            gatePassReportList.setVisibility(View.GONE);
                        }
                    } else if (gatePassReportModel.getStatus().equals(Constant.FALSE)) {
                         noDataFound.setVisibility(View.VISIBLE);
                        gatePassReportList.setVisibility(View.GONE);
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong), getApplicationContext());
                    } else if (gatePassReportModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<GatePassReportModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }
}