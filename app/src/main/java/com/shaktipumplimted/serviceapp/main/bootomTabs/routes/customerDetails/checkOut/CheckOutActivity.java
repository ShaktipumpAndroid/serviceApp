package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.checkOut;

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
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.FileUtils;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.activity.PhotoViewerActivity;
import com.shaktipumplimted.serviceapp.Utils.common.activity.SurfaceCameraActivity;
import com.shaktipumplimted.serviceapp.Utils.common.adapter.ImageSelectionAdapter;
import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.activity.ComplaintDetailsActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.model.ComplaintDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.checkOut.model.CheckOutDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList.model.CustomerListModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity implements ImageSelectionAdapter.ImageSelectionListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int PICK_FROM_FILE = 102;

    Toolbar toolbar;
    DatabaseHelper databaseHelper;
    CustomerListModel customerListModel;
    Spinner activitySpinner;
    TextView followUpDateTxt, submitBtn;
    RelativeLayout followUpDateBtn;
    TextInputEditText remarkExt;
    RecyclerView checkOutImages;

    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    AlertDialog alertDialog;

    ImageSelectionAdapter customAdapter;

    List<String> itemNameList = new ArrayList<>();
    int selectedIndex;
    boolean isUpdate = false;
    String selectedFollowUpDate = "";
    APIInterface apiInterface;
    List<SpinnerDataModel> checkOutDropdownList  = new ArrayList<>();

    public final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    public final static SimpleDateFormat sendDateFormat =
            new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Init();
        listner();
        retrieveValue();
    }

    private void Init() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        activitySpinner = findViewById(R.id.activitySpinner);
        followUpDateTxt = findViewById(R.id.followUpDateTxt);
        followUpDateBtn = findViewById(R.id.followUpDateBtn);
        remarkExt = findViewById(R.id.remarkExt);
        checkOutImages = findViewById(R.id.checkOutImages);
        submitBtn = findViewById(R.id.submitBtn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.check_out));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        followUpDateTxt.setText(Utility.getCurrentDate());

    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        followUpDateBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        activitySpinner.setOnItemSelectedListener(this);
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            customerListModel = (CustomerListModel) getIntent().getSerializableExtra(Constant.customerDetails);
        }


        if (Utility.isInternetOn(getApplicationContext())) {
            if (databaseHelper.isDataAvailable(DatabaseHelper.TABLE_CHECK_OUT_DROPDOWN)) {
                setDropdown();
            } else {
                getDropdownsList();
            }
        } else {
            setDropdown();
        }
    }

    private void getDropdownsList() {
        checkOutDropdownList  = new ArrayList<>();
        Utility.showProgressDialogue(this);
        Call<CheckOutDropdownModel> call3 = apiInterface.getCheckOutDropdown(Utility.getSharedPreferences(this, Constant.accessToken));
        call3.enqueue(new Callback<CheckOutDropdownModel>() {
            @Override
            public void onResponse(@NonNull Call<CheckOutDropdownModel> call, @NonNull Response<CheckOutDropdownModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    CheckOutDropdownModel checkOutDropdownModel = response.body();

                    if (checkOutDropdownModel.getStatus().equals(Constant.TRUE)) {

                        if (checkOutDropdownModel.getResponse().size() > 0) {

                            for (int i = 0; i < checkOutDropdownModel.getResponse().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_CHECK_OUT_DROPDOWN, DatabaseHelper.KEY_ID, checkOutDropdownModel.getResponse().get(i).getHelpCode())) {
                                    SpinnerDataModel spinnerDataModel = new SpinnerDataModel();
                                    spinnerDataModel.setId(checkOutDropdownModel.getResponse().get(i).getHelpCode());
                                    spinnerDataModel.setName(checkOutDropdownModel.getResponse().get(i).getHelpName());
                                    databaseHelper.insertSpinnerData(spinnerDataModel, DatabaseHelper.TABLE_CHECK_OUT_DROPDOWN);
                                }
                            }

                        }
                        setDropdown();
                    } else if (checkOutDropdownModel.getStatus().equals(Constant.FALSE)) {
                        Utility.hideProgressDialogue();
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong), CheckOutActivity.this);
                    } else if (checkOutDropdownModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckOutDropdownModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setDropdown() {
        checkOutDropdownList.add(new SpinnerDataModel("00", getResources().getString(R.string.select_closure)));
        checkOutDropdownList.addAll(databaseHelper.getSpinnerData(DatabaseHelper.TABLE_CHECK_OUT_DROPDOWN));

        SpinnerAdapter spinnerAdapter = new com.shaktipumplimted.serviceapp.Utils.common.adapter.SpinnerAdapter(CheckOutActivity.this, checkOutDropdownList);
        activitySpinner.setAdapter(spinnerAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.followUpDateBtn:
                ChooseDate();
                break;
            case R.id.submitBtn:

                break;
        }
    }

    private void ChooseDate() {

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(CheckOutActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view.getId() == R.id.activitySpinner) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*------------------------------------------------------------------------ImageList---------------------------------------------------------------------*/
    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.frontView));
        itemNameList.add(getResources().getString(R.string.innerView));
        itemNameList.add(getResources().getString(R.string.visitingCardPhoto));
        itemNameList.add(getResources().getString(R.string.ownerPhoto));


        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageModel.setBillNo(customerListModel.getCustomerName());
            imageModel.setPosition(i + 1);
            imageArrayList.add(imageModel);
        }

        //Create Table
        imageList = databaseHelper.getAllImages(DatabaseHelper.TABLE_CHECK_OUT_IMAGE_DATA, customerListModel.getCustomerName());

        Log.e("imageList====>", String.valueOf(imageList.size()));
        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {
                    if (imageList.get(i).getBillNo() != null &&
                            imageList.get(i).getBillNo().trim().equals(customerListModel.getCustomerName())) {
                        if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setName(imageList.get(i).getName());
                            imageModel.setImagePath(imageList.get(i).getImagePath());
                            imageModel.setBillNo(imageList.get(i).getBillNo());
                            imageModel.setImageSelected(true);
                            imageModel.setPosition(imageList.get(i).getPosition());
                            imageArrayList.set(j, imageModel);
                        }
                    }
                }
            }
        }

        customAdapter = new ImageSelectionAdapter(CheckOutActivity.this, imageArrayList, false);
        checkOutImages.setHasFixedSize(true);
        checkOutImages.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);

    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;

        if (imageModelList.isImageSelected()) {
            isUpdate = true;
            selectImage("1");
        } else {
            isUpdate = false;
            selectImage("0");

        }

    }

    private void selectImage(String value) {
        LayoutInflater inflater = (LayoutInflater) CheckOutActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(CheckOutActivity.this, R.style.MyDialogTheme);

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
                Intent i_display_image = new Intent(CheckOutActivity.this, PhotoViewerActivity.class);
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

        camraLauncher.launch(new Intent(CheckOutActivity.this, SurfaceCameraActivity.class)
                .putExtra(Constant.frontCamera,"1")
                .putExtra(Constant.customerName, customerListModel.getCustomerName()));

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
                    String path = FileUtils.getPath(CheckOutActivity.this, mImageCaptureUri); // From Gallery
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
                        Toast.makeText(CheckOutActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                    } else {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);

                        File file1 = Utility.saveFile(bitmap, customerListModel.getCustomerName(), "siteSurveyImages");
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
        imageModel.setBillNo(customerListModel.getCustomerName());
        imageModel.setLatitude("");
        imageModel.setLongitude("");
        imageModel.setSelectedCategory(imageArrayList.get(selectedIndex).getSelectedCategory());
        imageModel.setPosition(imageArrayList.get(selectedIndex).getPosition());
        imageArrayList.set(selectedIndex, imageModel);

        if (isUpdate) {
            databaseHelper.updateImagesData(imageModel, true, DatabaseHelper.TABLE_CHECK_OUT_IMAGE_DATA);
        } else {
            databaseHelper.insertImagesData(imageModel, true, DatabaseHelper.TABLE_CHECK_OUT_IMAGE_DATA);
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


}