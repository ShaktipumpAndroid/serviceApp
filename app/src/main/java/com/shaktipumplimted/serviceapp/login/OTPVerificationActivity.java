package com.shaktipumplimted.serviceapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.MainActivity;
import com.shaktipumplimted.serviceapp.otpReader.BroadcastRecieverSMS.MySMSBroadcastReceiver;
import com.shaktipumplimted.serviceapp.otpReader.BroadcastRecieverSMS.OTPReceiveListener;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar mToolbar;
    TextView submitOtpBtn, resendOtp;
    TextInputEditText otpExt;
    String verificationCode, contactNumber;
    APIInterface apiInterface2;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        Init();
        retriveValue();
        listner();
    }


    private void Init() {
        apiInterface2 = APIClient.getRetrofitOTP(getApplicationContext()).create(APIInterface.class);

        mToolbar = findViewById(R.id.toolbar);
        otpExt = findViewById(R.id.otpExt);
        resendOtp = findViewById(R.id.resendOtp);
        submitOtpBtn = findViewById(R.id.submitOtpBtn);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getResources().getString(R.string.otpVerification));

        startSMSListener();
     }
    private void listner() {
        submitOtpBtn.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void retriveValue() {
        if(getIntent().getExtras()!=null){
            contactNumber = getIntent().getStringExtra(Constant.mobileNumber);
            verificationCode = getIntent().getStringExtra(Constant.verificationCode);
        }
    }
    /*-----------------------------------------------On Click-------------------------------------------------------*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitOtpBtn:
                if(verificationCode.equals(otpExt.getText().toString().trim())){
                    ShowAlertResponse("1");
                }else {
                    Utility.ShowToast(getResources().getString(R.string.otpIncorrect),getApplicationContext());
                }
                break;
            case R.id.resendOtp:

                resendOTP(Utility.getOtp());
                break;
        }
    }

    /*-----------------------------------------------Send OTP-------------------------------------------------------*/
    private void resendOTP(String otp) {
        verificationCode = otp;
        Utility.showProgressDialogue(OTPVerificationActivity.this);
        Call<ResponseBody> call3 = apiInterface2.sendOTP(contactNumber,
                "Enter The Following OTP To Verify Your Account "+otp+" "+Utility.getHashKey(getApplicationContext()) +" SHAKTI", "SHAKTl", "0", "2", "91", "1707161675029844457");


        call3.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e("response==>", String.valueOf(response));
                Utility.hideProgressDialogue();
                if(response.isSuccessful()) {
                    ShowAlertResponse("0");
                    otpExt.setText("");
                    startSMSListener();
                }else {
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

    private void ShowAlertResponse(String value) {
        LayoutInflater inflater = (LayoutInflater) OTPVerificationActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(OTPVerificationActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        if (value.equals("0")) {
            title_txt.setText(getResources().getString(R.string.otp_send_successfully));
        } else {
            title_txt.setText(getResources().getString(R.string.verificationCodeMatched));
        }

        OK_txt.setOnClickListener(v -> {
                alertDialog.dismiss();
                if(value.equals("1")){
                    Intent intent = new Intent(OTPVerificationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

        });

    }
/*-----------------------------------------------SMS Reader-------------------------------------------------------*/
    private void startSMSListener() {
        MySMSBroadcastReceiver smsReceiver = new MySMSBroadcastReceiver();
        smsReceiver.setOTPListener(new OTPReceiveListener() {
            @Override
            public void onOTPReceived(String otp) {
                otpExt.setText(otp);
            }

            @Override
            public void onOTPTimeOut() {

            }
        });

        SmsRetrieverClient client = SmsRetriever.getClient(this);

        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(aVoid -> {
            // API successfully started
            //SMSBroadcastReceiver started listenting for sms
            Log.d("smslistener", "API successfully started");

        });
        task.addOnFailureListener(e -> {
            // Fail to start API
            e.printStackTrace();
        });
    }

}