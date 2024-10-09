package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.activity.ComplaintDetailsActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.activity.ComplaintDetailsOffRoleActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater.ComplaintListAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater.ComplaintStatusAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintListFragment extends Fragment implements ComplaintStatusAdapter.ItemClickListener,ComplaintListAdapter.ItemClickListener {

    View view;
    Context mContext;
    RecyclerView compStatusList, compList;
    List<ComplaintStatusModel.Datum> complaintStatusArrayList;
    List<ComplaintListModel.Datum> complaintArrayList;

    ComplaintStatusAdapter complaintStatusAdapter;
    ComplaintListAdapter complaintListAdapter;

    RelativeLayout searchRelative;
    SearchView searchUser;
    TextView noDataFound;
    SwipeRefreshLayout pullToRefresh;
    int selectedPosition = 0, prevPosition =0;
    APIInterface apiInterface;
    DatabaseHelper databaseHelper;

    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_complaint_list, container, false);

        Init(view);




        return view;
    }



    private void Init(View view) {
        mContext = getActivity();
        apiInterface = APIClient.getRetrofit(getActivity()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(getActivity());
        compStatusList = view.findViewById(R.id.compStatusList);
        compList = view.findViewById(R.id.compList);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        noDataFound = view.findViewById(R.id.noDataFound);
        searchUser = view.findViewById(R.id.searchUser);
        searchRelative = view.findViewById(R.id.searchRelative);

        searchViewMethod();
        listner();
        retrieveValue();

    }

    private void listner() {
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(true);
            getcomplaintList();
        });
    }
    private void retrieveValue() {
        Bundle bundle = getArguments();
            if(bundle.getString(Constant.APICALL).equals(Constant.TRUE)){
               if(Utility.isInternetOn(getActivity())){
                   getcomplaintList();
               }else {
                   setStatusAdapter();
               }
            }else {
                getLists();
            }
      }


    private void getLists() {
        if(Utility.isInternetOn(mContext)){
            if(!databaseHelper.isDataAvailable(DatabaseHelper.TABLE_COMPLAINT_DATA)){
                getcomplaintList();
            }else{
                setStatusAdapter();
            }
        }else {
            setStatusAdapter();
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), mContext);
        }
    }

    private void searchViewMethod() {
        searchRelative.setOnClickListener(v -> {
            searchUser.setFocusableInTouchMode(true);
            searchUser.requestFocus();
            searchUser.onActionViewExpanded();

        });

        ImageView searchIcon = searchUser.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.baseline_search_24));
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
                if (complaintListAdapter != null) {
                    if(!query.isEmpty()) {
                        complaintListAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (complaintListAdapter != null) {
                    if(!newText.isEmpty()) {
                        complaintListAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(v -> {

            searchUser.onActionViewCollapsed();
            if (complaintListAdapter != null) {
                complaintListAdapter.getFilter().filter("");
            }
        });

    }





    private void getStatusList() {
        complaintStatusArrayList = new ArrayList<>();
       /* Utility.showProgressDialogue(getActivity());
        Call<ComplaintStatusModel> call3 = apiInterface.getStatusList(Utility.getSharedPreferences(getActivity(), Constant.accessToken));
        call3.enqueue(new Callback<ComplaintStatusModel>() {
            @Override
            public void onResponse(@NonNull Call<ComplaintStatusModel> call, @NonNull Response<ComplaintStatusModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    ComplaintStatusModel complaintStatusModel = response.body();

                    if (complaintStatusModel.getStatus().equals(Constant.TRUE)) {

                        if (complaintStatusModel.getData().size() > 0) {

                            for (int i = 0; i < complaintStatusModel.getData().size(); i++) {
                                if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_STATUS_DATA, DatabaseHelper.KEY_COMPLAINT_STATUS_ID, complaintStatusModel.getData().get(i).getValpos())) {
                                    databaseHelper.insertComplaintStatusData(complaintStatusModel.getData().get(i),false);
                                }
                            }
                            ComplaintStatusModel.Datum complaintListModel1 = new ComplaintStatusModel.Datum();
                            complaintListModel1.setValpos("0000");
                            complaintListModel1.setDomvalueL("All");
                            complaintListModel1.setSelected(true);
                            if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_STATUS_DATA, DatabaseHelper.KEY_COMPLAINT_STATUS_ID, "0000")) {
                                databaseHelper.insertComplaintStatusData(complaintListModel1,true);
                            }
                            getcomplaintList();
                        }
                    }else if (complaintStatusModel.getStatus().equals(Constant.FALSE)) {
                    Utility.ShowToast(getResources().getString(R.string.something_went_wrong),getActivity());
                    }else if (complaintStatusModel.getStatus().equals(Constant.FAILED)){
                        Utility.logout(getActivity());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ComplaintStatusModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });*/
    }

    private void getcomplaintList() {
        complaintArrayList = new ArrayList<>();



        complaintStatusArrayList = new ArrayList<>();

        Utility.showProgressDialogue(getActivity());
        Call<ComplaintListModel> call3 = apiInterface.getComplaintList(Utility.getSharedPreferences(getActivity(), Constant.accessToken));
        call3.enqueue(new Callback<ComplaintListModel>() {
            @Override
            public void onResponse(@NonNull Call<ComplaintListModel> call, @NonNull Response<ComplaintListModel> response) {
                Log.e("url===>", String.valueOf(call.request().url()));
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    ComplaintListModel complaintListModel = response.body();
                    if (complaintListModel.getStatus().equals(Constant.TRUE)) {
                        for(int i=0;i<complaintListModel.getData().size();i++){
                            if (!databaseHelper.isRecordExist(DatabaseHelper.TABLE_COMPLAINT_DATA, DatabaseHelper.KEY_COMPLAINT_NUMBER, complaintListModel.getData().get(i).getCmpno())) {

                                ComplaintListModel.Datum complaintModel = new ComplaintListModel.Datum();
                                complaintModel.setCmpno(complaintListModel.getData().get(i).getCmpno());
                                complaintModel.setCaddress(complaintListModel.getData().get(i).getCaddress());
                                complaintModel.setMblno(complaintListModel.getData().get(i).getMblno());
                                complaintModel.setCstname(complaintListModel.getData().get(i).getCstname());
                                complaintModel.setPernr(complaintListModel.getData().get(i).getPernr());
                                complaintModel.setEname(complaintListModel.getData().get(i).getEname());
                                complaintModel.setStatus(complaintListModel.getData().get(i).getStatus());
                                complaintModel.setMatnr(complaintListModel.getData().get(i).getMatnr());
                                complaintModel.setMaktx(complaintListModel.getData().get(i).getMaktx());
                                complaintModel.setVbeln(complaintListModel.getData().get(i).getVbeln());

                                complaintModel.setFkdat(complaintListModel.getData().get(i).getFkdat());
                                complaintModel.setFwrdTo(complaintListModel.getData().get(i).getFwrdTo());
                                complaintModel.setFdate(complaintListModel.getData().get(i).getFdate());
                                complaintModel.setAction(complaintListModel.getData().get(i).getAction());
                                complaintModel.setCmpPenRe(complaintListModel.getData().get(i).getCmpPenRe());
                                complaintModel.setLat(complaintListModel.getData().get(i).getLat());
                                complaintModel.setLng(complaintListModel.getData().get(i).getLng());
                                complaintModel.setCurrentStatus(complaintListModel.getData().get(i).getCurrentStatus());
                                complaintModel.setCurrentLng("");
                                complaintModel.setCurrentLng("");
                                complaintModel.setCustomerPay("");
                                complaintModel.setCompanyPay("");
                                complaintModel.setFocAmount("");
                                complaintModel.setReturnByCompany("");
                                complaintModel.setPayToFreelancer("");
                                complaintModel.setPumpSrNo("");
                                complaintModel.setMotorSrNo("");
                                complaintModel.setControllerSrNo("");
                                complaintModel.setCategory("");
                                complaintModel.setClosureReason("");
                                complaintModel.setDefectType("");
                                complaintModel.setRelatedTo("");
                                complaintModel.setRemark("");
                                complaintModel.setCurrentDate("");
                                complaintModel.setCurrentTime("");
                                complaintModel.setDistance("");
                                complaintModel.setDataSavedLocally(false);


                                    databaseHelper.insertComplaintDetailsData(complaintModel);
                            }
                        }
                        setStatusAdapter();
                        pullToRefresh.setRefreshing(false);
                    }else if (complaintListModel.getStatus().equals(Constant.FALSE)){
                        Utility.ShowToast(getResources().getString(R.string.something_went_wrong),getActivity());
                    }else if (complaintListModel.getStatus().equals(Constant.FAILED)){
                        Utility.logout(getActivity());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ComplaintListModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());

            }
        });
    }

    private void setStatusAdapter() {

      //  complaintStatusArrayList = databaseHelper.getAllComplaintStatusData();

        complaintStatusArrayList = new ArrayList<>();

        complaintStatusArrayList.add( new ComplaintStatusModel.Datum("0000",Constant.all,true));
        complaintStatusArrayList.add( new ComplaintStatusModel.Datum("0001",Constant.REPLY,false));
        complaintStatusArrayList.add( new ComplaintStatusModel.Datum("0002",Constant.PENDING_FOR_CLOSURE,false));
        complaintStatusArrayList.add( new ComplaintStatusModel.Datum("0003",Constant.PENDING_FOR_APPROVAL,false));
        complaintStatusArrayList.add( new ComplaintStatusModel.Datum("0004",Constant.AWAITING_FOR_APPROVAL,false));
        complaintStatusArrayList.add( new ComplaintStatusModel.Datum("0005",Constant.APROPVED_COMPLAINTS,false));
        complaintStatusArrayList.add( new ComplaintStatusModel.Datum("0006",Constant.CLOSURE_COMPLAINTS,false));

        Log.e("complaintStatus===>",complaintStatusArrayList.toString());
        complaintStatusAdapter = new ComplaintStatusAdapter(mContext, complaintStatusArrayList,selectedPosition);
        compStatusList.setHasFixedSize(true);
        complaintStatusAdapter.ItemClick(ComplaintListFragment.this);
        compStatusList.setAdapter(complaintStatusAdapter);
        setComplaintAdapter("");
    }
    private void setComplaintAdapter(String status) {
        complaintArrayList = databaseHelper.getAllComplaintDetailData(status,"false");
         Log.e("complaintArrayList", String.valueOf(complaintArrayList.toString()));
        if(complaintArrayList.size()>0) {
            noDataFound.setVisibility(View.GONE);
            compList.setVisibility(View.VISIBLE);
            complaintListAdapter = new ComplaintListAdapter(mContext, complaintArrayList, noDataFound);
            compList.setHasFixedSize(true);
            complaintListAdapter.ItemClick(ComplaintListFragment.this);
            compList.setAdapter(complaintListAdapter);
        }else {
            noDataFound.setVisibility(View.VISIBLE);
            compList.setVisibility(View.GONE);
        }
    }


    @Override
    public void SetOnItemClickListener(ComplaintStatusModel.Datum response, int position) {


        selectedPosition = position;
        ComplaintStatusModel.Datum complaintStatusModel = new ComplaintStatusModel.Datum();
        complaintStatusModel.setValpos(response.getValpos());
        complaintStatusModel.setDomvalueL(response.getDomvalueL());
        complaintStatusModel.setSelected(!response.isSelected());
        complaintStatusArrayList.set(position, complaintStatusModel);
        complaintStatusAdapter.notifyItemChanged(position);


        if (prevPosition != position) {
            if (prevPosition != -1) {
                ComplaintStatusModel.Datum complaintStatusModel1 = new ComplaintStatusModel.Datum();
                complaintStatusModel1.setValpos(complaintStatusArrayList.get(prevPosition).getValpos());
                complaintStatusModel1.setDomvalueL(complaintStatusArrayList.get(prevPosition).getDomvalueL());
                complaintStatusModel1.setSelected(false);

                complaintStatusArrayList.set(prevPosition, complaintStatusModel1);
                complaintStatusAdapter.notifyItemChanged(prevPosition);
            }

            prevPosition = position;


        }
        if(!response.getDomvalueL().equals(Constant.all)){
            setComplaintAdapter(response.getDomvalueL());
        }else{
            setComplaintAdapter("");
        }

    }

    @Override
    public void SetOnItemClickListener(ComplaintListModel.Datum response, int position) {
        if(Utility.isOnRoleApp()) {
            Intent intent = new Intent(mContext, ComplaintDetailsActivity.class);
            intent.putExtra(Constant.complaintData, response);
            startActivity(intent);
        }else {
            if (Utility.isFreelancerLogin(mContext)) {
                if (Utility.isTravelStart(getActivity())) {
                    Intent intent = new Intent(mContext, ComplaintDetailsOffRoleActivity.class);
                    intent.putExtra(Constant.complaintData, response);
                    startActivity(intent);
                } else {
                    ShowAlertResponse();
                }
            } else {
                Intent intent = new Intent(mContext, ComplaintDetailsActivity.class);
                intent.putExtra(Constant.complaintData, response);
                startActivity(intent);
            }
        }
    }

    private void ShowAlertResponse() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.send_successfully_layout,
                null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        TextView OK_txt = layout.findViewById(R.id.OK_txt);
        TextView title_txt = layout.findViewById(R.id.title_txt);
        ImageView user_img = layout.findViewById(R.id.user_img);

        user_img.setVisibility(View.GONE);
        title_txt.setText(getResources().getString(R.string.visitComplaint));

        OK_txt.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

    }


}