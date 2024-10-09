package com.shaktipumplimted.serviceapp.main.bootomTabs.unsync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.model.CommonRespModel;
import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDetailsModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.DistanceCalculateModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.adapter.AttendanceAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.adapter.DsrAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.adapter.UnSyncCompListAdapter;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;
import com.shaktipumplimted.serviceapp.webService.uploadImages.UploadImageAPIS;
import com.shaktipumplimted.serviceapp.webService.uploadImages.interfaces.ActionListenerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsyncListFragment extends Fragment implements DsrAdapter.ItemClickListener, AttendanceAdapter.ItemClickListener, UnSyncCompListAdapter.ItemClickListener {

    RecyclerView dsrListView, attendanceListView, complaintCLoseListView;
    View view;
    DsrAdapter dsrAdapter;
    String solarInstdistance;
    AttendanceAdapter attendanceAdapter;
    UnSyncCompListAdapter unSyncCompListAdapter;
    DatabaseHelper databaseHelper;
    List<DsrDetailsModel> dsrDetailsModelList;
    List<MarkAttendanceModel> attendanceModelList;

    List<ComplaintListModel.Datum> complaintlist;
    APIInterface apiInterface, apiInterface1;

    UploadImageAPIS uploadImageAPIS;
    TextView dsrEntryTxt, attendanceTxt, closeComplaintTxt;

    List<ImageModel> imageArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_unsync_list, container, false);

        inIt(view);
        return view;
    }


    private void inIt(View view) {
        apiInterface = APIClient.getRetrofit(getActivity()).create(APIInterface.class);
        apiInterface1 = APIClient.getRetrofitDirection(getActivity()).create(APIInterface.class);
        databaseHelper = new DatabaseHelper(getActivity());
        uploadImageAPIS = new UploadImageAPIS(getActivity());
        dsrListView = view.findViewById(R.id.DsrListView);
        attendanceListView = view.findViewById(R.id.attendanceListView);
        complaintCLoseListView = view.findViewById(R.id.complaintCLoseList);
        dsrEntryTxt = view.findViewById(R.id.dsrEntryTxt);
        attendanceTxt = view.findViewById(R.id.attendanceTxt);
        closeComplaintTxt = view.findViewById(R.id.closeComplaintTxt);

        retrieveValue();
    }

    private void retrieveValue() {
        dsrDetailsModelList = new ArrayList<>();
        attendanceModelList = new ArrayList<>();
        complaintlist = new ArrayList<>();

        setDsrEntryData();
        setAttendanceData();
        setComplaintCloseData();

    }


    private void setDsrEntryData() {
        dsrDetailsModelList = new ArrayList<>();
        if (databaseHelper.isDataAvailable(databaseHelper.TABLE_DSR_RECORD)) {
            dsrDetailsModelList = databaseHelper.getAllDsrEntry(true);
            if (dsrDetailsModelList.size() > 0) {
                setDsrAdapter(dsrDetailsModelList);
                Log.e("dsrDetailsModelList===>", dsrDetailsModelList.toString());

                dsrListView.setVisibility(View.VISIBLE);
                attendanceTxt.setVisibility(View.VISIBLE);
            } else {
                dsrListView.setVisibility(View.GONE);
                dsrEntryTxt.setVisibility(View.GONE);
            }
        } else {
            dsrListView.setVisibility(View.GONE);
            dsrEntryTxt.setVisibility(View.GONE);
        }
    }

    private void setAttendanceData() {
        attendanceModelList=  new ArrayList<>();
        if (databaseHelper.isDataAvailable(databaseHelper.TABLE_MARK_ATTENDANCE_DATA)) {
            attendanceModelList = databaseHelper.getAllMarkAttendanceData(true, "", true);
            if (attendanceModelList.size() > 0) {
                setAttendanceAdapter(attendanceModelList);
                attendanceListView.setVisibility(View.VISIBLE);
                attendanceTxt.setVisibility(View.VISIBLE);
            } else {
                attendanceListView.setVisibility(View.GONE);
                attendanceTxt.setVisibility(View.GONE);
            }
        } else {
            attendanceListView.setVisibility(View.GONE);
            attendanceTxt.setVisibility(View.GONE);
        }
    }

    private void setComplaintCloseData() {
        complaintlist = new ArrayList<>();
        if (databaseHelper.isDataAvailable(databaseHelper.TABLE_COMPLAINT_DATA)) {
            complaintlist = databaseHelper.getAllComplaintDetailData("", "true");
            if (complaintlist.size() > 0) {
                setCompAdapter(complaintlist);
                Log.e("complaintlist===>", complaintlist.toString());

                complaintCLoseListView.setVisibility(View.VISIBLE);
                closeComplaintTxt.setVisibility(View.VISIBLE);
            } else {
                complaintCLoseListView.setVisibility(View.GONE);
                closeComplaintTxt.setVisibility(View.GONE);
            }
        } else {
            complaintCLoseListView.setVisibility(View.GONE);
            closeComplaintTxt.setVisibility(View.GONE);
        }
    }

    private void setDsrAdapter(List<DsrDetailsModel> dsrDetailsModelList) {
        dsrAdapter = new DsrAdapter(getActivity(), dsrDetailsModelList);
        dsrListView.setHasFixedSize(true);
        dsrListView.setAdapter(dsrAdapter);
        dsrAdapter.ItemClick(this);
    }

    private void setAttendanceAdapter(List<MarkAttendanceModel> attendanceModelList) {
        attendanceAdapter = new AttendanceAdapter(getActivity(), attendanceModelList);
        attendanceListView.setHasFixedSize(true);
        attendanceListView.setAdapter(attendanceAdapter);
        attendanceAdapter.ItemClick(this);
    }

    private void setCompAdapter(List<ComplaintListModel.Datum> complaintlist) {
        unSyncCompListAdapter = new UnSyncCompListAdapter(getActivity(), complaintlist);
        complaintCLoseListView.setHasFixedSize(true);
        complaintCLoseListView.setAdapter(unSyncCompListAdapter);
        unSyncCompListAdapter.ItemClick(this);
    }


    /*--------------------------------------------DSR item click------------------------------------------------------*/
    @Override
    public void SetOnItemClickListener(DsrDetailsModel dsrModel, int position) {
        if (Utility.isInternetOn(getActivity())) {
            syncDSRData(dsrModel);
        } else {
            Utility.ShowToast(getActivity().getResources().getString(R.string.checkInternetConnection), getActivity());
        }

    }

    /*--------------------------------------------Attendance item click------------------------------------------------------*/

    @Override
    public void SetOnItemClickListener(MarkAttendanceModel response, int position) {
        if (Utility.isInternetOn(getActivity())) {
            syncMarkAttendance(response);
        } else {
            Utility.ShowToast(getActivity().getResources().getString(R.string.checkInternetConnection), getActivity());
        }
    }

    /*--------------------------------------------Complaint item click------------------------------------------------------*/

    @Override
    public void SetOnItemClickListener(ComplaintListModel.Datum response, int position) {
        if (Utility.isInternetOn(getActivity())) {
            if (Utility.isFreelancerLogin(getActivity())) {
                if (response.getLat() != null && !response.getLat().isEmpty()) {
                    getCalculatedDistance(response.getLat(), response.getLng(),
                            response.getCurrentLat(), response.getCurrentLng(), response);
                }else{
                    saveFreelancerComplaint(response);
                }
            } else {
                closeComplaintAPI(response);
            }

        } else {
            Utility.ShowToast(getActivity().getResources().getString(R.string.checkInternetConnection), getActivity());
        }
    }

    /*--------------------------------------------Sync Dsr Entry------------------------------------------------------*/

    public void syncDSRData(DsrDetailsModel dsrModel) {
        try {


            Utility.showProgressDialogue(getActivity());
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("budat", dsrModel.getDate());
            jsonObject.put("help_name", dsrModel.getDsrActivity());
            jsonObject.put("dsr_agenda", dsrModel.getDsrPurpose());
            jsonObject.put("dsr_comment", dsrModel.getDsrOutcome());
            jsonObject.put("time", dsrModel.getTime());
            jsonObject.put("latitude", dsrModel.getLat());
            jsonObject.put("longitude", dsrModel.getLng());

            jsonArray.put(jsonObject);

            Call<CommonRespModel> call3 = apiInterface.saveDsrEntry(Utility.getSharedPreferences(getActivity(), Constant.accessToken), jsonArray.toString());
            call3.enqueue(new Callback<CommonRespModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonRespModel> call, @NonNull Response<CommonRespModel> response) {
                    Utility.hideProgressDialogue();
                    if (response.isSuccessful()) {
                        CommonRespModel commonRespModel = response.body();
                        if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                            Utility.ShowToast(commonRespModel.getMessage(), getActivity());
                            if (!Utility.getCurrentDate().equals(Utility.getFormattedDate("yyyyMMdd", "dd.MM.yyyy", dsrModel.getDate()))) {
                                databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_DSR_RECORD, DatabaseHelper.KEY_DSR_DATE, dsrModel.getDate());
                            } else {
                                DsrDetailsModel dsrDetailsModel = new DsrDetailsModel();
                                dsrDetailsModel.setDsrActivity(dsrModel.getDsrActivity());
                                dsrDetailsModel.setDsrOutcome(dsrModel.getDsrOutcome());
                                dsrDetailsModel.setDsrPurpose(dsrModel.getDsrPurpose());
                                dsrDetailsModel.setDate(dsrModel.getDate());
                                dsrDetailsModel.setTime(dsrModel.getTime());
                                dsrDetailsModel.setLat(dsrModel.getLat());
                                dsrDetailsModel.setLng(dsrModel.getLng());
                                dsrDetailsModel.setDataSavedLocally(false);
                                databaseHelper.updateDsrData(dsrDetailsModel);
                            }
                            setDsrEntryData();
                        } else if (commonRespModel.getStatus().equals(Constant.FALSE)) {
                            Utility.hideProgressDialogue();
                            Utility.ShowToast(getResources().getString(R.string.something_went_wrong), getActivity());
                        } else if (commonRespModel.getStatus().equals(Constant.FAILED)) {
                            Utility.logout(getActivity());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*--------------------------------------------Sync Mark Attendance------------------------------------------------------*/

    private void syncMarkAttendance(MarkAttendanceModel response) {
        Utility.showProgressDialogue(getActivity());
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", response.getAttendanceDate()));
            jsonObject.put("time", Utility.getFormattedTime("hh:mm a", "hhmmss", response.getAttendanceTime()));

            jsonObject.put("lat_long", response.getLatitude() + "," + response.getLongitude());

            if (!response.getLatitude().isEmpty()) {
                jsonObject.put("address", Utility.getAddressFromLatLng(getActivity(), response.getLatitude(), response.getLongitude()));
            }

            if (response.getAttendanceStatus().equals(Constant.attendanceIN)) {
                jsonObject.put("timestatus", "in");
            } else if (response.getAttendanceStatus().equals(Constant.attendanceOut)) {
                jsonObject.put("timestatus", "out");
            }
            jsonObject.put("image", Utility.getBase64FromPath(getActivity(), response.getAttendanceImg()));

            jsonArray.put(jsonObject);
          //  Log.e("jsonArray====>", jsonArray.toString());
            uploadImageAPIS.setActionListener(jsonArray, Constant.markAttendance, new ActionListenerCallback() {
                @Override
                public void onActionSuccess(String result) {
                    Utility.hideProgressDialogue();
                    CommonRespModel commonRespModel = new Gson().fromJson(result, CommonRespModel.class);
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {

                        if (!response.getAttendanceStatus().equals(Constant.attendanceOut)) {
                            databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_DSR_RECORD, DatabaseHelper.KEY_DSR_DATE, Utility.getFormattedDate("dd.MM.yyyy", "yyyyMMdd", Utility.getCurrentDate()));
                        }

                        if (databaseHelper.isRecordExist(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA, DatabaseHelper.KEY_ATTENDANCE_DATE, response.getAttendanceDate())) {
                            MarkAttendanceModel markAttendanceModel1 = new MarkAttendanceModel();
                            markAttendanceModel1.setAttendanceDate(response.getAttendanceDate());
                            markAttendanceModel1.setAttendanceTime(response.getAttendanceTime());
                            markAttendanceModel1.setAttendanceImg(response.getAttendanceImg());
                            markAttendanceModel1.setDataSavedLocally(false);
                            markAttendanceModel1.setAttendanceStatus(response.getAttendanceStatus());
                            databaseHelper.updateMarkAttendanceData(markAttendanceModel1);
                        }
                        setAttendanceData();
                        Utility.ShowToast(commonRespModel.getMessage(), getActivity());
                    } else if (commonRespModel.getStatus().equals(Constant.FALSE)) {
                        Utility.ShowToast(commonRespModel.getMessage(), getActivity());
                    } else if (commonRespModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getActivity());
                    }
                }

                @Override
                public void onActionFailure(String failureMessage) {
                    Utility.hideProgressDialogue();
                    try {
                        JSONObject jsonObject = new JSONObject(failureMessage);
                        if (jsonObject.getString("status").equals(Constant.FAILED)) {
                            Utility.logout(getActivity());
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*--------------------------------------------Sync Complaint Close Engineer------------------------------------------------------*/

    private void closeComplaintAPI(ComplaintListModel.Datum complaintModel) {
        Utility.showProgressDialogue(getActivity());
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("cmpno", complaintModel.getCmpno());
            jsonObject.put("category", complaintModel.getCategory());
            jsonObject.put("closer_reason", complaintModel.getClosureReason());
            jsonObject.put("defect", complaintModel.getDefectType());
            jsonObject.put("cmpln_related_to", complaintModel.getRelatedTo());
            jsonObject.put("customer", complaintModel.getCustomerPay());
            jsonObject.put("company", complaintModel.getCompanyPay());
            jsonObject.put("pay_freelancer", complaintModel.getPayToFreelancer());
            jsonObject.put("re_company", complaintModel.getReturnByCompany());
            jsonObject.put("focamt", complaintModel.getFocAmount());
            jsonObject.put("ZFEEDRMRK", complaintModel.getRemark());
            jsonObject.put("ZFEEDF", complaintModel.getRemark());
            jsonObject.put("cr_date", complaintModel.getCurrentDate());
            jsonObject.put("cr_time", complaintModel.getCurrentTime());
            jsonObject.put("latitude", complaintModel.getCurrentLat());
            jsonObject.put("longitude", complaintModel.getCurrentLng());
            jsonObject.put("p_serialno", complaintModel.getPumpSrNo());
            jsonObject.put("m_serialno", complaintModel.getMotorSrNo());
            jsonObject.put("c_serialno", complaintModel.getControllerSrNo());
            jsonArray.put(jsonObject);

            Log.e("jsonArray====>", jsonArray.toString());
            Call<CommonRespModel> call3 = apiInterface.complaintCloseEngineer(Utility.getSharedPreferences(getActivity(), Constant.accessToken), jsonArray.toString());
            call3.enqueue(new Callback<CommonRespModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonRespModel> call, @NonNull Response<CommonRespModel> response) {
                    Utility.hideProgressDialogue();
                    if (response.isSuccessful()) {
                        CommonRespModel commonRespModel = response.body();
                        if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                            databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_COMPLAINT_DATA, DatabaseHelper.KEY_COMPLAINT_NUMBER, complaintModel.getCmpno());
                            Utility.ShowToast(commonRespModel.getMessage(), getActivity());
                            setComplaintCloseData();
                        } else if (commonRespModel.getStatus().equals(Constant.FALSE)) {
                            Utility.hideProgressDialogue();
                            Utility.ShowToast(getResources().getString(R.string.something_went_wrong), getActivity());
                        } else if (commonRespModel.getStatus().equals(Constant.FAILED)) {
                            Utility.logout(getActivity());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*--------------------------------------------Sync Complaint Close Freelancer------------------------------------------------------*/

    private void getCalculatedDistance(String startLat, String startLong, String endLat, String endLong, ComplaintListModel.Datum complaintModel) {
        Utility.showProgressDialogue(getActivity());
        Call<DistanceCalculateModel> call3 = apiInterface1.getDistance(startLat + "," + startLong,
                endLat + "," + endLong, Constant.APIKEY);
        call3.enqueue(new Callback<DistanceCalculateModel>() {
            @Override
            public void onResponse(@NonNull Call<DistanceCalculateModel> call, @NonNull Response<DistanceCalculateModel> response) {
                Utility.hideProgressDialogue();
                if (response.isSuccessful()) {
                    DistanceCalculateModel distanceCalculateModel = response.body();

                    Log.e("distanceCalculate====>", String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText()));

                    solarInstdistance = String.valueOf(distanceCalculateModel.getRoutes().get(0).getLegs().get(0).getDistance().getText());

                    saveFreelancerComplaint(complaintModel);

                }

            }

            @Override
            public void onFailure(@NonNull Call<DistanceCalculateModel> call, @NonNull Throwable t) {
                call.cancel();
                Utility.hideProgressDialogue();
                Log.e("Error====>", t.getMessage().toString().trim());
            }
        });


    }


    private void saveFreelancerComplaint(ComplaintListModel.Datum complaintModel) {

        List<ImageModel> imageList = new ArrayList<>();

        Utility.showProgressDialogue(getActivity());
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cmpno", complaintModel.getCmpno());
            jsonObject.put("category", complaintModel.getCategory());
            jsonObject.put("closer_reason", complaintModel.getClosureReason());
            jsonObject.put("defect", complaintModel.getDefectType());
            jsonObject.put("cmpln_related_to", complaintModel.getRelatedTo());
            jsonObject.put("rm_code", Utility.getSharedPreferences(getActivity(),Constant.reportingPersonName));
            jsonObject.put("ZFEEDRMRK", complaintModel.getRemark());
            jsonObject.put("ZFEEDF", complaintModel.getRemark());
            jsonObject.put("cr_date", complaintModel.getCurrentDate());
            jsonObject.put("cr_time", complaintModel.getCurrentTime());
            jsonObject.put("latitude", complaintModel.getCurrentLat());
            jsonObject.put("longitude", complaintModel.getCurrentLng());
            jsonObject.put("distance", solarInstdistance);


            imageList = databaseHelper.getAllImages(DatabaseHelper.TABLE_COMPLAINT_IMAGE_DATA,complaintModel.getCmpno());
            for(int i=0; i<imageList.size() ; i++){
                if(imageList.get(i).getImagePath()!=null && !imageList.get(i).getImagePath().isEmpty()){
                    jsonObject.put("photo"+imageList.get(i).getPosition(), Utility.getBase64FromPath(getActivity(),imageList.get(i).getImagePath()));
                }
            }


            jsonArray.put(jsonObject);
           // Log.e("json===>",jsonArray.toString());
            uploadImageAPIS.setActionListener(jsonArray, Constant.OffrollClosure, new ActionListenerCallback() {
                @Override
                public void onActionSuccess(String result) {
                    Utility.hideProgressDialogue();

                    CommonRespModel commonRespModel = new Gson().fromJson(result, CommonRespModel.class);
                    if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                        databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_COMPLAINT_DATA, DatabaseHelper.KEY_COMPLAINT_NUMBER, complaintModel.getCmpno());
                        databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_COMPLAINT_IMAGE_DATA,DatabaseHelper.KEY_IMAGE_BILL_NO, complaintModel.getCmpno());
                        Utility.setSharedPreference(getActivity(), Constant.localConveyanceJourneyStart, "false");
                        Utility.ShowToast(commonRespModel.getMessage(), getActivity());
                        setComplaintCloseData();
                    } else if (commonRespModel.getStatus().equals(Constant.FALSE)) {
                        Utility.ShowToast(commonRespModel.getMessage(), getActivity());
                    } else if (commonRespModel.getStatus().equals(Constant.FAILED)) {
                        Utility.logout(getActivity());
                    }

                }

                @Override
                public void onActionFailure(String failureMessage) {
                    Utility.hideProgressDialogue();
                    try {
                        JSONObject jsonObject = new JSONObject(failureMessage);
                        if(jsonObject.getString("status").equals(Constant.FAILED)) {
                            Utility.logout(getActivity());
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}