package com.shaktipumplimted.serviceapp.webService.retofit;

import com.shaktipumplimted.serviceapp.Utils.common.model.CommonRespModel;
import com.shaktipumplimted.serviceapp.login.model.LoginRespModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.model.ComplaintDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.model.CompForwardListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model.PendingReasonListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model.PendingReasonModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.model.PhotoListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDetailsModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.DistanceCalculateModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.AttendanceDataModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.checkOut.model.CheckOutDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports.model.GatePassReportModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.model.LocalConveyanceReportModel;
import com.shaktipumplimted.serviceapp.webService.api.APIS;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET(APIS.LOGIN)
    Call<LoginRespModel> login(@Query("pernr") String userId, @Query("objs") String objs, @Query("login") String loginType,
                               @Query("pass") String password, @Query("fcmtoken") String fcmtoken, @Query("app_version") String app_version,
                               @Query("api") String api, @Query("apiversion") String apiversion, @Query("deviceid") String deviceid);




    @GET(APIS.DIRECTIONAPI)
    Call<DistanceCalculateModel> getDistance(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);

    @GET(APIS.STATUSLIST)
    Call<ComplaintStatusModel> getStatusList(@Query("token") String token);



    @GET(APIS.CUSTOMER_COMPLAINT)
    Call<ComplaintListModel> getComplaintList(@Query("token") String token);


    @GET(APIS.PENDING_REASONS_API)
    Call<PendingReasonModel> getPendingReasonList(@Query("token") String token);

    @GET(APIS.PENDING_REASON_HISTORY)
    Call<PendingReasonListModel> getPendingReasonHistory(@Query("token") String token, @Query("cmpno") String cmpno);

    @GET(APIS.COMPLAINT_FORWARD_PERSON_LIST_API)
    Call<CompForwardListModel> complaintForwardPersonList(@Query("token") String token, @Query("forward_to") String forward_to, @Query("cmpno") String cmpno);

    @GET(APIS.COMPLAINT_DROPDOWNS)
    Call<ComplaintDropdownModel> getComplaintDropdowns(@Query("token") String token);

    @GET(APIS.COMPLAINT_FORWARD_APPROVAL)
    Call<CommonRespModel> complaintForwardApproval(@Query("token") String token, @Query("forward_approval_complaint") String forward_approval_complaint);

    @GET(APIS.COMPLAINT_PHOTO_LIST)
    Call<PhotoListModel> getComplaintPhotoList(@Query("token") String token, @Query("cmpno") String cmpno, @Query("page") String page);


    @GET(APIS.GET_ATTENDANCE_DATA)
    Call<AttendanceDataModel> getAttendanceData(@Query("token") String token);



    @GET(APIS.DSR_DROPDOWN)
    Call<DsrDropdownModel> getDsrDropdown(@Query("token") String token);

    @GET(APIS.DSR_SAVE)
    Call<CommonRespModel> saveDsrEntry(@Query("token") String token ,@Query("data") String data);

    @GET(APIS.COMPLAINT_FORWARD)
    Call<CommonRespModel> forwardComplaint(@Query("token") String token ,@Query("complaint_forward") String data);

    @GET(APIS.CHECK_OUT_DROPDOWN)
    Call<CheckOutDropdownModel> getCheckOutDropdown(@Query("token") String token);

    @GET(APIS.sendVerificationOtpAPI)
    Call<ResponseBody> sendOTP(@Query("mobiles") String mobileno, @Query("message") String message, @Query("sender") String sender,
                               @Query("unicode") String unicode, @Query("route") String route, @Query("country") String country,
                               @Query("DLT_TE_ID") String dlt_te_id);


    @GET(APIS.TRAVEL_REPORT)
    Call<LocalConveyanceReportModel> getTravelReport(@Query("token") String token , @Query("start_date") String start_date, @Query("end_date") String end_date);

    @GET(APIS.GATEPASS_REPORT)
    Call<GatePassReportModel> getGatePassReport(@Query("token") String token);

}




