package com.shaktipumplimted.serviceapp.Utils.common.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.GpsTracker;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class SurfaceCameraActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    Camera.PictureCallback jpegCallback;
    ImageView captureBtn;
    TextView displayTxt;

    String latitude, longitude,customerName = "", complaintNo = "",frontCamera, Address = "";
    Bitmap bitmap;
    File file;
    GpsTracker gps;

    DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        Init();
        listner();
        retrieveValue();
    }

    private void retrieveValue() {
        if(getIntent().getExtras()!=null){
            customerName = getIntent().getStringExtra(Constant.customerName);
           if(getIntent().getStringExtra(Constant.complaintNo)!=null) {
               complaintNo = getIntent().getStringExtra(Constant.complaintNo);
           }
            if(getIntent().getStringExtra(Constant.frontCamera)!=null) {
                frontCamera = getIntent().getStringExtra(Constant.frontCamera);
            }

        }
        getGpsLocation();
    }


    private void Init() {
        gps = new GpsTracker(this);

        surfaceView = findViewById(R.id.surfaceView);
        captureBtn = findViewById(R.id.captureBtn);
        displayTxt = findViewById(R.id.displayTxt);

        decimalFormat = new DecimalFormat("##.######");
        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        pictureCallBack();
    }

    private void pictureCallBack() {
        jpegCallback = (data, camera) -> {
                bitmap = Utility.saveImageWithTimeStamp(SurfaceCameraActivity.this,data,displayTxt.getText().toString());
                file = Utility.saveFile(bitmap, customerName.trim(),Constant.Images);
            refreshCamera();
            onBackPressed();
        };

    }

    private void listner() {
        captureBtn.setOnClickListener(this);
    }


    /*------------------------------------------------------------------------Retrieve lat long---------------------------------------------------------------------*/

    public void getGpsLocation() {


        if (gps.canGetLocation()) {
            if (!String.valueOf(gps.getLatitude()).isEmpty() && !String.valueOf(gps.getLongitude()).isEmpty()) {


                latitude = decimalFormat.format(gps.getLatitude());
                longitude = decimalFormat.format(gps.getLongitude());
                setAddress();
            }
        } else {
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.captureBtn:
                if (!displayTxt.getText().toString().isEmpty()) {
                    captureImage();
                    captureBtn.setEnabled(false);
                    //enable button after 1000 millisecond
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        captureBtn.setEnabled(true);
                    }, 5000);
                } else {
                    Utility.ShowToast( getResources().getString(R.string.fetch_your_location),SurfaceCameraActivity.this);
                }

                break;
        }
    }

    private void captureImage() {
        if (camera != null) {
            camera.takePicture(null, null, jpegCallback);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            // open the camera
            if(frontCamera.equals("0")){
                camera = Camera.open(0);
            }else {
                camera = Camera.open();
            }

        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();

        // modify parameter
        List<Camera.Size> sizes = param.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        param.setPreviewSize(selected.width,selected.height);
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
}

    private void setAddress() {


        if (Utility.isInternetOn(getApplicationContext())) {
            Address = Utility.getAddressFromLatLng(SurfaceCameraActivity.this,latitude,longitude);

            if (complaintNo != null && !complaintNo.isEmpty()) {
                displayTxt.setText(Address + "\n" + "Date: " + Utility.getCurrentDate() + "\n" + "Time: " + Utility.getCurrentTime()
                        + "\n" + "Customer: " + customerName + "\n" + "Complaint No: " + complaintNo);
            }else {
                displayTxt.setText(Address + "\n" + "Date: " + Utility.getCurrentDate() + "\n" + "Time: " + Utility.getCurrentTime()
                        + "\n" + "Customer: " + customerName);
            }
        }else {
            if (complaintNo != null && !complaintNo.isEmpty()) {

                displayTxt.setText(" Latitude : " + String.valueOf(latitude) + "\n" +
                        "Longitude : " + String.valueOf(longitude) + "\n" + "Date: " + Utility.getCurrentDate() + "\n" + "Time: " + Utility.getCurrentTime()
                        + "\n" + "Customer: " + customerName+ "\n" + "Complaint No: " + complaintNo);
            }else {
                displayTxt.setText(" Latitude : " + String.valueOf(latitude) + "\n" +
                        "Longitude : " + String.valueOf(longitude) + "\n" + "Date: " + Utility.getCurrentDate() + "\n" + "Time: " + Utility.getCurrentTime()
                        + "\n" + "Customer: " + customerName);
            }
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onBackPressed() {

        if (file != null) {
            Intent intent = new Intent();
            intent.putExtra(Constant.file, file);
            intent.putExtra(Constant.latitude, String.valueOf(latitude));
            intent.putExtra(Constant.longitude, String.valueOf(longitude));
            intent.putExtra(Constant.address, Address);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Click Again", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }
}
