package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.addCustomer;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumplimted.serviceapp.R;

public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Spinner customerTypeSpinner, customerClassSpinner, countrySpinner, stateSpinner, districtSpinner, talukaSpinner,
            distributorSpinner, intrestTypeSpinner;
    TextInputEditText customerNameExt, customerMobileNoExt, customerLandLineExt, customerEmailExt, customerAddressExt, customerAadharExt,
            customerPanCardExt, customerCurrentLocExt, pinCodeExt, contactPersonExt, contactPersonNoExt;

    TextView submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        Init();
        listner();
    }

    private void listner() {

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        submitBtn.setOnClickListener(this);
        customerTypeSpinner.setOnItemSelectedListener(this);
        customerClassSpinner.setOnItemSelectedListener(this);
        countrySpinner.setOnItemSelectedListener(this);
        stateSpinner.setOnItemSelectedListener(this);
        districtSpinner.setOnItemSelectedListener(this);
        talukaSpinner.setOnItemSelectedListener(this);
        distributorSpinner.setOnItemSelectedListener(this);
        intrestTypeSpinner.setOnItemSelectedListener(this);
    }

    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        customerTypeSpinner = findViewById(R.id.customerTypeSpinner);
        customerClassSpinner = findViewById(R.id.customerClassSpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        stateSpinner = findViewById(R.id.stateSpinner);
        districtSpinner = findViewById(R.id.districtSpinner);
        talukaSpinner = findViewById(R.id.talukaSpinner);
        distributorSpinner = findViewById(R.id.distributorSpinner);
        intrestTypeSpinner = findViewById(R.id.intrestTypeSpinner);
        customerNameExt = findViewById(R.id.customerNameExt);
        customerMobileNoExt = findViewById(R.id.customerMobileNoExt);
        customerLandLineExt = findViewById(R.id.customerLandLineExt);
        customerEmailExt = findViewById(R.id.customerEmailExt);
        customerAddressExt = findViewById(R.id.customerAddressExt);
        customerAadharExt = findViewById(R.id.customerAadharExt);
        customerPanCardExt = findViewById(R.id.customerPanCardExt);
        customerCurrentLocExt = findViewById(R.id.customerCurrentLocExt);
        pinCodeExt = findViewById(R.id.pinCodeExt);
        contactPersonExt = findViewById(R.id.contactPersonExt);
        contactPersonNoExt = findViewById(R.id.contactPersonNoExt);
        submitBtn = findViewById(R.id.submitBtn);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.addCustomer));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.submitBtn:

             break;
     }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.customerTypeSpinner) {
            /*if (!parent.getSelectedItem().toString().equals("Select Category")) {
                selectedCategory = parent.getSelectedItem().toString();
            }*/
        }else  if (parent.getId() == R.id.customerClassSpinner) {
        }else  if (parent.getId() == R.id.countrySpinner) {
        }else  if (parent.getId() == R.id.stateSpinner) {
        }else  if (parent.getId() == R.id.districtSpinner) {
        }else  if (parent.getId() == R.id.talukaSpinner) {
        }else  if (parent.getId() == R.id.distributorSpinner) {
        }else  if (parent.getId() == R.id.intrestTypeSpinner) {
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}