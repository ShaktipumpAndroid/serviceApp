package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.adapter.ComplaintForwardAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.model.CompForwardListModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintForwardActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, ComplaintForwardAdapter.ItemClickListener {

    Toolbar toolbar;
    RadioButton serviceCenterRadio, freelancerRadio, solarInstPartnerRadio;

    RecyclerView recyclerview;
    TextView noDataFound;
    RelativeLayout searchRelative;
    SearchView searchUser;
    List<CompForwardListModel> compForwardPersonList;

    ComplaintForwardAdapter complaintForwardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_forward);

        Init();
        listner();
    }



    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        serviceCenterRadio = findViewById(R.id.serviceCenterRadio);
        freelancerRadio = findViewById(R.id.freelancerRadio);
        solarInstPartnerRadio = findViewById(R.id.solarInstPartnerRadio);
        recyclerview = findViewById(R.id.recyclerview);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complaintForward));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        searchViewMethod();
    }

    private void listner() {
        serviceCenterRadio.setOnCheckedChangeListener(this);
        freelancerRadio.setOnCheckedChangeListener(this);
        solarInstPartnerRadio.setOnCheckedChangeListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void searchViewMethod() {
        searchRelative.setOnClickListener(v -> {
            searchUser.setFocusableInTouchMode(true);
            searchUser.requestFocus();
            searchUser.onActionViewExpanded();

        });

        ImageView searchIcon = searchUser.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_search_24));
        searchIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));

        ImageView searchClose = searchUser.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(getResources().getColor(R.color.colorPrimary));


        EditText searchEditText = searchUser.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._14sdp));

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (complaintForwardAdapter != null) {
                    if(!query.isEmpty()) {
                        complaintForwardAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (complaintForwardAdapter != null) {
                    if(!newText.isEmpty()) {
                        complaintForwardAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(v -> {

            searchUser.onActionViewCollapsed();
            if (complaintForwardAdapter != null) {
                complaintForwardAdapter.getFilter().filter("");
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView.getId()==R.id.serviceCenterRadio){
            if(isChecked) {
                getList("1");
            }
        } else if(buttonView.getId()==R.id.freelancerRadio){
            if(isChecked) {
                getList("2");
            }
        } else if(buttonView.getId()==R.id.solarInstPartnerRadio){
            if(isChecked) {
                getList("3");
            }
        }
    }

    private void getList(String isSelectedId) {

        Log.e("isSelectedId====>",isSelectedId);
        compForwardPersonList = new ArrayList<>();
        compForwardPersonList.add(new CompForwardListModel("1700486","Shree Patidar Rewinding works",isSelectedId));
        compForwardPersonList.add(new CompForwardListModel("1700223","Seema Electricals - Badnagar",isSelectedId));
        compForwardPersonList.add(new CompForwardListModel("1700153","Pushpa Enterprises -Badnager",isSelectedId));
        compForwardPersonList.add(new CompForwardListModel("1700003","New Patidar & Patidar",isSelectedId));


        if(compForwardPersonList.size()>0){
            complaintForwardAdapter = new ComplaintForwardAdapter(this,compForwardPersonList,noDataFound);
            recyclerview.setHasFixedSize(true);
            complaintForwardAdapter.ItemClick(this);
            recyclerview.setAdapter(complaintForwardAdapter);
            noDataFound.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
        }else {
            noDataFound.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }

    }

    @Override
    public void SetOnItemClickListener(CompForwardListModel response, int position) {

        complaintForward(response,getResources().getString(R.string.want_to_forward_person));
    }

    private void complaintForward(CompForwardListModel response, String message) {
        LayoutInflater inflater = (LayoutInflater) ComplaintForwardActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alert_popup,
                null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ComplaintForwardActivity.this, R.style.MyDialogTheme);

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

        okBtn.setOnClickListener(v -> alertDialog.dismiss());

        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());

    }
}