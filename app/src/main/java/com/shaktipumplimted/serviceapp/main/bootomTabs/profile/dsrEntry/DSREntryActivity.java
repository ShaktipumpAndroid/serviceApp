package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumplimted.serviceapp.R;

public class DSREntryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    TextInputEditText purposeExt,outcomeExt;
    TextView submitBtn;
    Spinner operationSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsrentry);

        Init();
        listner();
    }



    private void Init() {
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

    }

    private void listner() {
        operationSpinner.setOnItemSelectedListener(this);
        submitBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
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
        if(view.getId()==R.id.operationSpinner){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}