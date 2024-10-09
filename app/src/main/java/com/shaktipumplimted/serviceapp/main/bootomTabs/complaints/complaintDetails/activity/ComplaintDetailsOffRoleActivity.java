package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.activity;

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
import android.widget.ImageView;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.shaktipumplimted.serviceapp.main.MainActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.model.ComplaintDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintDetailsOffRoleActivity extends AppCompatActivity implements ImageSelectionAdapter.ImageSelectionListener, View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;
    Toolbar toolbar;
    TextInputEditText complaintNo, customerName, customerMobileNo, customerAddress, materialCodeTxt, materialNameTxt,
            billNoTxt, billDateTxt, remarkTxt;
    TextView submitBtn;
    Spinner categorySpinner;
    List<SpinnerDataModel> complaintCategoryList = new ArrayList<>();
    String selectedCategory = "", imagePath = "", solarInstDistance = "";
    ComplaintListModel.Datum complaintListModel;
    ComplaintListModel.Datum complaintModel;
    DatabaseHelper databaseHelper;

    ImageView imgIcon;
    RelativeLayout startLocImg;
    GpsTracker gpsTracker;
    Context mContext;
    List<LocalConveyanceModel> localConveyanceList = new ArrayList<>();
    APIInterface apiInterface, apiInterface1;
    LocalConveyanceModel localConveyanceModel;
    RecyclerView complaintImgList;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();

    AlertDialog alertDialog;

    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();

    int selectedIndex;
    boolean isUpdate = false, isSelectedAllImages = false, isEndTravelImg = false;
    LocalConveyanceModel localConveyance;
    UploadImageAPIS uploadImageAPIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details_off_role);

        Init();
        listner();
        retrieveValue();
    }

    private void Init() {
        mContext = this;
        localConveyanceList = new ArrayList<>();
        uploadImageAPIS = new UploadImageAPIS(getApplicationContext());
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        apiInterface1 = APIClient.getRetrofitDirection(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(this);
        toolbar = findViewById(R.id.toolbar);
        complaintNo = findViewById(R.id.complaintNo);
        customerName = findViewById(R.id.customerName);
        customerMobileNo = findViewById(R.id.customerMobileNo);
        customerAddress = findViewById(R.id.customerAddress);
        materialCodeTxt = findViewById(R.id.materialCodeTxt);
        materialNameTxt = findViewById(R.id.materialNameTxt);
        billNoTxt = findViewById(R.id.billNoTxt);
        billDateTxt = findViewById(R.id.billDateTxt);
        remarkTxt = findViewById(R.id.remarkTxt);
        categorySpinner = findViewById(R.id.categorySpinner);
        complaintImgList = findViewById(R.id.complaintImgList);
        submitBtn = findViewById(R.id.submitBtn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complaintDetail));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        submitBtn.setOnClickListener(this);
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            complaintListModel = (ComplaintListModel.Datum) getIntent().getSerializableExtra(Constant.complaintData);

            complaintNo.setText(complaintListModel.getCmpno());
            customerName.setText(complaintListModel.getCstname());
            customerMobileNo.setText(complaintListModel.getMblno());
            customerAddress.setText(complaintListModel.getCaddress());
            materialCodeTxt.setText(complaintListModel.getMatnr());
            materialNameTxt.setText(complaintListModel.getMaktx());
            billNoTxt.setText(complaintListModel.getVbeln());
            billDateTxt.setText(complaintListModel.getFkdat());

            SetAdapter();
        }

        if (Utility.isInternetOn(getApplicationContext())) {
            if (databaseHelper.isDataAvailable(DatabaseHelper.TABLE_COMPLAINT_CATEGORY)) {
                setDropdown();
            } else {
                getDropdownsList();
            }
        } else {
            setDropdown();
        }
    }

    private void getDropdownsList() {
        complaintCategoryList = new ArrayList<>();

        Utility.showProgressDialogue(this);
        Call<ComplaintDropdownModel> call3 = apiInterface.getComplaintDropdowns(Utility.getSharedPreferences(this, Constant.accessToken));
        call3.enqueue(new Callback<ComplaintDropdownModel>() {
            @Override
            public void onResponse(@NonNull Call<ComplaintDropdownModel> call, @NonNull Response<ComplaintDropdownModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    ComplaintDropdownModel complaintDropdownModel = response.body();

                    if (complaintDropdownModel.getStatus().equals(Constant.TRUE)) {

                        if (complaintDropdownModel.getData().getComplainCategory().size() > 0) {

                            for (int i = 0; i < complaintDropdownModel.getData().getComplainCategory().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_CATEGORY, DatabaseHelper.KEY_ID, complaintDropdownModel.getData().getComplainCategory().get(i).getCatId())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(complaintDropdownModel.getData().getComplainCategory().get(i).getCatId());
                                    spinnerDataModel.setName(complaintDropdownModel.getData().getComplainCategory().get(i).getCategory());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_COMPLAINT_CATEGORY);
                                }
                            }

                        }

                        setDropdown();
                    } else if (complaintDropdownModel.getStatus().equals(Constant.FALSE)) {
                        Utility.hideProgressDialogue();
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong), ComplaintDetailsOffRoleActivity.this);
                    } else if (complaintDropdownModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ComplaintDropdownModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setDropdown() {
        complaintCategoryList.add(new SpinnerDataModel("00", getResources().getString(R.string.select_cmp_cat)));
        complaintCategoryList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_COMPLAINT_CATEGORY));

        setSpinnerAdapter(complaintCategoryList, categorySpinner);

        if (Utility.isFreelancerLogin(getApplicationContext())) {
            categorySpinner.setSelection(3);
            categorySpinner.setClickable(false);
            categorySpinner.setEnabled(false);
            selectedCategory = complaintCategoryList.get(3).getId();
        }

    }

    private void setSpinnerAdapter(List<SpinnerDataModel> spinnerList, Spinner spinner) {
        SpinnerAdapter spinnerAdapter = new com.shaktipumplimted.serviceapp.Utils.common.adapter.SpinnerAdapter(ComplaintDetailsOffRoleActivity.this, spinnerList);
        spinner.setAdapter(spinnerAdapter);
    }

    /*------------------------------------------------------------------------ImageList---------------------------------------------------------------------*/
    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.others));
        itemNameList.add(getResources().getString(R.string.old_prd_srnr));
        itemNameList.add(getResources().getString(R.string.site_error_defect));
        itemNameList.add(getResources().getString(R.string.discharge_img));
        itemNameList.add(getResources().getString(R.string.site_image_farmer_img));
        itemNameList.add(getResources().getString(R.string.fir_field));
        itemNameList.add(getResources().getString(R.string.cust_form));
        itemNameList.add(getResources().getString(R.string.approval_img));
        itemNameList.add(getResources().getString(R.string.CMC));


        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo(complaintListModel.getCmpno());
            imageModel.setPosition(i + 1);
            imageModel.setSelectedCategory(selectedCategory);
            imageArrayList.add(imageModel);
        }

        //Create Table
        imageList = databaseHelper.getAllImages(DatabaseHelper.TABLE_COMPLAINT_IMAGE_DATA, complaintListModel.getCmpno());

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

        customAdapter = new ImageSelectionAdapter(ComplaintDetailsOffRoleActivity.this, imageArrayList, false);
        complaintImgList.setHasFixedSize(true);
        complaintImgList.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);

    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        isEndTravelImg = false;
        if (imageModelList.isImageSelected()) {
            isUpdate = true;
            selectImage("1");
        } else {
            if (!selectedCategory.isEmpty()) {
                isUpdate = false;
                selectImage("0");
            } else {
                Utility.ShowToast(getResources().getString(R.string.select_category_first), getApplicationContext());
            }
        }

    }

    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) ComplaintDetailsOffRoleActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(ComplaintDetailsOffRoleActivity.this, R.style.MyDialogTheme);

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
                Intent i_display_image = new Intent(ComplaintDetailsOffRoleActivity.this, PhotoViewerActivity.class);
                i_display_image.putExtra(Constant.imagePath, imageArrayList.get(selectedIndex).getImagePath());
                i_display_image.putExtra(Constant.Images,"0");
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

        camraLauncher.launch(new Intent(ComplaintDetailsOffRoleActivity.this, SurfaceCameraActivity.class)
                .putExtra(Constant.frontCamera, "1")
                .putExtra(Constant.customerName, complaintListModel.getCstname()));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {

                        Bundle bundle = result.getData().getExtras();
                        Log.e("bundle====>", bundle.get(Constant.file).toString());

                        if (isEndTravelImg) {
                            imagePath = bundle.get(Constant.file).toString();
                            imgIcon.setImageResource(R.mipmap.tick_icon);
                            localConveyanceSavedOffline("1");
                        } else {
                            UpdateArrayList(bundle.get(Constant.file).toString(), bundle.getString(Constant.latitude), bundle.getString(Constant.longitude));

                        }
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
                    String path = FileUtils.getPath(ComplaintDetailsOffRoleActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(ComplaintDetailsOffRoleActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);

                        File file1 = Utility.saveFile(bitmap, complaintListModel.getCstname(), "complaintImages");
                        UpdateArrayList(file1.getPath(), "", "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void UpdateArrayList(String path, String latitude, String longitude) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageModel.setBillNo(complaintListModel.getCmpno());
        imageModel.setLatitude(latitude);
        imageModel.setLongitude(longitude);
        imageModel.setSelectedCategory(imageArrayList.get(selectedIndex).getSelectedCategory());
        imageModel.setPosition(imageArrayList.get(selectedIndex).getPosition());
        imageArrayList.set(selectedIndex, imageModel);

        if (isUpdate) {
            databaseHelper.updateImagesData(imageModel, true, DatabaseHelper.TABLE_COMPLAINT_IMAGE_DATA);
        } else {
            databaseHelper.insertImagesData(imageModel, true, DatabaseHelper.TABLE_COMPLAINT_IMAGE_DATA);
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

    /*-------------------------------------------------On Click------------------------------------------------------*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                   closeComplaint();
                break;

            case R.id.startLocImg:
                isEndTravelImg = true;
                if (checkPermission()) {
                    cameraIntent();
                } else {
                    requestPermission();
                }

                break;
        }

    }

    private void closeComplaint() {
        if (remarkTxt.getText().toString().trim().isEmpty()) {
            Utility.ShowToast(getResources().getString(R.string.CustomerCommentClosureRemark), getApplicationContext());
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
                startEndTravel();

            }
        }
    }

    private void startEndTravel() {
        gpsTracker = new GpsTracker(this);
        if (checkPermission()) {
            if (gpsTracker.canGetLocation()) {
                localConveyanceList = databaseHelper.getAllLocalConveyanceData(true);
                if (!localConveyanceList.isEmpty()) {
                    localConveyance = localConveyanceList.get(localConveyanceList.size() - 1);
                    localConveyanceSavedOffline("0");
                    if (Utility.isInternetOn(getApplicationContext())) {
                        if (complaintListModel.getLat() != null && !complaintListModel.getLat().isEmpty()) {
                            getCalculatedDistance(complaintListModel.getLat(), complaintListModel.getLng(),
                                    String.valueOf(gpsTracker.getLatitude()),
                                    String.valueOf(gpsTracker.getLongitude()));
                        } else {
                            getCalculatedDistance2(localConveyance.getStartLatitude(), localConveyance.getStartLongitude(),
                                    String.valueOf(gpsTracker.getLatitude()),
                                    String.valueOf(gpsTracker.getLongitude()));
                        }

                    } else {
                        Utility.ShowToast(getResources().getString(R.string.dataSavedLocally), getApplicationContext());
                        Utility.setSharedPreference(getApplicationContext(), Constant.localConveyanceJourneyStart, "false");
                        localConveyanceSavedOffline("0");
                    }
                }else {
                    Utility.ShowToast(getResources().getString(R.string.visitComplaint),getApplicationContext());
                }
                } else {
                    gpsTracker.showSettingsAlert();
                }

        } else {
            requestPermission();
        }
    }

    private void localConveyanceSavedOffline(String value) {
        localConveyanceModel = new LocalConveyanceModel();
        localConveyanceModel.setStartLatitude(localConveyance.getStartLatitude());
        localConveyanceModel.setStartLongitude(localConveyance.getStartLongitude());
        localConveyanceModel.setEndLatitude(String.valueOf(gpsTracker.getLatitude()));
        localConveyanceModel.setEndLongitude(String.valueOf(gpsTracker.getLongitude()));
        localConveyanceModel.setStartAddress(localConveyance.getStartAddress());
        localConveyanceModel.setEndAddress("");
        localConveyanceModel.setStartDate(localConveyance.getStartDate());
        localConveyanceModel.setStartTime(localConveyance.getStartTime());
        localConveyanceModel.setEndDate(Utility.getCurrentDate());
        localConveyanceModel.setEndTime(Utility.getCurrentTime());
        localConveyanceModel.setStartImgPath(localConveyance.getStartImgPath());
        localConveyanceModel.setEndImgPath(imagePath);
        localConveyanceModel.setTravelMode(complaintListModel.getCmpno());
        databaseHelper.updateLocalConveyanceData(localConveyanceModel);
        if(value.equals("0")){

        complaintSavedOffline();
        }
    }

    private void complaintSavedOffline() {
        complaintModel = new ComplaintListModel.Datum();
        complaintModel.setCmpno(complaintListModel.getCmpno());
        complaintModel.setCaddress(complaintListModel.getCaddress());
        complaintModel.setMblno(complaintListModel.getMblno());
        complaintModel.setCstname(complaintListModel.getCstname());
        complaintModel.setPernr(complaintListModel.getPernr());
        complaintModel.setEname(complaintListModel.getEname());
        complaintModel.setStatus(complaintListModel.getStatus());
        complaintModel.setMatnr(complaintListModel.getMatnr());
        complaintModel.setMaktx(complaintListModel.getMaktx());
        complaintModel.setVbeln(complaintListModel.getVbeln());
        complaintModel.setFkdat(complaintListModel.getFkdat());
        complaintModel.setFwrdTo(complaintListModel.getFwrdTo());
        complaintModel.setFdate(complaintListModel.getFdate());
        complaintModel.setAction(complaintListModel.getAction());
        complaintModel.setCmpPenRe(complaintListModel.getCmpPenRe());
        complaintModel.setLat(complaintListModel.getLat());
        complaintModel.setLng(complaintListModel.getLng());
        complaintModel.setCurrentStatus(complaintListModel.getCurrentStatus());
        complaintModel.setCurrentLat(String.valueOf(gpsTracker.getLatitude()));
        complaintModel.setCurrentLng(String.valueOf(gpsTracker.getLongitude()));
        complaintModel.setCustomerPay("");
        complaintModel.setCompanyPay("");
        complaintModel.setFocAmount("");
        complaintModel.setReturnByCompany("");
        complaintModel.setPayToFreelancer("");
        complaintModel.setPumpSrNo("");
        complaintModel.setMotorSrNo("");
        complaintModel.setControllerSrNo("");
        complaintModel.setCategory(selectedCategory);
        complaintModel.setClosureReason("");
        complaintModel.setDefectType("");
        complaintModel.setRelatedTo("");
        complaintModel.setRemark(remarkTxt.getText().toString().trim());
        complaintModel.setCurrentDate(Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", Utility.getCurrentDate()));
        complaintModel.setCurrentTime(Utility.getFormattedTime("hh:mm a", "HHmmss", Utility.getCurrentTime()));
        complaintModel.setDistance(complaintListModel.getDistance());
        complaintModel.setDataSavedLocally(true);
        databaseHelper.updateComplaintDetailsData(complaintModel);
    }

    private void getCalculatedDistance(String startLat, String startLong, String endLat, String endLong) {
        Utility.showProgressDialogue(this);
        Call<DistanceCalculateModel> call3 = apiInterface1.getDistance(startLat + "," + startLong,
                endLat + "," + endLong, Constant.APIKEY);
        call3.enqueue(new Callback<DistanceCalculateModel>() {
            @Override
            public void onResponse(@NonNull Call<DistanceCalculateModel> call, @NonNull Response<DistanceCalculateModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    DistanceCalculateModel distanceCalculateModel = response.body();

                  //  Log.e("distanceCalculate====>", String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText()));

                    solarInstDistance = String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText());

                    getCalculatedDistance2(localConveyance.getStartLatitude(), localConveyance.getStartLongitude(),
                            String.valueOf(gpsTracker.getLatitude()),
                            String.valueOf(gpsTracker.getLongitude()));

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

    private void getCalculatedDistance2(String startLat, String startLong, String endLat, String endLong) {
        Utility.showProgressDialogue(this);
        Call<DistanceCalculateModel> call3 = apiInterface1.getDistance(startLat + "," + startLong,
                endLat + "," + endLong, Constant.APIKEY);
        call3.enqueue(new Callback<DistanceCalculateModel>() {
            @Override
            public void onResponse(@NonNull Call<DistanceCalculateModel> call, @NonNull Response<DistanceCalculateModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    DistanceCalculateModel distanceCalculateModel = response.body();

                    saveTravelPopup(distanceCalculateModel.getRoutes(), startLat, startLong, endLat, endLong);
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

    private void saveTravelPopup(List<DistanceCalculateModel.Route> routes, String startLat, String startLong, String endLat, String endLong) {
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
        TextView cancelBtn = layout.findViewById(R.id.cancelBtn);


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

        startLatitudeExt.setText(startLat);

        startLongitudeExt.setText(startLong);
        startAddressExt.setText(routes.get(0).getLegs().get(0).getStartAddress());
        endLatitudeExt.setText(endLat);
        endLongitudeExt.setText(endLong);
        endAddressExt.setText(routes.get(0).getLegs().get(0).getEndAddress());
        distanceEdt.setText(routes.get(0).getLegs().get(0).getDistance().getText());
        travelModeEdt.setText(complaintListModel.getCmpno());
        travelModeEdt.setEnabled(false);

        confirmBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
              if(Utility.isInternetOn(getApplicationContext())) {
                  saveData(localConveyanceModel, distanceEdt.getText().toString().trim());
              }else {
                  Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),getApplicationContext());
              }

        });

        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());
        startLocImg.setOnClickListener(this);
    }


    private void saveData(LocalConveyanceModel response, String travelDistance) {
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
            jsonObject.put("end_location", Utility.getAddressFromLatLng(getApplicationContext(),response.getEndLatitude(),response.getEndLongitude()));
            jsonObject.put("travel_MODE",complaintListModel.getCmpno());
            jsonObject.put("distance", travelDistance);
            jsonObject.put("lat_long", response.getStartLatitude()+","+response.getStartLongitude());

            if(response.getStartImgPath()!=null &&!response.getStartImgPath().isEmpty()) {
                jsonObject.put("PHOTO1", Utility.getBase64FromPath(getApplicationContext(), response.getStartImgPath()));
            }else {
                jsonObject.put("PHOTO1","");
            }
            if(imagePath!=null && !imagePath.isEmpty()) {
                jsonObject.put("PHOTO2", Utility.getBase64FromPath(getApplicationContext(),imagePath));
            }else {
                jsonObject.put("PHOTO2","");
            }
            jsonArray.put(jsonObject);

            Log.e("json===>",jsonArray.toString());
            uploadImageAPIS.setActionListener(jsonArray, Constant.localConveyance, new ActionListenerCallback() {
                @Override
                public void onActionSuccess(String result) {
                    Utility.hideProgressDialogue();

                    CommonRespModel commonRespModel = new Gson().fromJson(result, CommonRespModel.class);
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {

                        databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_LOCAL_CONVEYANCE_DATA, DatabaseHelper.KEY_START_TIME, response.getStartTime());
                        closeComplaintAPI(complaintModel);
                        Utility.setSharedPreference(getApplicationContext(), Constant.localConveyanceJourneyStart, "false");
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

    private void closeComplaintAPI(ComplaintListModel.Datum complaintModel) {
        Utility.showProgressDialogue(this);
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cmpno", complaintModel.getCmpno());
            jsonObject.put("category", complaintModel.getCategory());
            jsonObject.put("closer_reason", complaintModel.getClosureReason());
            jsonObject.put("defect", complaintModel.getDefectType());
            jsonObject.put("cmpln_related_to", complaintModel.getRelatedTo());
            jsonObject.put("rm_code", Utility.getSharedPreferences(getApplicationContext(),Constant.reportingPersonName));
            jsonObject.put("ZFEEDRMRK", complaintModel.getRemark());
            jsonObject.put("ZFEEDF", complaintModel.getRemark());
            jsonObject.put("cr_date", complaintModel.getCurrentDate());
            jsonObject.put("cr_time", complaintModel.getCurrentTime());
            jsonObject.put("latitude", complaintModel.getCurrentLat());
            jsonObject.put("longitude", complaintModel.getCurrentLng());
            jsonObject.put("distance", solarInstDistance);


            for(int i=0; i<imageArrayList.size() ; i++){
                if(imageArrayList.get(i).getImagePath()!=null && !imageArrayList.get(i).getImagePath().isEmpty()){
                    jsonObject.put("photo"+imageArrayList.get(i).getPosition(), Utility.getBase64FromPath(getApplicationContext(),imageArrayList.get(i).getImagePath()));
                }
            }


            jsonArray.put(jsonObject);
            Log.e("json===>",jsonArray.toString());
            uploadImageAPIS.setActionListener(jsonArray, Constant.OffrollClosure, new ActionListenerCallback() {
                @Override
                public void onActionSuccess(String result) {
                    Utility.hideProgressDialogue();

                    CommonRespModel commonRespModel = new Gson().fromJson(result, CommonRespModel.class);
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                        databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_COMPLAINT_DATA, DatabaseHelper.KEY_COMPLAINT_NUMBER, complaintListModel.getCmpno());
                        databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_COMPLAINT_IMAGE_DATA,DatabaseHelper.KEY_IMAGE_BILL_NO, complaintListModel.getCmpno());
                        Utility.setSharedPreference(getApplicationContext(), Constant.localConveyanceJourneyStart, "false");
                        Utility.ShowToast(commonRespModel.getMessage(), getApplicationContext());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(Constant.APICALL,Constant.TRUE);
                        startActivity(intent);
                        finish();
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