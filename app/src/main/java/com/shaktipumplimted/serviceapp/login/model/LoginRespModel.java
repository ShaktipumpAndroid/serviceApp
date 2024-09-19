package com.shaktipumplimted.serviceapp.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRespModel {
    @SerializedName("LOGIN")
    @Expose
    private String login;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Userid")
    @Expose
    private String userid;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("reportingPersonSapID")
    @Expose
    private String reportingPersonSapID;
    @SerializedName("reportingPersonName")
    @Expose
    private String reportingPersonName;
    @SerializedName("AccessToken")
    @Expose
    private String accessToken;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReportingPersonSapID() {
        return reportingPersonSapID;
    }

    public void setReportingPersonSapID(String reportingPersonSapID) {
        this.reportingPersonSapID = reportingPersonSapID;
    }

    public String getReportingPersonName() {
        return reportingPersonName;
    }

    public void setReportingPersonName(String reportingPersonName) {
        this.reportingPersonName = reportingPersonName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
