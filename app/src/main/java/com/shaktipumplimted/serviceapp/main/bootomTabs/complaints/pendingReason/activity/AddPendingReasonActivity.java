package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPendingReasonActivity extends AppCompatActivity implements View.OnClickListener {


    Toolbar toolbar;
    Spinner pendingReasonSpinner;
    EditText actionExt;
    TextView followUpDateTxt,submitBtn;
    RelativeLayout followUpDateBtn;
    String selectedFollowUpDate = "";

    public final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    public final static SimpleDateFormat sendDateFormat =
            new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pending_reason);

        Init();
        listner();
    }



    private void Init() {
        pendingReasonSpinner = findViewById(R.id.pendingReasonSpinner);
        actionExt = findViewById(R.id.actionExt);
        followUpDateTxt = findViewById(R.id.followUpDateTxt);
        followUpDateBtn = findViewById(R.id.followUpDateBtn);
        submitBtn = findViewById(R.id.submitBtn);

        toolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.addPendingReason));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        followUpDateTxt.setText(Utility.getCurrentDate());
    }

    private void listner() {
        followUpDateBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.followUpDateBtn:
                ChooseDate();
                break;
            case R.id.submitBtn:

                break;
        }
    }

    private void ChooseDate() {

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(AddPendingReasonActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                followUpDateTxt.setText(dateFormat.format(calendar.getTime()));
                selectedFollowUpDate = sendDateFormat.format(calendar.getTime());
                Log.e("Date1==>", selectedFollowUpDate.toString());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();


    }

}