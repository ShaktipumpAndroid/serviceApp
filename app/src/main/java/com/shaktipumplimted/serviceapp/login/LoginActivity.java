package com.shaktipumplimted.serviceapp.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.login.model.LoginRespModel;
import com.shaktipumplimted.serviceapp.main.MainActivity;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText userNameExt, passwordExt;
    TextView loginBtn;
    RelativeLayout loginRl, loginTypeLayout;
    APIInterface apiInterface;
    String fcmToken = "", loginType = "";
    Spinner loginTypeSpinner;
    List<SpinnerDataModel> loginTyepList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
        listner();
    }

    private void listner() {
        loginBtn.setOnClickListener(this);
        loginTypeSpinner.setOnItemSelectedListener(this);
    }

    private void Init() {
        loginTyepList = new ArrayList<>();
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        userNameExt = findViewById(R.id.userNameExt);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.loginTypeSpinner) {
            if (!loginTyepList.get(position).getName().equals(getResources().getString(R.string.selectLoginType))) {
                if (loginTyepList.get(position).getName().equals(Constant.serviceCenterTech)) {
                    loginType = "SRV_CNTR_T";
                }else {
                    loginType = loginTyepList.get(position).getName().trim();
                }
            }else {
                loginType = "";
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}