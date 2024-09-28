package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.zxing.integration.android.IntentIntegrator;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.model.ComplaintDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.activity.ComplaintForwardActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.activity.ComplaintPhotoListActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.activity.PendingReasonListActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    TextInputEditText complaintNo, customerName, customerMobileNo, customerAddress, materialCodeTxt, materialNameTxt,
            serialNoTxt, billNoTxt, billDateTxt, customerPayExt, companyPayExt, focAmountExt, returnByCompanyExt, payToFreelancerExt, remarkTxt;
    EditText pumpSerialTxt, motorSerialTxt, controllerSerialTxt;
    Spinner categorySpinner, closureReasonSpinner, defectTypeSpinner, complaintRelatedToSpinner;
    ImageView pumpScanBtn, motorScanBtn, controllerScanBtn;
    LinearLayout pendingReasonBtn, forwardForApprovalBtn, forwardComplaintBtn, closeComplaintBtn;

    GmsBarcodeScannerOptions options;
    GmsBarcodeScanner scanner;
    ComplaintListModel.Datum complaintListModel;
    DatabaseHelper databaseHelper;
    int scannerCode, index_cmp_category;
    APIInterface apiInterface;
    List<SpinnerDataModel> complaintCategoryList = new ArrayList<>();
    List<SpinnerDataModel> complaintDefectList = new ArrayList<>();
    List<SpinnerDataModel> complaintRelatedToList = new ArrayList<>();
    List<SpinnerDataModel> complaintClosureList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);

        Init();
        listner();
        retriveValue();
    }

    private void retriveValue() {
        if (getIntent().getExtras() != null) {
            complaintListModel = (ComplaintListModel.Datum) getIntent().getSerializableExtra(Constant.complaintData);
            complaintNo.setText(complaintListModel.getCmpno());
            customerName.setText(complaintListModel.getCstname());
            customerMobileNo.setText(complaintListModel.getMblno());
            customerAddress.setText(complaintListModel.getCaddress());
            materialCodeTxt.setText(complaintListModel.getMatnr());
            materialNameTxt.setText(complaintListModel.getMaktx());
            billNoTxt.setText(complaintListModel.getVbeln());
            billDateTxt.setText(complaintListModel.getFkdat());
        }

        if (Utility.isInternetOn(getApplicationContext())) {
            if (databaseHelper.isDataAvailable(DatabaseHelper.TABLE_COMPLAINT_CATEGORY) && databaseHelper.isDataAvailable(DatabaseHelper.TABLE_COMPLAINT_DEFECT)
                    && databaseHelper.isDataAvailable(DatabaseHelper.TABLE_COMPLAINT_RELATED)) {
                setDropdown();
            } else {
                getDropdownsList();
            }
        } else {
            setDropdown();
        }
    }

    private void getDropdownsList() {
        complaintCategoryList = new ArrayList<>();
        complaintDefectList = new ArrayList<>();
        complaintRelatedToList = new ArrayList<>();
        complaintClosureList = new ArrayList<>();

        Utility.showProgressDialogue(this);
        Call<ComplaintDropdownModel> call3 = apiInterface.getComplaintDropdowns(Utility.getSharedPreferences(this, Constant.accessToken));
        call3.enqueue(new Callback<ComplaintDropdownModel>() {
            @Override
            public void onResponse(@NonNull Call<ComplaintDropdownModel> call, @NonNull Response<ComplaintDropdownModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    ComplaintDropdownModel complaintDropdownModel = response.body();
                    if (complaintDropdownModel.getStatus().equals(Constant.TRUE)) {

                        if (complaintDropdownModel.getData().getComplainCategory().size() > 0) {

                            for (int i = 0; i < complaintDropdownModel.getData().getComplainCategory().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_CATEGORY, DatabaseHelper.KEY_ID, complaintDropdownModel.getData().getComplainCategory().get(i).getCatId())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(complaintDropdownModel.getData().getComplainCategory().get(i).getCatId());
                                    spinnerDataModel.setName(complaintDropdownModel.getData().getComplainCategory().get(i).getCategory());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_COMPLAINT_CATEGORY);
                                }
                            }

                        }
                        if (complaintDropdownModel.getData().getComplainDefect().size() > 0) {

                            for (int i = 0; i < complaintDropdownModel.getData().getComplainDefect().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_DEFECT, DatabaseHelper.KEY_ID, complaintDropdownModel.getData().getComplainDefect().get(i).getCatId())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(complaintDropdownModel.getData().getComplainDefect().get(i).getCatId());
                                    spinnerDataModel.setName(complaintDropdownModel.getData().getComplainDefect().get(i).getDefect());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_COMPLAINT_DEFECT);
                                }
                            }

                        }
                        if (complaintDropdownModel.getData().getComplainRelatedTo().size() > 0) {

                            for (int i = 0; i < complaintDropdownModel.getData().getComplainRelatedTo().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_RELATED, DatabaseHelper.KEY_ID, complaintDropdownModel.getData().getComplainRelatedTo().get(i).getCatId())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(complaintDropdownModel.getData().getComplainRelatedTo().get(i).getCatId());
                                    spinnerDataModel.setName(complaintDropdownModel.getData().getComplainRelatedTo().get(i).getCmplnRelatedTo());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_COMPLAINT_RELATED);
                                }
                            }

                        }

                        if (complaintDropdownModel.getData().getComplainCloser().size() > 0) {

                            for (int i = 0; i < complaintDropdownModel.getData().getComplainCloser().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_CLOSURE, DatabaseHelper.KEY_ID, complaintDropdownModel.getData().getComplainCloser().get(i).getReason())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(complaintDropdownModel.getData().getComplainCloser().get(i).getReason());
                                    spinnerDataModel.setName(complaintDropdownModel.getData().getComplainCloser().get(i).getReason());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_COMPLAINT_CLOSURE);
                                }
                            }

                        }

                        setDropdown();
                    } else if (complaintDropdownModel.getStatus().equals(Constant.FALSE)) {
                        Utility.hideProgressDialogue();
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong), ComplaintDetailsActivity.this);
                    } else if (complaintDropdownModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ComplaintDropdownModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setDropdown() {
        complaintCategoryList.add(new SpinnerDataModel("00", getResources().getString(R.string.select_cmp_cat)));
        complaintCategoryList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_COMPLAINT_CATEGORY));

        complaintDefectList.add(new SpinnerDataModel("00", getResources().getString(R.string.select_defect_cat)));
        complaintDefectList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_COMPLAINT_DEFECT));

        complaintRelatedToList.add(new SpinnerDataModel("00", getResources().getString(R.string.select_related_to)));
        complaintRelatedToList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_COMPLAINT_RELATED));

        complaintClosureList.add(new SpinnerDataModel("00", getResources().getString(R.string.select_closure)));
        complaintClosureList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_COMPLAINT_CLOSURE));

        setSpinnerAdapter(complaintCategoryList, categorySpinner);
        setSpinnerAdapter(complaintDefectList, defectTypeSpinner);
        setSpinnerAdapter(complaintClosureList, closureReasonSpinner);
        setSpinnerAdapter(complaintRelatedToList, complaintRelatedToSpinner);
    }

    private void setSpinnerAdapter(List<SpinnerDataModel> spinnerList, Spinner spinner) {
        SpinnerAdapter spinnerAdapter = new com.shaktipumplimted.serviceapp.Utils.common.adapter.SpinnerAdapter(ComplaintDetailsActivity.this, spinnerList);
        spinner.setAdapter(spinnerAdapter);
    }


    private void Init() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        toolbar = findViewById(R.id.toolbar);
        complaintNo = findViewById(R.id.complaintNo);
        customerName = findViewById(R.id.customerName);
        customerMobileNo = findViewById(R.id.customerMobileNo);
        customerAddress = findViewById(R.id.customerAddress);
        materialCodeTxt = findViewById(R.id.materialCodeTxt);
        materialNameTxt = findViewById(R.id.materialNameTxt);
        serialNoTxt = findViewById(R.id.serialNoTxt);
        billNoTxt = findViewById(R.id.billNoTxt);
        billDateTxt = findViewById(R.id.billDateTxt);
        customerPayExt = findViewById(R.id.customerPayExt);
        companyPayExt = findViewById(R.id.companyPayExt);
        focAmountExt = findViewById(R.id.focAmountExt);
        returnByCompanyExt = findViewById(R.id.returnByCompanyExt);
        payToFreelancerExt = findViewById(R.id.payToFreelancerExt);
        remarkTxt = findViewById(R.id.remarkTxt);
        pumpSerialTxt = findViewById(R.id.pumpSerialTxt);
        motorSerialTxt = findViewById(R.id.motorSerialTxt);
        controllerSerialTxt = findViewById(R.id.controllerSerialTxt);
        categorySpinner = findViewById(R.id.categorySpinner);
        closureReasonSpinner = findViewById(R.id.closureReasonSpinner);
        defectTypeSpinner = findViewById(R.id.defectTypeSpinner);
        complaintRelatedToSpinner = findViewById(R.id.complaintRelatedToSpinner);

        pumpScanBtn = findViewById(R.id.pumpScanBtn);
        motorScanBtn = findViewById(R.id.motorScanBtn);
        controllerScanBtn = findViewById(R.id.controllerScanBtn);
        pendingReasonBtn = findViewById(R.id.pendingReasonBtn);
        forwardForApprovalBtn = findViewById(R.id.forwardForApprovalBtn);
        forwardComplaintBtn = findViewById(R.id.forwardComplaintBtn);
        closeComplaintBtn = findViewById(R.id.closeComplaintBtn);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complaintDetail));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_ALL_FORMATS)
                .build();

        scanner = GmsBarcodeScanning.getClient(this);

        retrieveValue();


    }

    private void listner() {
        pumpScanBtn.setOnClickListener(this);
        motorScanBtn.setOnClickListener(this);
        controllerScanBtn.setOnClickListener(this);
        pendingReasonBtn.setOnClickListener(this);
        forwardForApprovalBtn.setOnClickListener(this);
        forwardComplaintBtn.setOnClickListener(this);
        closeComplaintBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            complaintListModel = (ComplaintListModel.Datum) getIntent().getSerializableExtra(Constant.complaintData);
        }

        categorySpinner.setOnItemSelectedListener(this);
    }

    /*--------------------------------------------On Click Listner-------------------------------------------------------*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pumpScanBtn:
                ScanCode(1000);
                break;
            case R.id.motorScanBtn:
                ScanCode(2000);
                break;
            case R.id.controllerScanBtn:
                ScanCode(3000);
                break;

            case R.id.pendingReasonBtn:
                Intent intent = new Intent(this, PendingReasonListActivity.class);
                intent.putExtra(Constant.complaintData, complaintListModel);
                startActivity(intent);
                break;

            case R.id.forwardForApprovalBtn:
                ForwardForApproval(getResources().getString(R.string.want_to_forward_approval));
                break;

            case R.id.forwardComplaintBtn:
                Intent intent1 = new Intent(this, ComplaintForwardActivity.class);
                intent1.putExtra(Constant.complaintData, complaintListModel);
                startActivity(intent1);
                break;

            case R.id.closeComplaintBtn:
                closureValidation();
                break;
        }

    }

    private void closureValidation() {

    }




    /*--------------------------------------------Scanner Code-------------------------------------------------------*/

    public void ScanCode(int scannerCode) {
        this.scannerCode = scannerCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            scanner.startScan().addOnSuccessListener(
                            barcode -> {
                                // Task completed successfully
                                String rawValue = barcode.getRawValue();
                                setScanValue(rawValue, scannerCode);
                            })
                    .addOnCanceledListener(
                            () -> {
                                // Task canceled
                                Utility.ShowToast(getResources().getString(R.string.scanning_cancelled), getApplicationContext());
                            })
                    .addOnFailureListener(
                            e -> {
                                // startScanOldVersion();
                                // Task failed with an exception
                                //  Utility.ShowToast(getResources().getString(R.string.scanning_cancelled),getApplicationContext());
                                startScanOldVersion();
                            });
        } else {
            startScanOldVersion();
        }

    }

    private void startScanOldVersion() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QRCode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);// Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void setScanValue(String rawValue, int scannerCode) {
        if (scannerCode == 3000) {
            controllerSerialTxt.setText(rawValue);
        } else {
            if (Utility.isAlphaNumeric(rawValue)) {
                if (scannerCode == 1000) {
                    pumpSerialTxt.setText(rawValue);
                } else if (scannerCode == 2000) {
                    motorSerialTxt.setText(rawValue);
                }
            } else {
                Utility.ShowToast(getResources().getString(R.string.scanCorrectValue), getApplicationContext());
            }

        }
    }



    /*--------------------------------------------Forward For Approval-------------------------------------------------------*/

    private void ForwardForApproval(String message) {
        LayoutInflater inflater = (LayoutInflater) ComplaintDetailsActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alert_popup,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ComplaintDetailsActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView okBtn = layout.findViewById(R.id.okBtn);
        TextView cancelBtn = layout.findViewById(R.id.cancelBtn);
        TextView messageTxt = layout.findViewById(R.id.messageTxt);

        messageTxt.setText(message);

        okBtn.setOnClickListener(v -> alertDialog.dismiss());

        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());

    }
    /*--------------------------------------------OnCreate Menu Options-------------------------------------------------------*/


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complaint_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_view_photo:
                Intent intent = new Intent(getApplicationContext(), ComplaintPhotoListActivity.class);
                startActivity(intent);
                return true;

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view.getId() == R.id.categorySpinner) {

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}