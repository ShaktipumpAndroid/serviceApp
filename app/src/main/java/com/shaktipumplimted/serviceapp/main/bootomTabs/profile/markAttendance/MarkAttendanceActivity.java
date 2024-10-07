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

import com.google.gson.Gson;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.SurfaceCameraActivity;
import com.shaktipumplimted.serviceapp.Utils.common.model.CommonRespModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.adapter.MarkAttendanceAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.AllAttendanceRecordModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.AttendanceDataModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;
import com.shaktipumplimted.serviceapp.webService.uploadImages.UploadImageAPIS;
import com.shaktipumplimted.serviceapp.webService.uploadImages.interfaces.ActionListenerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 101;

    Toolbar toolbar;
    TextView attendanceInBtn, attendanceOutBtn, attendanceInTimeTxt, attendanceOutTimeTxt;

    APIInterface apiInterface;
    DatabaseHelper databaseHelper;
    String markAttendanceStatus = "", imagePath = "";
    List<MarkAttendanceModel> markAttendanceList;
    List<AllAttendanceRecordModel> allAttendanceList = new ArrayList<>();
    RecyclerView attendanceList;
    String timestatus = "", latitude = "", longitude = "", address = "", date = "", time = "";
    UploadImageAPIS uploadImageAPIS;

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
        allAttendanceList = new ArrayList<>();
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        uploadImageAPIS = new UploadImageAPIS(getApplicationContext());
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


        if (Utility.isInternetOn(getApplicationContext())) {
                getAttendanceData();

        } else {
            setMarkAttendanceData();
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
        }

    }

    private void setMarkAttendanceData() {

        markAttendanceList = databaseHelper.getAllMarkAttendanceData(false, Utility.getCurrentDate(), false);
        allAttendanceList = databaseHelper.getAllAttendanceHistoryData();
        if (allAttendanceList.size() > 0) {

            MarkAttendanceAdapter markAttendanceAdapter = new MarkAttendanceAdapter(getApplicationContext(), allAttendanceList);
            attendanceList.setHasFixedSize(true);
            attendanceList.setAdapter(markAttendanceAdapter);

            if (markAttendanceList.size() > 0) {
                if (markAttendanceList.get(0).getAttendanceStatus().equals(Constant.attendanceIN)) {

                    attendanceInBtn.setBackgroundResource(R.drawable.rounded_bg_grey);
                    attendanceOutBtn.setBackgroundResource(R.drawable.rounded_bg_blue);
                    attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_in_time) + markAttendanceList.get(markAttendanceList.size() - 1).getAttendanceDate() + "\n" + markAttendanceList.get(markAttendanceList.size() - 1).getAttendanceTime());
                }
                if (markAttendanceList.size() > 1) {
                    if (markAttendanceList.get(1).getAttendanceStatus().equals(Constant.attendanceOut)) {

                        attendanceInBtn.setBackgroundResource(R.drawable.rounded_bg_grey);
                        attendanceOutBtn.setBackgroundResource(R.drawable.rounded_bg_grey);
                        attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_in_time) + markAttendanceList.get(0).getAttendanceDate() + "\n" + markAttendanceList.get(0).getAttendanceTime());
                        attendanceOutTimeTxt.setText(getResources().getString(R.string.attendance_out_time) + markAttendanceList.get(markAttendanceList.size() - 1).getAttendanceDate() + "\n" + markAttendanceList.get(markAttendanceList.size() - 1).getAttendanceTime());

                    }
                }
            }
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
                if(databaseHelper.isRecordExist(DatabaseHelper.TABLE_DSR_RECORD, DatabaseHelper.KEY_DSR_DATE,Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd",Utility.getCurrentDate()))){
                    markAttendance("2");
                }else {
                    Utility.ShowToast(getResources().getString(R.string.pls_fill_dsr_entry),getApplicationContext());
                }
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
                        imagePath = bundle.get(Constant.file).toString();
                        latitude = bundle.get(Constant.latitude).toString();
                        longitude = bundle.get(Constant.longitude).toString();
                        address = bundle.get(Constant.address).toString();

                        saveInLocalDatabase();

                    }
                }
            });

    private void saveInLocalDatabase() {
        if (markAttendanceStatus.equals("1")) {
            databaseHelper.deleteData(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA);
        }

        MarkAttendanceModel markAttendanceModel = new MarkAttendanceModel();
        markAttendanceModel.setAttendanceDate(Utility.getCurrentDate());
        markAttendanceModel.setAttendanceTime(Utility.getCurrentTime());
        markAttendanceModel.setAttendanceImg(imagePath);
        markAttendanceModel.setLatitude(latitude);
        markAttendanceModel.setLongitude(longitude);
        markAttendanceModel.setDataSavedLocally(true);

        if (markAttendanceStatus.equals("1")) {
            markAttendanceModel.setAttendanceStatus(Constant.attendanceIN);
            attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_in_time) + Utility.getCurrentDate() + "\n" + Utility.getCurrentTime());

        } else {
            markAttendanceModel.setAttendanceStatus(Constant.attendanceOut);
            attendanceInTimeTxt.setText(getResources().getString(R.string.attendance_out_time) + Utility.getCurrentDate() + "\n" + Utility.getCurrentTime());
        }
        Log.e("markAttendanceModel====>", markAttendanceModel.toString());
        databaseHelper.insertMarkAttendanceData(markAttendanceModel);
        if (Utility.isInternetOn(getApplicationContext())) {
            SaveAttendance(markAttendanceModel);
        } else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
        }
    }

    private void SaveAttendance(MarkAttendanceModel markAttendanceModel) {
        markAttendanceList = databaseHelper.getAllMarkAttendanceData(false, Utility.getCurrentDate(), false);
        if (markAttendanceStatus.equals("1")) {
            timestatus = "in";
        } else if (markAttendanceStatus.equals("2")) {
            timestatus = "out";
        }
        date = Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", markAttendanceList.get(markAttendanceList.size() - 1).getAttendanceDate());
        time = Utility.getFormattedTime("hh:mm a", "hhmmss", markAttendanceList.get(markAttendanceList.size() - 1).getAttendanceTime());

        Utility.showProgressDialogue(this);
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", date);
            jsonObject.put("time",time);
            jsonObject.put("lat_long", latitude + "," + longitude);

            if (!address.isEmpty()) {
                jsonObject.put("address", address);
            } else {
                jsonObject.put("address", Utility.getAddressFromLatLng(getApplicationContext(), latitude, longitude));
            }

            jsonObject.put("timestatus", timestatus);
            jsonObject.put("image", Utility.getBase64FromPath(getApplicationContext(), imagePath));

            jsonArray.put(jsonObject);
            uploadImageAPIS.setActionListener(jsonArray, Constant.markAttendance, new ActionListenerCallback() {
                @Override
                public void onActionSuccess(String result) {
                    Utility.hideProgressDialogue();
                    CommonRespModel commonRespModel = new Gson().fromJson(result, CommonRespModel.class);
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {

                        if (!markAttendanceStatus.equals("1")) {
                            databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_DSR_RECORD, DatabaseHelper.KEY_DSR_DATE, Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", Utility.getCurrentDate()));
                        }

                        if (databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA, DatabaseHelper.KEY_ATTENDANCE_DATE, markAttendanceModel.getAttendanceDate())) {
                            MarkAttendanceModel markAttendanceModel1 = new MarkAttendanceModel();
                            markAttendanceModel1.setAttendanceDate(markAttendanceModel.getAttendanceDate());
                            markAttendanceModel1.setAttendanceTime(markAttendanceModel.getAttendanceTime());
                            markAttendanceModel1.setAttendanceImg(markAttendanceModel.getAttendanceImg());
                            markAttendanceModel1.setAttendanceStatus(markAttendanceModel.getAttendanceStatus());
                            markAttendanceModel1.setLatitude(markAttendanceModel.getLatitude());
                            markAttendanceModel1.setLongitude(markAttendanceModel.getLongitude());
                            markAttendanceModel1.setDataSavedLocally(false);

                            databaseHelper.updateMarkAttendanceData(markAttendanceModel1);
                        }

                        onBackPressed();
                        Utility.ShowToast(commonRespModel.getMessage(), getApplicationContext());
                    } else if (commonRespModel.getStatus().equals(Constant.FALSE)) {
                        Utility.ShowToast(commonRespModel.getMessage(), getApplicationContext());
                    } else if (commonRespModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }
                }

                @Override
                public void onActionFailure(String failureMessage) {
                    Utility.hideProgressDialogue();
                    try {
                        JSONObject jsonObject = new JSONObject(failureMessage);
                        if(jsonObject.getString("status").equals(Constant.FAILED)) {
                            Utility.logout(getApplicationContext());
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void getAttendanceData() {

        Utility.showProgressDialogue(this);
        Call<AttendanceDataModel> call3 = apiInterface.getAttendanceData(Utility.getSharedPreferences(getApplicationContext(), Constant.accessToken));
        call3.enqueue(new Callback<AttendanceDataModel>() {
            @Override
            public void onResponse(@NonNull Call<AttendanceDataModel> call, @NonNull Response<AttendanceDataModel> response) {
                Log.e("url===>", String.valueOf(call.request().url()));
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    AttendanceDataModel attendanceDataModel = response.body();
                    if (attendanceDataModel.getStatus().equals(Constant.TRUE)) {

                        imagePath = "";

                        for (int i=0; i<attendanceDataModel.getResponse().size(); i++){

                            if (!attendanceDataModel.getResponse().get(i).getServerTimeIn().isEmpty()&&
                                    !attendanceDataModel.getResponse().get(i).getServerTimeIn().equals("00:00:00")) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA, DatabaseHelper.KEY_ATTENDANCE_TIME, Utility.getFormattedTime("HH:mm:ss", "h:mm a", attendanceDataModel.getResponse().get(i).getServerTimeIn()))) {
                                    addInLocalDatabase(attendanceDataModel.getResponse().get(i), Constant.attendanceIN, 0);
                                    addInLocalDatabase(attendanceDataModel.getResponse().get(i), Constant.attendanceIN, 3);

                                }
                            }
                            if (!attendanceDataModel.getResponse().get(i).getServerTimeOut().isEmpty() &&
                                    !attendanceDataModel.getResponse().get(i).getServerTimeOut().equals("00:00:00")) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA, DatabaseHelper.KEY_ATTENDANCE_TIME, Utility.getFormattedTime("HH:mm:ss", "h:mm a", attendanceDataModel.getResponse().get(i).getServerTimeOut()))) {
                                    addInLocalDatabase(attendanceDataModel.getResponse().get(i), Constant.attendanceOut, 1);
                                    addInLocalDatabase(attendanceDataModel.getResponse().get(i), Constant.attendanceIN, 3);

                                }
                            }


                        }

                        setMarkAttendanceData();

                    }
                    if (attendanceDataModel.getStatus().equals(Constant.FALSE)) {
                        Utility.hideProgressDialogue();
                    } else if (attendanceDataModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<AttendanceDataModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });


    }

    private void addInLocalDatabase(AttendanceDataModel.Response response, String attendanceStatus, int value) {

        switch (value) {
            case 0:
                MarkAttendanceModel markAttendanceModel = new MarkAttendanceModel();
                markAttendanceModel.setAttendanceDate(response.getServerDateIn());
                markAttendanceModel.setAttendanceTime(Utility.getFormattedTime("HH:mm:ss", "h:mm a", response.getServerTimeIn()));
                markAttendanceModel.setAttendanceImg(imagePath);
                markAttendanceModel.setAttendanceStatus(attendanceStatus);
                markAttendanceModel.setLatitude("");
                markAttendanceModel.setLongitude("");
                markAttendanceModel.setDataSavedLocally(false);
                databaseHelper.insertMarkAttendanceData(markAttendanceModel);
                break;
            case 1:
                MarkAttendanceModel markAttendanceModel1 = new MarkAttendanceModel();
                markAttendanceModel1.setAttendanceDate(response.getServerDateOut());
                markAttendanceModel1.setAttendanceTime(Utility.getFormattedTime("HH:mm:ss", "h:mm a", response.getServerTimeOut()));
                markAttendanceModel1.setAttendanceImg(imagePath);
                markAttendanceModel1.setAttendanceStatus(attendanceStatus);
                markAttendanceModel1.setLatitude("");
                markAttendanceModel1.setLongitude("");
                markAttendanceModel1.setDataSavedLocally(false);
                databaseHelper.insertMarkAttendanceData(markAttendanceModel1);
                break;
            case 3:
                AllAttendanceRecordModel attendanceRecordModel = new AllAttendanceRecordModel();
                attendanceRecordModel.setAttendanceDate(response.getServerDateIn());
                attendanceRecordModel.setAttendanceInTime(response.getServerTimeIn());
                attendanceRecordModel.setAttendanceOutTime(response.getServerTimeOut());
                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_ATTENDANCE_HISTORY_DATA, DatabaseHelper.KEY_ATTENDANCE_DATE, response.getServerDateIn())) {
                    databaseHelper.insertAttendanceHistoryData(attendanceRecordModel);
                } else {
                    databaseHelper.updateAttendanceHistoryData(attendanceRecordModel);
                }
                break;


        }
    }
}