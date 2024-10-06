package com.shaktipumplimted.serviceapp.login;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.login.model.LoginRespModel;
import com.shaktipumplimted.serviceapp.main.MainActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.LocalConveyanceActivity;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int REQUEST_CODE_PERMISSION = 101;
    TextInputLayout userNameLayout, passwordLayout;
    TextInputEditText userNameExt, passwordExt;
    TextView loginBtn;
    RelativeLayout loginRl, loginTypeLayout;
    APIInterface apiInterface,apiInterface2;
    String fcmToken = "", loginType = "", verificationCode = "";
    Spinner loginTypeSpinner;
    List<SpinnerDataModel> loginTyepList;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
        listner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermission()) {
            requestPermission();
        }
    }

    private void listner() {
        loginBtn.setOnClickListener(this);
        loginTypeSpinner.setOnItemSelectedListener(this);
    }

    private void Init() {
        loginTyepList = new ArrayList<>();
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        apiInterface2 = APIClient.getRetrofitOTP(getApplicationContext()).create(APIInterface.class);
        userNameLayout = findViewById(R.id.userNameLayout);
        userNameExt = findViewById(R.id.userNameExt);
        passwordLayout = findViewById(R.id.passwordLayout);
        passwordExt = findViewById(R.id.passwordExt);
        loginBtn = findViewById(R.id.loginBtn);
        loginRl = findViewById(R.id.loginRl);
        loginTypeSpinner = findViewById(R.id.loginTypeSpinner);
        loginTypeLayout = findViewById(R.id.loginTypeLayout);
        if (!Utility.isOnRoleApp()) {
            loginTyepList.add(new SpinnerDataModel("00", getResources().getString(R.string.selectLoginType)));
            loginTyepList.add(new SpinnerDataModel("01", Constant.employee));
            loginTyepList.add(new SpinnerDataModel("02", Constant.freelancer));
            loginTyepList.add(new SpinnerDataModel("03", Constant.serviceCenterTech));

            SpinnerAdapter spinnerAdapter = new com.shaktipumplimted.serviceapp.Utils.common.adapter.SpinnerAdapter(getApplicationContext(), loginTyepList);
            loginTypeSpinner.setAdapter(spinnerAdapter);
        }else {
            loginTypeLayout.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:

                if(!Utility.isOnRoleApp()) {
                    if (loginType.toString().isEmpty()) {
                        Snackbar.make(loginRl, getResources().getString(R.string.selectLoginType), Snackbar.LENGTH_LONG).show();
                    } else if (loginType.trim().equals(Constant.SRV_CNTR_T)) {
                        if (userNameExt.getText().toString().isEmpty()) {
                            Snackbar.make(loginRl, getResources().getString(R.string.EnterRegisteredMobileNumber), Snackbar.LENGTH_LONG).show();
                        } else if (Utility.isValidMobile(userNameExt.getText().toString().trim())) {
                            apiCall(0);
                        } else {
                            Snackbar.make(loginRl, getResources().getString(R.string.enterValidMobile), Snackbar.LENGTH_LONG).show();
                        }

                    } else {
                        if (userNameExt.getText().toString().isEmpty()) {
                            Snackbar.make(loginRl, getResources().getString(R.string.enter_sap_code), Snackbar.LENGTH_LONG).show();
                        } else if (passwordExt.getText().toString().isEmpty()) {
                            Snackbar.make(loginRl, getResources().getString(R.string.enter_password), Snackbar.LENGTH_LONG).show();
                        } else {
                            apiCall(1);
                        }
                    }
                }else {
                    if (userNameExt.getText().toString().isEmpty()) {
                        Snackbar.make(loginRl, getResources().getString(R.string.enter_sap_code), Snackbar.LENGTH_LONG).show();
                    } else if (passwordExt.getText().toString().isEmpty()) {
                        Snackbar.make(loginRl, getResources().getString(R.string.enter_password), Snackbar.LENGTH_LONG).show();
                    } else {
                        apiCall(1);
                    }
                }

                break;
        }
    }

    private void apiCall(int value) {

        if (!checkPermission()) {
            requestPermission();
        } else {
            if (Utility.isInternetOn(getApplicationContext())) {
                if (value == 0) {
                    sendOTP(Utility.getOtp());
                } else {
                    loginAPI();
                }
            } else {
                Snackbar.make(loginRl, getResources().getString(R.string.checkInternetConnection), Snackbar.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.loginTypeSpinner) {
            if (!loginTyepList.get(position).getName().equals(getResources().getString(R.string.selectLoginType))) {
                if (loginTyepList.get(position).getName().equals(Constant.serviceCenterTech)) {
                    loginType = Constant.SRV_CNTR_T;
                    userNameLayout.setHint(getResources().getString(R.string.EnterRegisteredMobileNumber));
                    loginBtn.setText(getResources().getString(R.string.sendOTP));
                    passwordLayout.setVisibility(View.GONE);
                }else {
                    loginType = loginTyepList.get(position).getName().trim();
                    userNameLayout.setHint(getResources().getString(R.string.enter_sap_code));
                    loginBtn.setText(getResources().getString(R.string.loginTxt));
                    passwordLayout.setVisibility(View.VISIBLE);
                }
            } else {
                loginType = "";
                userNameLayout.setHint(getResources().getString(R.string.enter_sap_code));
                loginBtn.setText(getResources().getString(R.string.loginTxt));
                passwordLayout.setVisibility(View.VISIBLE);
            }

            Log.e("loginType==>", loginType);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*--------------------------------------------Login API------------------------------------------------------------*/
    private void loginAPI() {
        try {
            String objs = "";
            if (Utility.isOnRoleApp()) {
                objs = Constant.OnRoll;
                loginType = Constant.employee;
            } else {
                objs = Constant.OffRoll;
            }
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Utility.showProgressDialogue(this);
            Call<LoginRespModel> call3 = apiInterface.login(userNameExt.getText().toString().trim(),
                    loginType,
                    objs,
                    passwordExt.getText().toString().trim(), fcmToken, pInfo.versionName, String.valueOf(Build.VERSION.SDK_INT), Build.VERSION.RELEASE, Utility.getDeviceName());
            call3.enqueue(new Callback<LoginRespModel>() {
                @Override
                public void onResponse(@NonNull Call<LoginRespModel> call, @NonNull Response<LoginRespModel> response) {
                    Utility.hideProgressDialogue();
                    if (response.isSuccessful()) {
                        LoginRespModel loginRespModel = response.body();
                        if (loginRespModel.getLogin().equals("Y")) {
                            Utility.setSharedPreference(getApplicationContext(), Constant.userID, loginRespModel.getUserid());
                            Utility.setSharedPreference(getApplicationContext(), Constant.userName, loginRespModel.getUsername());
                            Utility.setSharedPreference(getApplicationContext(), Constant.userMobile, loginRespModel.getMobile());
                            Utility.setSharedPreference(getApplicationContext(), Constant.userEmail, loginRespModel.getEmail());
                            Utility.setSharedPreference(getApplicationContext(), Constant.reportingPersonSapId, loginRespModel.getReportingPersonSapID());
                            Utility.setSharedPreference(getApplicationContext(), Constant.reportingPersonName, loginRespModel.getReportingPersonName());
                            Utility.setSharedPreference(getApplicationContext(), Constant.accessToken, loginRespModel.getAccessToken());
                            Utility.setSharedPreference(getApplicationContext(), Constant.loginType, loginType);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Utility.ShowToast(getResources().getString(R.string.checkLoginPassword), getApplicationContext());
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<LoginRespModel> call, @NonNull Throwable t) {
                    call.cancel();
                    Utility.hideProgressDialogue();
                    Log.e("Error====>", t.getMessage().toString().trim());

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Utility.hideProgressDialogue();
        }


    }

    /*--------------------------------------------Send OTP API------------------------------------------------------------*/
    private void sendOTP(String otp) {
        verificationCode = otp;
        Utility.showProgressDialogue(LoginActivity.this);
        Call<ResponseBody> call3 = apiInterface2.sendOTP(userNameExt.getText().toString().trim(),
                "Enter The Following OTP To Verify Your Account "+otp+" "+Utility.getHashKey(getApplicationContext()) +" SHAKTI", "SHAKTl", "0", "2", "91", "1707161675029844457");

        call3.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e("response==>", String.valueOf(response));
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    ShowAlertResponse();
                } else {
                    if (response.errorBody() != null) {
                        String error = Utility.parseError(response);
                        Utility.ShowToast(error, getApplicationContext());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
            }
        });
    }

    private void ShowAlertResponse() {
        LayoutInflater inflater = (LayoutInflater) LoginActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);


        title_txt.setText(getResources().getString(R.string.otp_send_successfully));


        OK_txt.setOnClickListener(v -> {

                alertDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, OTPVerificationActivity.class);
                intent.putExtra(Constant.verificationCode,verificationCode);
                intent.putExtra(Constant.mobileNumber,userNameExt.getText().toString().trim());
                startActivity(intent);
                finish();

        });

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

                        if (!CoarseLocationAccepted && !Camera && !ReadMediaImages ) {
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