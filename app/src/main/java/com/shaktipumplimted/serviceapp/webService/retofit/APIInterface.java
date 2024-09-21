package com.shaktipumplimted.serviceapp.webService.retofit;

import com.shaktipumplimted.serviceapp.login.model.LoginRespModel;
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
}



