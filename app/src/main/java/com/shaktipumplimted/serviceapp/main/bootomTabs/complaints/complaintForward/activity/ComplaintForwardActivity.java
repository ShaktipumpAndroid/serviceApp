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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.model.CommonRespModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.adapter.ComplaintForwardAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.model.CompForwardListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.DSREntryActivity;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintForwardActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, ComplaintForwardAdapter.ItemClickListener {

    Toolbar toolbar;
    RadioButton serviceCenterRadio, freelancerRadio, solarInstPartnerRadio;
    SwipeRefreshLayout pullToRefresh;
    RecyclerView recyclerview;
    TextView noDataFound;
    RelativeLayout searchRelative;
    SearchView searchUser;
    List<CompForwardListModel.Response> compForwardPersonList;

    ComplaintForwardAdapter complaintForwardAdapter;

    APIInterface apiInterface;
    DatabaseHelper databaseHelper;
    ComplaintListModel.Datum complaintListModel;
    String isSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_forward);

        Init();
        listner();
        retrieveValue();
    }

    private void retrieveValue() {
        if (getIntent().getExtras() != null) {
            complaintListModel = (ComplaintListModel.Datum) getIntent().getSerializableExtra(Constant.complaintData);
        }
    }


    private void Init() {
        apiInterface = APIClient.getRetrofit(getApplicationContext()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        serviceCenterRadio = findViewById(R.id.serviceCenterRadio);
        freelancerRadio = findViewById(R.id.freelancerRadio);
        solarInstPartnerRadio = findViewById(R.id.solarInstPartnerRadio);
        recyclerview = findViewById(R.id.recyclerview);
        noDataFound = findViewById(R.id.noDataFound);
        searchUser = findViewById(R.id.searchUser);
        searchRelative = findViewById(R.id.searchRelative);
        pullToRefresh = findViewById(R.id.pullToRefresh);
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
        pullToRefresh.setOnRefreshListener(() -> {
            if (Utility.isInternetOn(getApplicationContext())) {
                if (!isSelected.isEmpty()) {
                    pullToRefresh.setRefreshing(true);
                    getPersonsList();
                } else {
                    pullToRefresh.setRefreshing(false);
                }
            } else {
                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
                pullToRefresh.setRefreshing(false);
            }
        });
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

        if (buttonView.getId() == R.id.serviceCenterRadio) {
            if (isChecked) {
                isSelected = "01";
                getList();
            }
        } else if (buttonView.getId() == R.id.solarInstPartnerRadio) {
            if (isChecked) {
                isSelected = "02";
                getList();
            }
        } else if (buttonView.getId() == R.id.freelancerRadio) {
            if (isChecked) {
                isSelected = "03";
                getList();
            }
        }
    }

    private void getList() {
        if (Utility.isInternetOn(getApplicationContext())) {
            if(databaseHelper.isDataAvailable(DatabaseHelper.TABLE_COMPLAINT_FORWARD_PERSON_DATA)){
                if(databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_FORWARD_PERSON_DATA,DatabaseHelper.KEY_STATUS_SELECTED,isSelected)) {
                    setAdapter();
                }else {
                    getPersonsList();
                }
            }else {
                getPersonsList();
            }
        } else {
            setAdapter();
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
        }
    }


    @Override
    public void SetOnItemClickListener(CompForwardListModel.Response response, int position) {

        complaintForward(response, getResources().getString(R.string.want_to_forward_person));
    }

    private void complaintForward(CompForwardListModel.Response response, String message) {
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

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveData(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());

    }

    private void saveData(CompForwardListModel.Response forwardModel) throws JSONException {
        Utility.showProgressDialogue(this);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmpno", complaintListModel.getCmpno());
        if(serviceCenterRadio.isChecked()){
            jsonObject.put("forward_to","Service Center");
        } else if (freelancerRadio.isChecked()) {
            jsonObject.put("forward_to","Freelancer");
        } else if (solarInstPartnerRadio.isChecked()) {
            jsonObject.put("forward_to","Solar installer");
        }
        jsonObject.put("cmp_pen_re", "");
        jsonObject.put("forward_to_code", forwardModel.getPartnerCode());


        jsonArray.put(jsonObject);


        Call<CommonRespModel> call3 = apiInterface.forwardComplaint(Utility.getSharedPreferences(this, Constant.accessToken), jsonArray.toString());
        call3.enqueue(new Callback<CommonRespModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonRespModel> call, @NonNull Response<CommonRespModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    CommonRespModel commonRespModel = response.body();
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                        onBackPressed();
                        Utility.ShowToast(commonRespModel.getMessage(), getApplicationContext());
                    } else if (commonRespModel.getStatus().equals(Constant.FALSE)) {
                        Utility.hideProgressDialogue();
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong), ComplaintForwardActivity.this);
                    } else if (commonRespModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonRespModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());
            }
        });
    }


    private void getPersonsList() {
        compForwardPersonList = new ArrayList<>();

        Utility.showProgressDialogue(this);
        Call<CompForwardListModel> call3 = apiInterface.complaintForwardPersonList(Utility.getSharedPreferences(this, Constant.accessToken), isSelected, complaintListModel.getCmpno());
        call3.enqueue(new Callback<CompForwardListModel>() {
            @Override
            public void onResponse(@NonNull Call<CompForwardListModel> call, @NonNull Response<CompForwardListModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    CompForwardListModel compForwardListModel = response.body();
                    if (compForwardListModel.getStatus().equals(Constant.TRUE)) {

                        if (compForwardListModel.getResponse().size() > 0) {
                            for (int i = 0; i < compForwardListModel.getResponse().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_FORWARD_PERSON_DATA, DatabaseHelper.KEY_PERSON_CODE, compForwardListModel.getResponse().get(i).getPartnerCode())) {
                                    CompForwardListModel.Response compForwardModel = new CompForwardListModel.Response();
                                    compForwardModel.setPartnerCode(compForwardListModel.getResponse().get(i).getPartnerCode());
                                    compForwardModel.setPartnerName(compForwardListModel.getResponse().get(i).getPartnerName());
                                    compForwardModel.setIsSelected(isSelected);
                                    databaseHelper.insertComplaintForwardPersonData(compForwardModel);
                                }
                            }
                        }
                        setAdapter();

                    } else if (compForwardListModel.getStatus().equals(Constant.FALSE)) {
                        pullToRefresh.setRefreshing(false);
                        noDataFound.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    } else if (compForwardListModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getApplicationContext());

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<CompForwardListModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setAdapter() {
        pullToRefresh.setRefreshing(false);
        compForwardPersonList = databaseHelper.getComplaintForwardPersonList(isSelected);
        if (!compForwardPersonList.isEmpty()) {
            complaintForwardAdapter = new ComplaintForwardAdapter(this, compForwardPersonList, noDataFound);
            recyclerview.setHasFixedSize(true);
            complaintForwardAdapter.ItemClick(this);
            recyclerview.setAdapter(complaintForwardAdapter);
            noDataFound.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
        } else {
            noDataFound.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
    }
}