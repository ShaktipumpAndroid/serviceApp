package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

import com.google.gson.Gson;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.FileUtils;
import com.shaktipumplimted.serviceapp.Utils.GpsTracker;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.PhotoViewerActivity;
import com.shaktipumplimted.serviceapp.Utils.common.activity.SurfaceCameraActivity;
import com.shaktipumplimted.serviceapp.Utils.common.adapter.ImageSelectionAdapter;
import com.shaktipumplimted.serviceapp.Utils.common.model.CommonRespModel;
import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model.PendingReasonModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;
import com.shaktipumplimted.serviceapp.webService.uploadImages.UploadImageAPIS;
import com.shaktipumplimted.serviceapp.webService.uploadImages.interfaces.ActionListenerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPendingReasonActivity extends AppCompatActivity implements View.OnClickListener, ImageSelectionAdapter.ImageSelectionListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;

    Toolbar toolbar;
    Spinner pendingReasonSpinner;
    EditText actionExt;
    TextView followUpDateTxt, submitBtn;
    RelativeLayout followUpDateBtn;

    RecyclerView compImagesList;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();

    AlertDialog alertDialog;

    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();
    List<SpinnerDataModel> pendingReasonList = new ArrayList<>();
    ComplaintListModel.Datum complaintListModel;
    int selectedIndex;
    boolean isUpdate = false, isSelectedAllImages = false;

    String selectedFollowUpDate = "", selectedPendingReason = "";
    DatabaseHelper databaseHelper;
    public final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    public final static SimpleDateFormat sendDateFormat =
            new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

    APIInterface apiInterface;
    GpsTracker gpsTracker;

    UploadImageAPIS uploadImageAPIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pending_reason);

        Init();
        listner();
        retrieveValue();
    }




    private void Init() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        uploadImageAPIS = new UploadImageAPIS(getApplicationContext());
        gpsTracker = new GpsTracker(getApplicationContext());
        pendingReasonSpinner = findViewById(R.id.pendingReasonSpinner);
        actionExt = findViewById(R.id.actionExt);
        followUpDateTxt = findViewById(R.id.followUpDateTxt);
        followUpDateBtn = findViewById(R.id.followUpDateBtn);
        compImagesList = findViewById(R.id.compImagesList);
        submitBtn = findViewById(R.id.submitBtn);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.addPendingReason));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        followUpDateTxt.setText(Utility.getCurrentDate());


        if(Utility.isInternetOn(getApplicationContext())){
            if(databaseHelper.isDataAvailable(DatabaseHelper.TABLE_PENDING_REASON_DATA)){
                setAdapter();
            }else {
                getPendingReasonList();
            }
        }else {
            setAdapter();
        }
    }

    private void listner() {
        followUpDateBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        pendingReasonSpinner.setOnItemSelectedListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {complaintListModel = (ComplaintListModel.Datum) getIntent().getSerializableExtra(Constant.complaintData);
            SetAdapter();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.followUpDateBtn:
                ChooseDate();
                break;
            case R.id.submitBtn:
                if (selectedPendingReason.isEmpty()) {
                    Utility.ShowToast(getResources().getString(R.string.selectPendingReason), getApplicationContext());
                } else if (actionExt.getText().toString().trim().isEmpty()) {
                    Utility.ShowToast(getResources().getString(R.string.EnterAction), getApplicationContext());
                } else if (followUpDateTxt.getText().toString().trim().isEmpty()) {
                    Utility.ShowToast(getResources().getString(R.string.followUpDate), getApplicationContext());
                } else {
                    for (int i = 0; i < imageArrayList.size(); i++) {
                        if (!imageArrayList.get(i).isImageSelected()) {
                            isSelectedAllImages = false;
                            Utility.ShowToast(getResources().getString(R.string.select_all_images), getApplicationContext());
                            break;
                        } else {
                            isSelectedAllImages = true;
                        }
                    }
                    if (isSelectedAllImages) {
                        if (Utility.isInternetOn(getApplicationContext())) {
                            addPendingReason();
                        } else {
                            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
                        }
                    }
                }


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

    /*------------------------------------------------------------------------ImageList---------------------------------------------------------------------*/
    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.attechformphoto1));
        itemNameList.add(getResources().getString(R.string.attechformphoto2));
        itemNameList.add(getResources().getString(R.string.attechformphoto3));
        itemNameList.add(getResources().getString(R.string.attechformphoto4));


        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo(complaintListModel.getCmpno());
            imageModel.setPosition(i + 1);
            imageModel.setSelectedCategory(selectedPendingReason);
            imageArrayList.add(imageModel);
        }

        //Create Table
        imageList = databaseHelper.getAllImages(DatabaseHelper.TABLE_PENDING_REASON_IMAGE_DATA, complaintListModel.getCmpno());

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo() != null &&
                            imageList.get(i).getBillNo().trim().equals(complaintListModel.getCmpno())) {
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setName(imageList.get(i).getName());
                            imageModel.setImagePath(imageList.get(i).getImagePath());
                            imageModel.setBillNo(imageList.get(i).getBillNo());
                            imageModel.setImageSelected(true);
                            imageModel.setPosition(imageList.get(i).getPosition());
                            imageModel.setSelectedCategory(imageList.get(i).getSelectedCategory());
                            imageArrayList.set(j, imageModel);
                        }
                    }
                }
            }
        }

        customAdapter = new ImageSelectionAdapter(AddPendingReasonActivity.this, imageArrayList, false);
        compImagesList.setHasFixedSize(true);
        compImagesList.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);

    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;

        if (imageModelList.isImageSelected()) {
            isUpdate = true;
            selectImage("1");
        } else {
            if(!selectedPendingReason.isEmpty()) {
                isUpdate = false;
                selectImage("0");
            }else {
                Utility.ShowToast(getResources().getString(R.string.selectPendingReasonFirst),getApplicationContext());
            }
        }

    }

    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) AddPendingReasonActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(AddPendingReasonActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView camera = layout.findViewById(R.id.camera);
        TextView cancel = layout.findViewById(R.id.cancel);

        if (value.equals("0")) {
            gallery.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            camera.setText(getResources().getString(R.string.camera));

        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            camera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                galleryIntent();
            } else {
                Intent i_display_image = new Intent(AddPendingReasonActivity.this, PhotoViewerActivity.class);
                i_display_image.putExtra(Constant.imagePath, imageArrayList.get(selectedIndex).getImagePath());
                i_display_image.putExtra(Constant.Images, "0");
                startActivity(i_display_image);
            }
        });

        camera.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                cameraIntent();
            } else {
                selectImage("0");
            }
        });

        cancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
    }

    private void cameraIntent() {

        camraLauncher.launch(new Intent(AddPendingReasonActivity.this, SurfaceCameraActivity.class)
                .putExtra(Constant.frontCamera, "1")
                .putExtra(Constant.customerName, complaintListModel.getCstname()));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {

                        Bundle bundle = result.getData().getExtras();
                        Log.e("bundle====>", bundle.get(Constant.file).toString());
                        UpdateArrayList(bundle.get(Constant.file).toString());

                    }

                }
            });


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {

            case PICK_FROM_FILE:
                try {
                    Uri mImageCaptureUri = data.getData();
                    String path = FileUtils.getPath(AddPendingReasonActivity.this, mImageCaptureUri); // From Gallery
                    if (path == null) {
                        path = mImageCaptureUri.getPath(); // From File Manager
                    }
                    Log.e("Activity", "PathHolder22= " + path);
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    String file;
                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file = "";
                    }
                    if (TextUtils.isEmpty(file)) {
                        Toast.makeText(AddPendingReasonActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);

                        File file1 = Utility.saveFile(bitmap, complaintListModel.getCstname(), "complaintImages");
                        UpdateArrayList(file1.getPath());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setBillNo(complaintListModel.getCmpno());
        imageModel.setLatitude("");
        imageModel.setLongitude("");
        imageModel.setSelectedCategory(imageArrayList.get(selectedIndex).getSelectedCategory());
        imageModel.setPosition(imageArrayList.get(selectedIndex).getPosition());
        imageArrayList.set(selectedIndex, imageModel);

        if (isUpdate) {
            databaseHelper.updateImagesData(imageModel, true, DatabaseHelper.TABLE_PENDING_REASON_IMAGE_DATA);
        } else {
            databaseHelper.insertImagesData(imageModel, true, DatabaseHelper.TABLE_PENDING_REASON_IMAGE_DATA);
        }

        customAdapter.notifyDataSetChanged();

    }

    /*--------------------------------------------Check Permission-------------------------------------------------------*/

    @Override
    protected void onResume() {
        super.onResume();
        CheakPermissions();
    }

    private void CheakPermissions() {
        if (!checkPermission()) {
            requestPermission();
        }

    }

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.pendingReasonSpinner) {
            if (!parent.getSelectedItem().toString().equals(getResources().getString(R.string.selectPendingReason))) {
                selectedPendingReason = pendingReasonList.get(position).getId();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getPendingReasonList() {
        pendingReasonList = new ArrayList<>();

        Utility.showProgressDialogue(this);
        Call<PendingReasonModel> call3 = apiInterface.getPendingReasonList(Utility.getSharedPreferences(this, Constant.accessToken));
        call3.enqueue(new Callback<PendingReasonModel>() {
            @Override
            public void onResponse(@NonNull Call<PendingReasonModel> call, @NonNull Response<PendingReasonModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    PendingReasonModel pendingReasonModel = response.body();
                    if (pendingReasonModel.getStatus().equals(Constant.TRUE)) {

                        if (pendingReasonModel.getPendingReason().size() > 0) {
                            for (int i = 0; i < pendingReasonModel.getPendingReason().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_PENDING_REASON_DATA, DatabaseHelper.KEY_ID, pendingReasonModel.getPendingReason().get(i).getCmpPenRe())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(pendingReasonModel.getPendingReason().get(i).getCmpPenRe());
                                    spinnerDataModel.setName(pendingReasonModel.getPendingReason().get(i).getName());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_PENDING_REASON_DATA);
                                }
                            }
                            setAdapter();
                        }
                    }else if (complaintListModel.getStatus().equals(Constant.FALSE)){
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong),getApplicationContext());
                    } else if (pendingReasonModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<PendingReasonModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setAdapter() {
        pendingReasonList.add(new SpinnerDataModel("00", getResources().getString(R.string.selectPendingReason)));
        pendingReasonList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_PENDING_REASON_DATA));
        SpinnerAdapter spinnerAdapter = new com.shaktipumplimted.serviceapp.Utils.common.adapter.SpinnerAdapter(AddPendingReasonActivity.this, pendingReasonList);
        pendingReasonSpinner.setAdapter(spinnerAdapter);

    }


    public void addPendingReason() {
        Utility.showProgressDialogue(this);
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cmpno", complaintListModel.getCmpno());
            jsonObject.put("follow_up_date", selectedFollowUpDate);
            jsonObject.put("reason", actionExt.getText().toString().trim());
            jsonObject.put("CMP_PEN_RE", selectedPendingReason);
            jsonObject.put("latitude", gpsTracker.getLatitude());
            jsonObject.put("longitude", gpsTracker.getLongitude());
            jsonObject.put("cr_date", Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", Utility.getCurrentDate()));
            jsonObject.put("cr_time", Utility.getFormattedTime("hh:mm a", "hhmmss", Utility.getCurrentTime()));
            jsonObject.put("cmpln_status", Constant.REPLY);

            for (int i = 0; i < imageArrayList.size(); i++) {
                if (imageArrayList.get(i).isImageSelected()) {
                    jsonObject.put("photo" + imageArrayList.get(i).getPosition(), Utility.getBase64FromPath(getApplicationContext(), imageArrayList.get(i).getImagePath()));
                }
            }
            jsonArray.put(jsonObject);
            uploadImageAPIS.setActionListener(jsonArray, Constant.addPendingImage, new ActionListenerCallback() {
                @Override
                public void onActionSuccess(String result) {
                    Utility.hideProgressDialogue();
                    CommonRespModel commonRespModel = new Gson().fromJson(result, CommonRespModel.class);
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                        databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_PENDING_REASON_IMAGE_DATA, DatabaseHelper.KEY_IMAGE_BILL_NO, complaintListModel.getCmpno());
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
}