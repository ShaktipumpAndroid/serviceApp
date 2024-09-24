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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.login.model.LoginRespModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.activity.AddPendingReasonActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.adapter.LocalConveyanceAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.DistanceCalculateModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.LocalConveyanceModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

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
                startEndTravel("3");
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
        EditText travelModeEdt = layout.findViewById(R.id.travelModeEdt);
        TextInputLayout distanceLayout = layout.findViewById(R.id.distanceLayout);
        TextInputEditText distanceEdt = layout.findViewById(R.id.distanceEdt);

        TextView startLocImgTxt = layout.findViewById(R.id.startLocImgTxt);
        imgIcon = layout.findViewById(R.id.imgIcon);
        startLocImg = layout.findViewById(R.id.startLocImg);
        TextView confirmBtn = layout.findViewById(R.id.confirmBtn);
        TextView cancelBtn = null;
        try {
            cancelBtn = layout.findViewById(R.id.cancelBtn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (value.equals("3")) {
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
            travelModeEdt.setVisibility(View.VISIBLE);

            startLatitudeLayout.setVisibility(View.GONE);
            startLongitudeLayout.setVisibility(View.GONE);
            startAddressLayout.setVisibility(View.GONE);
            travelModeEdt.setVisibility(View.GONE);
            distanceLayout.setVisibility(View.GONE);

            Log.e("latitude2=>", String.valueOf(gpsTracker.getLatitude()));
            Log.e("longitude2=>", String.valueOf(gpsTracker.getLongitude()));
            startLatitudeExt.setText(localConveyance.getStartLatitude());
            startLongitudeExt.setText(localConveyance.getStartLongitude());
            startAddressExt.setText(localConveyance.getStartAddress());
            endLatitudeExt.setText(String.valueOf(gpsTracker.getLatitude()));
            endLongitudeExt.setText(String.valueOf(gpsTracker.getLongitude()));
            endAddressExt.setText(Utility.getAddressFromLatLng(mContext, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude())));

        }else if(value.equals("2")){
            localConveyanceList = new ArrayList<>();
            localConveyanceList = databaseHelper.getAllLocalConveyanceData(true);
            LocalConveyanceModel localConveyance = localConveyanceList.get(localConveyanceList.size() - 1);

            startLatitudeLayout.setHint(getResources().getString(R.string.startLatitude));
            startLongitudeLayout.setHint(getResources().getString(R.string.startLongitude));
            startAddressLayout.setHint(getResources().getString(R.string.startAddress));
            startLocImgTxt.setText(getResources().getString(R.string.endLocImg));
            endtLatitudeLayout.setVisibility(View.VISIBLE);
            endLongitudeLayout.setVisibility(View.VISIBLE);
            endAddressLayout.setVisibility(View.VISIBLE);
            travelModeEdt.setVisibility(View.VISIBLE);
            distanceLayout.setVisibility(View.VISIBLE);


            startLatitudeExt.setText(localConveyance.getStartLatitude());
            startLongitudeExt.setText(localConveyance.getStartLongitude());
            startAddressExt.setText(localConveyance.getStartAddress());
            endLatitudeExt.setText(localConveyance.getEndLatitude());
            endLongitudeExt.setText(localConveyance.getEndLongitude());
            endAddressExt.setText(localConveyance.getEndAddress());
            distanceEdt.setText(distance);
            endAddressExt.setText(localConveyance.getEndAddress());
        }else {
            Log.e("latitude=>", String.valueOf(gpsTracker.getLatitude()));
            Log.e("longitude=>", String.valueOf(gpsTracker.getLongitude()));
            startLatitudeExt.setText(String.valueOf(gpsTracker.getLatitude()));
            startLongitudeExt.setText(String.valueOf(gpsTracker.getLongitude()));
            startAddressExt.setText(Utility.getAddressFromLatLng(mContext, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude())));
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
            } else if(value.equals("3")){
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
                Log.e("endLat=>",endLatitudeExt.getText().toString().trim());
                databaseHelper.updateLocalConveyanceData(localConveyanceModel);

                tripEnded();
                getAllData();
            } else if (value.equals("2")) {

            }
            alertDialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());
        startLocImg.setOnClickListener(this);

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


    private void getCalculatedDistance(LocalConveyanceModel response) {
        Utility.showProgressDialogue(this);
        Call<DistanceCalculateModel> call3 = apiInterface1.getDistance(response.getStartLatitude() + "," + response.getStartLongitude(),
                response.getEndLatitude() + "," + response.getEndLongitude(), Constant.APIKEY);
        call3.enqueue(new Callback<DistanceCalculateModel>() {
            @Override
            public void onResponse(@NonNull Call<DistanceCalculateModel> call, @NonNull Response<DistanceCalculateModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    DistanceCalculateModel distanceCalculateModel = response.body();

                    Log.e("distanceCalculate====>", String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText()));

                    distance = String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText());
                    Log.e("distance2==>", distance);
                    startEndTravel("2");
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