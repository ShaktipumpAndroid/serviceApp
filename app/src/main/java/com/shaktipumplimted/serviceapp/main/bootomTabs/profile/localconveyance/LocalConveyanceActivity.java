package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.GpsTracker;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.SurfaceCameraActivity;
import com.shaktipumplimted.serviceapp.Utils.common.model.CommonRespModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.adapter.LocalConveyanceAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.DistanceCalculateModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.LocalConveyanceModel;
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

public class LocalConveyanceActivity extends AppCompatActivity implements View.OnClickListener, LocalConveyanceAdapter.ItemClickListener {
    private static final int REQUEST_CODE_PERMISSION = 101;
    Toolbar toolbar;
    LinearLayout startTravelBtn, endTravelBtn;
    RecyclerView savedTravelList;
    ImageView imgIcon;
    RelativeLayout startLocImg;
    GpsTracker gpsTracker;
    Context mContext;
    DatabaseHelper databaseHelper;
    String imagePath;
    List<LocalConveyanceModel> localConveyanceList = new ArrayList<>();
    LocalConveyanceAdapter localConveyanceAdapter;
    APIInterface apiInterface1;
    String distance = "";
    UploadImageAPIS uploadImageAPIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_conveyance);

        Init();
        listner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermission()) {
            requestPermission();
        } else if (!Utility.isLocationEnabled(LocalConveyanceActivity.this)) {
            Utility.buildAlertMessageNoGps(LocalConveyanceActivity.this);
        }
    }

    private void Init() {
        mContext = this;
        uploadImageAPIS = new UploadImageAPIS(getApplicationContext());
        apiInterface1 = APIClient.getRetrofitDirection(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        toolbar = findViewById(R.id.toolbar);
        startTravelBtn = findViewById(R.id.startTravelBtn);
        endTravelBtn = findViewById(R.id.endTravelBtn);
        savedTravelList = findViewById(R.id.savedTravelList);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.localConveyance));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if (Utility.isTravelStart(getApplicationContext())) {
            tripStarted();
        } else {
            tripEnded();
        }
        getAllData();

    }


    private void listner() {
        startTravelBtn.setOnClickListener(this);
        endTravelBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }
    /*--------------------------------------------OnClick Listner-------------------------------------------------------*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTravelBtn:
                startEndTravel("1");
                break;
            case R.id.endTravelBtn:
                startEndTravel("2");
                break;
            case R.id.startLocImg:
                if (checkPermission()) {
                    cameraIntent();
                } else {
                    requestPermission();
                }

                break;
        }
    }


    private void startEndTravel(String value) {
        gpsTracker = new GpsTracker(this);
        if (checkPermission()) {
            if (gpsTracker.canGetLocation()) {
                startEndTravelPopup(value);
            } else {
                gpsTracker.showSettingsAlert();
            }
        } else {
            requestPermission();
        }
    }




    private void cameraIntent() {

        camraLauncher.launch(new Intent(LocalConveyanceActivity.this, SurfaceCameraActivity.class)
                .putExtra(Constant.frontCamera,"1")
                .putExtra(Constant.customerName, Utility.getSharedPreferences(getApplicationContext(), Constant.userName)));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {

                        Bundle bundle = result.getData().getExtras();
                        Log.e("bundle====>", bundle.get(Constant.file).toString());
                        imagePath = bundle.get(Constant.file).toString();
                        imgIcon.setImageResource(R.mipmap.tick_icon);
                    }

                }
            });

    private void startEndTravelPopup(String value) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.start_end_popup,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextInputLayout startLatitudeLayout = layout.findViewById(R.id.startLatitudeLayout);
        TextInputLayout startLongitudeLayout = layout.findViewById(R.id.startLongitudeLayout);
        TextInputLayout startAddressLayout = layout.findViewById(R.id.startAddressLayout);
        TextInputEditText startLatitudeExt = layout.findViewById(R.id.startLatitudeExt);
        TextInputEditText startLongitudeExt = layout.findViewById(R.id.startLongitudeExt);
        TextInputEditText startAddressExt = layout.findViewById(R.id.startAddressExt);

        TextInputLayout endtLatitudeLayout = layout.findViewById(R.id.endLatitudeLayout);
        TextInputLayout endLongitudeLayout = layout.findViewById(R.id.endLongitudeLayout);
        TextInputLayout endAddressLayout = layout.findViewById(R.id.endAddressLayout);
        TextInputEditText endLatitudeExt = layout.findViewById(R.id.endLatitudeExt);
        TextInputEditText endLongitudeExt = layout.findViewById(R.id.endLongitudeExt);
        TextInputEditText endAddressExt = layout.findViewById(R.id.endAddressExt);
        TextInputLayout travelModeLayout = layout.findViewById(R.id.travelLayout);
        TextInputEditText travelModeEdt = layout.findViewById(R.id.travelModeEdt);
        TextInputLayout distanceLayout = layout.findViewById(R.id.distanceLayout);
        TextInputEditText distanceEdt = layout.findViewById(R.id.distanceEdt);

        TextView startLocImgTxt = layout.findViewById(R.id.startLocImgTxt);
        imgIcon = layout.findViewById(R.id.imgIcon);
        startLocImg = layout.findViewById(R.id.startLocImg);
        TextView confirmBtn = layout.findViewById(R.id.confirmBtn);
        TextView cancelBtn  = layout.findViewById(R.id.cancelBtn);

        if (value.equals("1")) {
            startLatitudeExt.setText(String.valueOf(gpsTracker.getLatitude()));
            startLongitudeExt.setText(String.valueOf(gpsTracker.getLongitude()));
            startAddressExt.setText(Utility.getAddressFromLatLng(mContext, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude())));
        }
        else if (value.equals("2")) {
            localConveyanceList = new ArrayList<>();
            localConveyanceList = databaseHelper.getAllLocalConveyanceData(true);
            LocalConveyanceModel localConveyance = localConveyanceList.get(localConveyanceList.size() - 1);

            startLatitudeLayout.setHint(getResources().getString(R.string.endLatitude));
            startLongitudeLayout.setHint(getResources().getString(R.string.endLongitude));
            startAddressLayout.setHint(getResources().getString(R.string.endLocation));
            startLocImgTxt.setText(getResources().getString(R.string.endLocImg));
            endtLatitudeLayout.setVisibility(View.VISIBLE);
            endLongitudeLayout.setVisibility(View.VISIBLE);
            endAddressLayout.setVisibility(View.VISIBLE);

            startLatitudeLayout.setVisibility(View.GONE);
            startLongitudeLayout.setVisibility(View.GONE);
            startAddressLayout.setVisibility(View.GONE);
            travelModeEdt.setVisibility(View.GONE);
            distanceLayout.setVisibility(View.GONE);

            startLatitudeExt.setText(localConveyance.getStartLatitude());
            startLongitudeExt.setText(localConveyance.getStartLongitude());
            startAddressExt.setText(localConveyance.getStartAddress());
            endLatitudeExt.setText(String.valueOf(gpsTracker.getLatitude()));
            endLongitudeExt.setText(String.valueOf(gpsTracker.getLongitude()));
            endAddressExt.setText(Utility.getAddressFromLatLng(mContext, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude())));

        }

        confirmBtn.setOnClickListener(v -> {
            if (value.equals("1")) {
                LocalConveyanceModel localConveyanceModel = new LocalConveyanceModel();
                localConveyanceModel.setStartLatitude(startLatitudeExt.getText().toString().trim());
                localConveyanceModel.setStartLongitude(startLongitudeExt.getText().toString().trim());
                localConveyanceModel.setEndLatitude("");
                localConveyanceModel.setEndLongitude("");
                localConveyanceModel.setStartAddress(startAddressExt.getText().toString().trim());
                localConveyanceModel.setEndAddress("");
                localConveyanceModel.setStartDate(Utility.getCurrentDate());
                localConveyanceModel.setStartTime(Utility.getCurrentTime());
                localConveyanceModel.setEndDate("");
                localConveyanceModel.setEndTime("");
                localConveyanceModel.setStartImgPath(imagePath);
                localConveyanceModel.setEndImgPath("");
                databaseHelper.insertLocalConveyanceData(localConveyanceModel);

                tripStarted();
            } else if(value.equals("2")){
                localConveyanceList = new ArrayList<>();
                localConveyanceList = databaseHelper.getAllLocalConveyanceData(true);

                LocalConveyanceModel localConveyance = localConveyanceList.get(localConveyanceList.size() - 1);
                LocalConveyanceModel localConveyanceModel = new LocalConveyanceModel();
                localConveyanceModel.setStartLatitude(localConveyance.getStartLatitude());
                localConveyanceModel.setStartLongitude(localConveyance.getStartLongitude());
                localConveyanceModel.setEndLatitude(endLatitudeExt.getText().toString().trim());
                localConveyanceModel.setEndLongitude(endLongitudeExt.getText().toString().trim());
                localConveyanceModel.setStartAddress(localConveyance.getStartAddress());
                localConveyanceModel.setEndAddress(endAddressExt.getText().toString().trim());
                localConveyanceModel.setStartDate(localConveyance.getStartDate());
                localConveyanceModel.setStartTime(localConveyance.getStartTime());
                localConveyanceModel.setEndDate(Utility.getCurrentDate());
                localConveyanceModel.setEndTime(Utility.getCurrentTime());
                localConveyanceModel.setStartImgPath(localConveyance.getStartImgPath());
                localConveyanceModel.setEndImgPath(imagePath);
                databaseHelper.updateLocalConveyanceData(localConveyanceModel);

                tripEnded();
                getAllData();
            }
            alertDialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());
        startLocImg.setOnClickListener(this);

    }
    private void saveTravelPopup(LocalConveyanceModel response) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.start_end_popup,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextInputLayout startLatitudeLayout = layout.findViewById(R.id.startLatitudeLayout);
        TextInputLayout startLongitudeLayout = layout.findViewById(R.id.startLongitudeLayout);
        TextInputLayout startAddressLayout = layout.findViewById(R.id.startAddressLayout);
        TextInputEditText startLatitudeExt = layout.findViewById(R.id.startLatitudeExt);
        TextInputEditText startLongitudeExt = layout.findViewById(R.id.startLongitudeExt);
        TextInputEditText startAddressExt = layout.findViewById(R.id.startAddressExt);

        TextInputLayout endtLatitudeLayout = layout.findViewById(R.id.endLatitudeLayout);
        TextInputLayout endLongitudeLayout = layout.findViewById(R.id.endLongitudeLayout);
        TextInputLayout endAddressLayout = layout.findViewById(R.id.endAddressLayout);
        TextInputEditText endLatitudeExt = layout.findViewById(R.id.endLatitudeExt);
        TextInputEditText endLongitudeExt = layout.findViewById(R.id.endLongitudeExt);
        TextInputEditText endAddressExt = layout.findViewById(R.id.endAddressExt);
        TextInputLayout travelModeLayout = layout.findViewById(R.id.travelLayout);
        TextInputEditText travelModeEdt = layout.findViewById(R.id.travelModeEdt);
        TextInputLayout distanceLayout = layout.findViewById(R.id.distanceLayout);
        TextInputEditText distanceEdt = layout.findViewById(R.id.distanceEdt);

        TextView startLocImgTxt = layout.findViewById(R.id.startLocImgTxt);
        imgIcon = layout.findViewById(R.id.imgIcon);
        startLocImg = layout.findViewById(R.id.startLocImg);
        TextView confirmBtn = layout.findViewById(R.id.confirmBtn);
        TextView cancelBtn  = layout.findViewById(R.id.cancelBtn);

            startLatitudeLayout.setHint(getResources().getString(R.string.startLatitude));
            startLongitudeLayout.setHint(getResources().getString(R.string.startLongitude));
            startAddressLayout.setHint(getResources().getString(R.string.startAddress));
            startLocImgTxt.setText(getResources().getString(R.string.endLocImg));
            endtLatitudeLayout.setVisibility(View.VISIBLE);
            endLongitudeLayout.setVisibility(View.VISIBLE);
            endAddressLayout.setVisibility(View.VISIBLE);
            travelModeLayout.setVisibility(View.VISIBLE);
            distanceLayout.setVisibility(View.VISIBLE);
            startLocImg.setVisibility(View.VISIBLE);

            startLatitudeExt.setText(response.getStartLatitude());
            startLongitudeExt.setText(response.getStartLongitude());
            startAddressExt.setText(response.getStartAddress());
            endLatitudeExt.setText(response.getEndLatitude());
            endLongitudeExt.setText(response.getEndLongitude());
            endAddressExt.setText(Utility.getAddressFromLatLng(getApplicationContext(),response.getEndLatitude(),response.getEndLongitude()));
            distanceEdt.setText(distance);
            if(response.getTravelMode()!=null && !response.getTravelMode().isEmpty()){
                travelModeEdt.setEnabled(false);
                travelModeEdt.setText(response.getTravelMode());
            }else {
                travelModeEdt.requestFocus();
            }


        confirmBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
                if(travelModeEdt.getText().toString().isEmpty()){
                    Utility.ShowToast(getResources().getString(R.string.pls_enter_travel_mode),getApplicationContext());
                }else{
                    saveData(response,travelModeEdt.getText().toString().trim());
                }


        });

        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());

    }

    private void saveData(LocalConveyanceModel response, String travelMode) {
        Utility.showProgressDialogue(this);
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("begda", Utility.getFormattedDate("dd.MM.yyyy","yyyyMMdd",response.getStartDate()));
            jsonObject.put("endda", Utility.getFormattedDate("dd.MM.yyyy","yyyyMMdd",response.getEndDate()));
            jsonObject.put("start_time", Utility.getFormattedTime("hh:mm a","hhmmss",response.getStartTime()));
            jsonObject.put("end_time", Utility.getFormattedTime("hh:mm a","hhmmss",response.getEndTime()));
            jsonObject.put("start_lat", response.getStartLatitude());
            jsonObject.put("end_lat", response.getEndLatitude());
            jsonObject.put("start_long", response.getStartLongitude());
            jsonObject.put("end_long", response.getEndLongitude());
            jsonObject.put("start_location", response.getStartAddress());
            jsonObject.put("end_location", response.getEndAddress());
            jsonObject.put("travel_MODE", travelMode);
            jsonObject.put("distance", distance);
            jsonObject.put("lat_long", response.getStartLatitude()+","+response.getStartLongitude());
            if(response.getStartImgPath()!=null &&!response.getStartImgPath().isEmpty()) {
                jsonObject.put("PHOTO1", Utility.getBase64FromPath(getApplicationContext(), response.getStartImgPath()));
            }else {
                jsonObject.put("PHOTO1","");
            }
            if(response.getEndImgPath()!=null &&!response.getEndImgPath().isEmpty()) {
                jsonObject.put("PHOTO2", Utility.getBase64FromPath(getApplicationContext(), response.getEndImgPath()));
            }else {
                jsonObject.put("PHOTO2","");
            }
            jsonArray.put(jsonObject);
            uploadImageAPIS.setActionListener(jsonArray, Constant.localConveyance, new ActionListenerCallback() {
                @Override
                public void onActionSuccess(String result) {
                    Utility.hideProgressDialogue();

                    CommonRespModel commonRespModel = new Gson().fromJson(result, CommonRespModel.class);
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                        databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_LOCAL_CONVEYANCE_DATA, DatabaseHelper.KEY_START_TIME, response.getStartTime());
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

    private void getAllData() {
        localConveyanceList = new ArrayList<>();
        localConveyanceList = databaseHelper.getAllLocalConveyanceData(false);

        if (localConveyanceList.size() > 0) {
            Log.e("localconve===>", localConveyanceList.toString());
            localConveyanceAdapter = new LocalConveyanceAdapter(this, localConveyanceList);
            savedTravelList.setHasFixedSize(true);
            savedTravelList.setAdapter(localConveyanceAdapter);
            localConveyanceAdapter.ItemClick(this);

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

    /*--------------------------------------------Adapter Item Click-------------------------------------------------------*/

    @Override
    public void SetOnItemClickListener(LocalConveyanceModel response, int position) {

        if (Utility.isInternetOn(getApplicationContext())) {
            getCalculatedDistance(response);
        } else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
        }
    }


    private void getCalculatedDistance(LocalConveyanceModel conveyanceModel) {
        Utility.showProgressDialogue(this);
        Call<DistanceCalculateModel> call3 = apiInterface1.getDistance(conveyanceModel.getStartLatitude() + "," + conveyanceModel.getStartLongitude(),
                conveyanceModel.getEndLatitude() + "," + conveyanceModel.getEndLongitude(), Constant.APIKEY);
        call3.enqueue(new Callback<DistanceCalculateModel>() {
            @Override
            public void onResponse(@NonNull Call<DistanceCalculateModel> call, @NonNull Response<DistanceCalculateModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    DistanceCalculateModel distanceCalculateModel = response.body();

                    Log.e("distanceCalculate====>", String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText()));

                    distance = String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText());
                    Log.e("distance2==>", distance);
                    saveTravelPopup(conveyanceModel);

                }

            }

            @Override
            public void onFailure(@NonNull Call<DistanceCalculateModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());
            }
        });


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void tripStarted() {
        startTravelBtn.setEnabled(false);
        endTravelBtn.setEnabled(true);
        startTravelBtn.setBackground(getDrawable(R.drawable.rounded_left_grey));
        endTravelBtn.setBackground(getDrawable(R.drawable.rounded_right_white));
        Utility.setSharedPreference(getApplicationContext(), Constant.localConveyanceJourneyStart, "true");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void tripEnded() {
        startTravelBtn.setEnabled(true);
        endTravelBtn.setEnabled(false);
        startTravelBtn.setBackground(getDrawable(R.drawable.rounded_left_white));
        endTravelBtn.setBackground(getDrawable(R.drawable.rounded_right_grey));
        Utility.setSharedPreference(getApplicationContext(), Constant.localConveyanceJourneyStart, "false");
    }


}