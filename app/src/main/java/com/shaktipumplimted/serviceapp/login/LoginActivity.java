package com.shaktipumplimted.serviceapp.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.login.model.LoginRespModel;
import com.shaktipumplimted.serviceapp.main.MainActivity;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userNameExt, passwordExt;
    TextView loginBtn;
    RelativeLayout loginRl, login_type_layout;
    APIInterface apiInterface;
    String fcmToken = "";
    Spinner login_type_spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
        listner();
    }

    private void listner() {
        loginBtn.setOnClickListener(this);
    }

    private void Init() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        userNameExt = findViewById(R.id.userNameExt);
        passwordExt = findViewById(R.id.passwordExt);
        loginBtn = findViewById(R.id.loginBtn);
        loginRl = findViewById(R.id.loginRl);
        login_type_spinner = findViewById(R.id.login_type_spinner);
        login_type_layout = findViewById(R.id.login_type_layout);
        if(Utility.isOnRoleApp()){
            login_type_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                if (userNameExt.getText().toString().isEmpty()) {
                    Snackbar.make(loginRl, getResources().getString(R.string.enter_sap_code), Snackbar.LENGTH_LONG).show();
                } else if (passwordExt.getText().toString().isEmpty()) {
                    Snackbar.make(loginRl, getResources().getString(R.string.enter_password), Snackbar.LENGTH_LONG).show();
                } else {
                    if (Utility.isInternetOn(getApplicationContext())) {
                        loginAPI();
                    } else {
                        Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
                    }
                }
                break;
        }
    }

    private void loginAPI() {
        String loginType = Constant.employee;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Utility.showProgressDialogue(this);
            Call<LoginRespModel> call3 = apiInterface.login(userNameExt.getText().toString().trim(),
                    loginType,
                    Constant.OnRoll,
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
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}