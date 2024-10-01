package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.GpsTracker;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.activity.ComplaintDetailsActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.model.ComplaintDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDetailsModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDropdownModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DSREntryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    TextInputEditText purposeExt,outcomeExt;
    TextView submitBtn;
    Spinner operationSpinner;
    DatabaseHelper databaseHelper;
    APIInterface apiInterface;
    List<SpinnerDataModel> dsrList  = new ArrayList<>();
    String selectedActivity = "", latitude = "", longitude = "";
    GpsTracker gps;
    DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsrentry);

        Init();
        listner();
        retrieveValue();
    }


    private void Init() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        toolbar = findViewById(R.id.toolbar);
        operationSpinner = findViewById(R.id.operationSpinner);
        purposeExt = findViewById(R.id.purposeExt);
        outcomeExt = findViewById(R.id.outcomeExt);
        submitBtn = findViewById(R.id.submitBtn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.dsrEntry));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        gps = new GpsTracker(this);
        decimalFormat = new DecimalFormat("##.######");

    }

    private void listner() {
        operationSpinner.setOnItemSelectedListener(this);
        submitBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void retrieveValue() {

        if (Utility.isInternetOn(getApplicationContext())) {
            if (databaseHelper.isDataAvailable(DatabaseHelper.TABLE_DSR_DROPWODN)) {
                setDropdown();
            } else {
                getDropdownsList();
            }
        } else {
            setDropdown();
        }
        getGpsLocation();
    }

    public void getGpsLocation() {


        if (gps.canGetLocation()) {
            if (!String.valueOf(gps.getLatitude()).isEmpty() && !String.valueOf(gps.getLongitude()).isEmpty()) {
                latitude = decimalFormat.format(gps.getLatitude());
                longitude = decimalFormat.format(gps.getLongitude());
            }
        } else {
            gps.showSettingsAlert();
        }
    }
    private void getDropdownsList() {
        dsrList = new ArrayList<>();

        Utility.showProgressDialogue(this);
        Call<DsrDropdownModel> call3 = apiInterface.getDsrDropdown(Utility.getSharedPreferences(this, Constant.accessToken));
        call3.enqueue(new Callback<DsrDropdownModel>() {
            @Override
            public void onResponse(@NonNull Call<DsrDropdownModel> call, @NonNull Response<DsrDropdownModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    DsrDropdownModel dsrDropdownModel = response.body();

                    if (dsrDropdownModel.getStatus().equals(Constant.TRUE)) {

                        if (dsrDropdownModel.getData().size() > 0) {

                            for (int i = 0; i < dsrDropdownModel.getData().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_DSR_DROPWODN, DatabaseHelper.KEY_ID, dsrDropdownModel.getData().get(i).getHelpCode())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(dsrDropdownModel.getData().get(i).getHelpCode());
                                    spinnerDataModel.setName(dsrDropdownModel.getData().get(i).getHelpName());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_DSR_DROPWODN);
                                }
                            }

                        }
                        setDropdown();
                    } else if (dsrDropdownModel.getStatus().equals(Constant.FALSE)) {
                        Utility.hideProgressDialogue();
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong), DSREntryActivity.this);
                    } else if (dsrDropdownModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DsrDropdownModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setDropdown() {
        dsrList.add(new SpinnerDataModel("00", getResources().getString(R.string.select_dsr_activity)));
        dsrList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_DSR_DROPWODN));
        SpinnerAdapter spinnerAdapter = new com.shaktipumplimted.serviceapp.Utils.common.adapter.SpinnerAdapter(DSREntryActivity.this, dsrList);
        operationSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitBtn:
                if(Utility.isInternetOn(getApplicationContext())){
                    if(selectedActivity.isEmpty()){
                        Utility.ShowToast(getResources().getString(R.string.pls_select_activity),getApplicationContext());
                    } else if (outcomeExt.getText().toString().isEmpty()) {
                        Utility.ShowToast(getResources().getString(R.string.pls_enter_outcome),getApplicationContext());
                    } else if (purposeExt.getText().toString().isEmpty()) {
                        Utility.ShowToast(getResources().getString(R.string.pls_enter_purpose),getApplicationContext());
                    } else{
                        saveData();
                    }
                }else{
                    saveLocally();
                }
                break;
        }
    }

    private void saveLocally() {
        DsrDetailsModel dsrDetailsModel = new DsrDetailsModel();
        dsrDetailsModel.setDsrActivity(selectedActivity);
        dsrDetailsModel.setDsrOutcome(outcomeExt.getText().toString().trim());
        dsrDetailsModel.setDsrPurpose(purposeExt.getText().toString().trim());
        dsrDetailsModel.setDate(Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd",Utility.getCurrentDate()));
        dsrDetailsModel.setTime(Utility.getFormattedTime("HH:mm", "hhmmss",Utility.getCurrentTime()));
        dsrDetailsModel.setLat(latitude);
        dsrDetailsModel.setLng(longitude);
        databaseHelper.insertDsrData(dsrDetailsModel);
    }

    private void saveData() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(view.getId()==R.id.operationSpinner){
            selectedActivity = dsrList.get(position).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}