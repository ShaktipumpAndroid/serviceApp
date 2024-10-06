package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.adapter.LocalConveyanceReportAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.model.LocalConveyanceReportModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalConveyanceReportActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    RelativeLayout fromDateRl, toDateRl;
    TextView fromDateTxt, toDateTxt, searchBtn,noDataFound;
    APIInterface apiInterface;
    RecyclerView localConveyanceReportList;

    public final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_conveyance_report);

        inIt();
        listner();
    }


    private void inIt() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        toolbar = findViewById(R.id.toolbar);
        fromDateRl = findViewById(R.id.fromDateRl);
        toDateRl = findViewById(R.id.toDateRl);
        fromDateTxt = findViewById(R.id.fromDateTxt);
        toDateTxt = findViewById(R.id.toDateTxt);
        searchBtn = findViewById(R.id.searchBtn);
        localConveyanceReportList = findViewById(R.id.localConveyanceReportList);
        noDataFound = findViewById(R.id.noDataFound);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.localConveyanceReport));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        fromDateTxt.setText(Utility.getCurrentDate());
        toDateTxt.setText(Utility.getCurrentDate());

    }

    private void listner() {
        searchBtn.setOnClickListener(this);
        fromDateRl.setOnClickListener(this);
        toDateRl.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:
                if (Utility.isInternetOn(getApplicationContext())) {
                    getLocalConveyanceReport();
                } else {
                    Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
                }
                break;

            case R.id.fromDateRl:
                ChooseDate(0);
                break;

            case R.id.toDateRl:
                ChooseDate(1);
                break;
        }
    }


    private void ChooseDate(int value) {

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(LocalConveyanceReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                if (value == 0) {
                    fromDateTxt.setText(dateFormat.format(calendar.getTime()));
                } else {
                    toDateTxt.setText(dateFormat.format(calendar.getTime()));

                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        //dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();


    }

    private void getLocalConveyanceReport() {

        Utility.showProgressDialogue(this);
        Call<LocalConveyanceReportModel> call3 = apiInterface.getTravelReport(Utility.getSharedPreferences(getApplicationContext(), Constant.accessToken),
                Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", fromDateTxt.getText().toString().trim()),
                Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", toDateTxt.getText().toString().trim()));
        call3.enqueue(new Callback<LocalConveyanceReportModel>() {
            @Override
            public void onResponse(@NonNull Call<LocalConveyanceReportModel> call, @NonNull Response<LocalConveyanceReportModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    LocalConveyanceReportModel localConveyanceReportModel = response.body();

                    if (localConveyanceReportModel.getStatus().equals(Constant.TRUE)) {

                        if (localConveyanceReportModel.getResponse().size() > 0) {
                            LocalConveyanceReportAdapter localConveyanceReportAdapter = new LocalConveyanceReportAdapter(getApplicationContext(), localConveyanceReportModel.getResponse());
                            localConveyanceReportList.setHasFixedSize(true);
                            localConveyanceReportList.setAdapter(localConveyanceReportAdapter);

                            noDataFound.setVisibility(View.GONE);
                            localConveyanceReportList.setVisibility(View.VISIBLE);
                        }else {
                            noDataFound.setText(getResources().getString(R.string.nodatafound));
                            noDataFound.setVisibility(View.VISIBLE);
                            localConveyanceReportList.setVisibility(View.GONE);
                        }
                    } else if (localConveyanceReportModel.getStatus().equals(Constant.FALSE)) {
                        noDataFound.setText(getResources().getString(R.string.nodatafound));
                        noDataFound.setVisibility(View.VISIBLE);
                        localConveyanceReportList.setVisibility(View.GONE);
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong), getApplicationContext());
                    } else if (localConveyanceReportModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<LocalConveyanceReportModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }
}