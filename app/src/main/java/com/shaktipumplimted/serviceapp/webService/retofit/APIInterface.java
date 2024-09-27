package com.shaktipumplimted.serviceapp.webService.retofit;

import com.shaktipumplimted.serviceapp.login.model.LoginRespModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.model.ComplaintDropdownModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.model.CompForwardListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model.PendingReasonModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.DistanceCalculateModel;
import com.shaktipumplimted.serviceapp.webService.api.APIS;

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

    @GET(APIS.COMPLAINT_FORWARD_PERSON_LIST_API)
    Call<CompForwardListModel> complaintForwardPersonList(@Query("token") String token, @Query("forward_to") String forward_to, @Query("cmpno") String cmpno);

    @GET(APIS.COMPLAINT_DROPDOWNS)
    Call<ComplaintDropdownModel> getComplaintDropdowns(@Query("token") String token);

  }



