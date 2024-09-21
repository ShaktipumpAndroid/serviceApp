package com.shaktipumplimted.serviceapp.main.bootomTabs.profile;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.LocalConveyanceActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.CustomerDetailActivity;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    View view;
    Context mContext;
    TextView userNameTxt, userEmailTxt, appVersionTxt;
    LinearLayout attendanceLinear, dsrEntryLinear, localConveyanceLinear, reportLinear, logoutLinear;
    PackageInfo pInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Init(view);
        listner();
        return view;
    }

    private void listner() {
        attendanceLinear.setOnClickListener(this);
        dsrEntryLinear.setOnClickListener(this);
        localConveyanceLinear.setOnClickListener(this);
        reportLinear.setOnClickListener(this);
        logoutLinear.setOnClickListener(this);
    }

    private void Init(View view) {
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userEmailTxt = view.findViewById(R.id.userEmailTxt);
        appVersionTxt = view.findViewById(R.id.appVersionTxt);
        attendanceLinear = view.findViewById(R.id.attendanceLinear);
        dsrEntryLinear = view.findViewById(R.id.dsrEntryLinear);
        localConveyanceLinear = view.findViewById(R.id.localConveyanceLinear);
        reportLinear = view.findViewById(R.id.reportLinear);
        logoutLinear = view.findViewById(R.id.logoutLinear);

        try {
             pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
        userNameTxt.setText(Utility.getSharedPreferences(getActivity(), Constant.userName));
        userEmailTxt.setText(Utility.getSharedPreferences(getActivity(), Constant.userEmail));
        appVersionTxt.setText(getResources().getString(R.string.app_version)+" "+pInfo.versionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attendanceLinear:

                break;
            case R.id.dsrEntryLinear:

                break;
            case R.id.localConveyanceLinear:
                Intent intent = new Intent(getActivity(), LocalConveyanceActivity.class);
                startActivity(intent);

                break;
            case R.id.reportLinear:

                break;
            case R.id.logoutLinear:
                     logoutPopup(getResources().getString(R.string.sureWantToLogout));
                break;
        }
    }

    private void logoutPopup(String message) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.alert_popup,
                    null);
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme);

            builder.setView(layout);
            builder.setCancelable(false);
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show();


            TextView okBtn = layout.findViewById(R.id.okBtn);
            TextView cancelBtn = layout.findViewById(R.id.cancelBtn);
            TextView messageTxt = layout.findViewById(R.id.messageTxt);

            messageTxt.setText(message);

            okBtn.setOnClickListener(v -> {
                Utility.logout(mContext);
            });

            cancelBtn.setOnClickListener(v -> alertDialog.dismiss());
        }

}