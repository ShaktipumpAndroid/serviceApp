package com.shaktipumplimted.serviceapp.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.login.LoginActivity;
import com.shaktipumplimted.serviceapp.main.MainActivity;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        inIt();
    }

    private void inIt() {

        new Handler().postDelayed(() -> {

            if(Utility.getSharedPreferences(getApplicationContext(), Constant.userID)!=null && !Utility.getSharedPreferences(getApplicationContext(), Constant.userID).isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }, 3000);

    }
}