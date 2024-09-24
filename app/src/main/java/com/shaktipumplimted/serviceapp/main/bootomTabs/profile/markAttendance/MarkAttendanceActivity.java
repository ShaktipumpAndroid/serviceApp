package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.SurfaceCameraActivity;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.LocalConveyanceActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

public class MarkAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 101;

    Toolbar toolbar;
    TextView attendanceInBtn, attendanceOutBtn, attendanceInTimeTxt, attendanceOutTimeTxt;

    APIInterface apiInterface;
    DatabaseHelper databaseHelper;
    String markAttendanceStatus ="",imagePath = "";
    List <MarkAttendanceModel>markAttendanceList;
    RecyclerView  attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        Init();
        listner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkPermission()){
            requestPermission();
        }
    }

    private void Init() {
        markAttendanceList = new ArrayList<>();
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        attendanceInBtn = findViewById(R.id.attendanceInBtn);
        attendanceOutBtn = findViewById(R.id.attendanceOutBtn);
        attendanceInTimeTxt = findViewById(R.id.attendanceInTimeTxt);
        attendanceOutTimeTxt = findViewById(R.id.attendanceOutTimeTxt);
        attendanceList = findViewById(R.id.attendanceList);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.markAttendance));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

       setMarkAttendanceData();
    }

    private void setMarkAttendanceData() {
        markAttendanceList = databaseHelper.getAllMarkAttendanceData(true);

        if(databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA,DatabaseHelper.KEY_ATTENDANCE_DATE,Utility.getCurrentDate())&&
                databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA,DatabaseHelper.KEY_ATTENDANCE_STATUS,Constant.attendanceIN)){

            attendanceInBtn.setBackgroundResource(R.drawable.rounded_bg_grey);
            attendanceOutBtn.setBackgroundResource(R.drawable.rounded_bg_blue);
            attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_in_time)+markAttendanceList.get(markAttendanceList.size()-1).getAttendanceDate()+"\n"+markAttendanceList.get(markAttendanceList.size()-1).getAttendanceTime());
        }

        if(databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA,DatabaseHelper.KEY_ATTENDANCE_DATE,Utility.getCurrentDate())&&
                databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA,DatabaseHelper.KEY_ATTENDANCE_STATUS,Constant.attendanceOut)){

            attendanceInBtn.setBackgroundResource(R.drawable.rounded_bg_grey);
            attendanceOutBtn.setBackgroundResource(R.drawable.rounded_bg_grey);
            attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_in_time)+markAttendanceList.get(0).getAttendanceDate()+"\n"+markAttendanceList.get(0).getAttendanceTime());
            attendanceOutTimeTxt.setText(getResources().getString(R.string.attendance_out_time)+markAttendanceList.get(markAttendanceList.size()-1).getAttendanceDate()+"\n"+markAttendanceList.get(markAttendanceList.size()-1).getAttendanceTime());

        }
    }

    private void listner() {
        attendanceInBtn.setOnClickListener(this);
        attendanceOutBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attendanceInBtn:
                markAttendance("1");

                break;

            case R.id.attendanceOutBtn:
                markAttendance("2");
                break;
        }
    }

    private void markAttendance(String value) {
        markAttendanceStatus = value;
        cameraIntent();
    }



    private void cameraIntent() {

        camraLauncher.launch(new Intent(MarkAttendanceActivity.this, SurfaceCameraActivity.class)
                        .putExtra(Constant.frontCamera,"0")
                .putExtra(Constant.customerName, Utility.getSharedPreferences(getApplicationContext(), Constant.userName)));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {

                        Bundle bundle = result.getData().getExtras();
                        Log.e("bundle====>", bundle.get(Constant.file).toString());
                        imagePath = bundle.get(Constant.file).toString();
                       saveInLocalDatabase();
                    }

                }
            });

    private void saveInLocalDatabase() {
        MarkAttendanceModel markAttendanceModel = new MarkAttendanceModel();
        markAttendanceModel.setAttendanceDate(Utility.getCurrentDate());
        markAttendanceModel.setAttendanceTime(Utility.getCurrentTime());
        markAttendanceModel.setAttendanceImg(imagePath);
        if(markAttendanceStatus.equals("1")){
            markAttendanceModel.setAttendanceStatus(Constant.attendanceIN);
            attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_in_time)+Utility.getCurrentDate()+"\n"+Utility.getCurrentTime());
        }else {
            markAttendanceModel.setAttendanceStatus(Constant.attendanceOut);
            attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_out_time)+Utility.getCurrentDate()+"\n"+Utility.getCurrentTime());
        }

        databaseHelper.insertMarkAttendanceData(markAttendanceModel);
    }


    /*--------------------------------------------Check Permission-------------------------------------------------------*/


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
    }

    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadMediaImages = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);


        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadMediaImages == PackageManager.PERMISSION_GRANTED;
        } else {
            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0) {

                    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        boolean CoarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean Camera = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadMediaImages = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (!CoarseLocationAccepted && !Camera && !ReadMediaImages) {
                            Utility.ShowToast(getResources().getString(R.string.allow_all_permission), getApplicationContext());
                        }
                    } else {
                        boolean FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadPhoneStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean WritePhoneStorage = grantResults[4] == PackageManager.PERMISSION_GRANTED;


                        if (!FineLocationAccepted && !CoarseLocationAccepted && !Camera && !ReadPhoneStorage && !WritePhoneStorage) {
                            Utility.ShowToast(getResources().getString(R.string.allow_all_permission), getApplicationContext());
                        }
                    }
                }
                break;
        }
    }

}