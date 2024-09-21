package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.zxing.integration.android.IntentIntegrator;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.FileUtils;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.PhotoViewerActivity;
import com.shaktipumplimted.serviceapp.Utils.common.activity.SurfaceCameraActivity;
import com.shaktipumplimted.serviceapp.Utils.common.adapter.ImageSelectionAdapter;
import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.activity.ComplaintForwardActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.activity.ComplaintPhotoListActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.activity.PendingReasonListActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDetailsActivity extends AppCompatActivity implements  View.OnClickListener {

    Toolbar toolbar;
    TextInputEditText complaintNo, customerName, customerMobileNo, customerAddress, materialCodeTxt, materialNameTxt,
            serialNoTxt, billNoTxt, billDateTxt, customerPayExt, companyPayExt, focAmountExt, returnByCompanyExt, payToFreelancerExt, remarkTxt;
    EditText pumpSerialTxt, motorSerialTxt, controllerSerialTxt;
    Spinner categorySpinner, closureReasonSpinner, defectTypeSpinner, complaintRelatedToSpinner;
    ImageView pumpScanBtn, motorScanBtn, controllerScanBtn;
    LinearLayout pendingReasonBtn, forwardForApprovalBtn, forwardComplaintBtn, closeComplaintBtn;

    GmsBarcodeScannerOptions options;
    GmsBarcodeScanner scanner;
    ComplaintListModel complaintListModel;
    DatabaseHelper databaseHelper;
   int scannerCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);

        Init();
        listner();
        retriveValue();
    }

    private void retriveValue() {
        if(getIntent().getExtras()!=null){
            complaintListModel = (ComplaintListModel) getIntent().getSerializableExtra(Constant.complaintData);
            complaintNo.setText(complaintListModel.getCompNo());
            customerName.setText(complaintListModel.getCustomerName());
            customerMobileNo.setText(complaintListModel.getCustomerMobile());
            customerAddress.setText(complaintListModel.getCustomerAddress());
        }
    }

    private void Init() {
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
            complaintListModel = (ComplaintListModel) getIntent().getSerializableExtra(Constant.complaintData);
        }
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
                intent.putExtra(Constant.complaintData,complaintListModel);
                startActivity(intent);
                break;

            case R.id.forwardForApprovalBtn:
                ForwardForApproval(getResources().getString(R.string.want_to_forward_approval));
                break;

            case R.id.forwardComplaintBtn:
                Intent intent1 = new Intent(this, ComplaintForwardActivity.class);
                startActivity(intent1);
                break;

            case R.id.closeComplaintBtn:

                break;
        }

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
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0,     spanString.length(), 0); //fix the color to white
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
}