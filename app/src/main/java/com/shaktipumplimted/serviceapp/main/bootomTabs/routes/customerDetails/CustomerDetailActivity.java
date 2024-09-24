package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.checkOut.CheckOutActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory.VisitHistoryActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory.model.VisitHistoryModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList.model.CustomerListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.sitesurvey.SiteSurveyActivity;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

public class CustomerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextInputEditText customerNameExt,customerMobileNoExt,customerEmailExt,customerAddressExt;
    LinearLayout checkInLinear,checkOutLinear,siteSurveyLinear,historyLinear;

    CustomerListModel customerListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        Init();
        listner();
        retrieveValue();
    }

    private void retrieveValue() {
        if(getIntent().getExtras()!=null){
            customerListModel = (CustomerListModel) getIntent().getSerializableExtra(Constant.customerDetails);
            customerNameExt.setText(customerListModel.getCustomerName());
            customerMobileNoExt.setText(customerListModel.getCustomerMobile());
            customerEmailExt.setText(customerListModel.getCustomerEmail());
            customerAddressExt.setText(customerListModel.getCustomerAddress());
        }
    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        checkInLinear.setOnClickListener(this);
        checkOutLinear.setOnClickListener(this);
        siteSurveyLinear.setOnClickListener(this);
        historyLinear.setOnClickListener(this);
    }

    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        customerNameExt = findViewById(R.id.customerNameExt);
        customerMobileNoExt = findViewById(R.id.customerMobileNoExt);
        customerEmailExt = findViewById(R.id.customerEmailExt);
        customerAddressExt = findViewById(R.id.customerAddressExt);
        checkInLinear = findViewById(R.id.checkInLinear);
        checkOutLinear = findViewById(R.id.checkOutLinear);
        siteSurveyLinear = findViewById(R.id.siteSurveyLinear);
        historyLinear = findViewById(R.id.historyLinear);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.customerDetails));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

    }

    /*--------------------------------------------On Click Listner-------------------------------------------------------*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkInLinear:
                checkInPopup(getResources().getString(R.string.want_to_checkout));
                break;
            case R.id.checkOutLinear:
                Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                intent.putExtra(Constant.customerDetails,customerListModel);
                startActivity(intent);
                break;
            case R.id.siteSurveyLinear:
                Intent intent1 = new Intent(getApplicationContext(), SiteSurveyActivity.class);
                intent1.putExtra(Constant.customerDetails,customerListModel);
                startActivity(intent1);

                break;
            case R.id.historyLinear:
                Intent intent2 = new Intent(getApplicationContext(), VisitHistoryActivity.class);
                startActivity(intent2);

                break;
        }
    }
    /*--------------------------------------------Check In Popup-------------------------------------------------------*/

    private void checkInPopup(String message) {
        LayoutInflater inflater = (LayoutInflater) CustomerDetailActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alert_popup,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CustomerDetailActivity.this, R.style.MyDialogTheme);

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
}