package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceDataModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private List<Response> response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
    public static class Response {

        @SerializedName("mandt")
        @Expose
        private String mandt;
        @SerializedName("pernr")
        @Expose
        private String pernr;
        @SerializedName("begda")
        @Expose
        private String begda;
        @SerializedName("server_date_in")
        @Expose
        private String serverDateIn;
        @SerializedName("server_time_in")
        @Expose
        private String serverTimeIn;
        @SerializedName("server_date_out")
        @Expose
        private String serverDateOut;
        @SerializedName("server_time_out")
        @Expose
        private String serverTimeOut;
        @SerializedName("in_address")
        @Expose
        private String inAddress;
        @SerializedName("in_time")
        @Expose
        private String inTime;
        @SerializedName("out_address")
        @Expose
        private String outAddress;
        @SerializedName("out_time")
        @Expose
        private String outTime;
        @SerializedName("working_hours")
        @Expose
        private String workingHours;
        @SerializedName("in_lat_long")
        @Expose
        private String inLatLong;
        @SerializedName("in_url")
        @Expose
        private String inUrl;
        @SerializedName("out_lat_long")
        @Expose
        private String outLatLong;
        @SerializedName("out_url")
        @Expose
        private String outUrl;
        @SerializedName("device_name")
        @Expose
        private String deviceName;
        @SerializedName("model_no")
        @Expose
        private String modelNo;
        @SerializedName("imei")
        @Expose
        private String imei;
        @SerializedName("version")
        @Expose
        private String version;
        @SerializedName("api")
        @Expose
        private String api;
        @SerializedName("api_version")
        @Expose
        private String apiVersion;
        @SerializedName("attendance_from")
        @Expose
        private String attendanceFrom;
        @SerializedName("hod_appr")
        @Expose
        private String hodAppr;
        @SerializedName("hod_rej")
        @Expose
        private String hodRej;
        @SerializedName("hod_erdat")
        @Expose
        private String hodErdat;
        @SerializedName("hod_time")
        @Expose
        private String hodTime;

        public String getMandt() {
            return mandt;
        }

        public void setMandt(String mandt) {
            this.mandt = mandt;
        }

        public String getPernr() {
            return pernr;
        }

        public void setPernr(String pernr) {
            this.pernr = pernr;
        }

        public String getBegda() {
            return begda;
        }

        public void setBegda(String begda) {
            this.begda = begda;
        }

        public String getServerDateIn() {
            return serverDateIn;
        }

        public void setServerDateIn(String serverDateIn) {
            this.serverDateIn = serverDateIn;
        }

        public String getServerTimeIn() {
            return serverTimeIn;
        }

        public void setServerTimeIn(String serverTimeIn) {
            this.serverTimeIn = serverTimeIn;
        }

        public String getServerDateOut() {
            return serverDateOut;
        }

        public void setServerDateOut(String serverDateOut) {
            this.serverDateOut = serverDateOut;
        }

        public String getServerTimeOut() {
            return serverTimeOut;
        }

        public void setServerTimeOut(String serverTimeOut) {
            this.serverTimeOut = serverTimeOut;
        }

        public String getInAddress() {
            return inAddress;
        }

        public void setInAddress(String inAddress) {
            this.inAddress = inAddress;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public String getOutAddress() {
            return outAddress;
        }

        public void setOutAddress(String outAddress) {
            this.outAddress = outAddress;
        }

        public String getOutTime() {
            return outTime;
        }

        public void setOutTime(String outTime) {
            this.outTime = outTime;
        }

        public String getWorkingHours() {
            return workingHours;
        }

        public void setWorkingHours(String workingHours) {
            this.workingHours = workingHours;
        }

        public String getInLatLong() {
            return inLatLong;
        }

        public void setInLatLong(String inLatLong) {
            this.inLatLong = inLatLong;
        }

        public String getInUrl() {
            return inUrl;
        }

        public void setInUrl(String inUrl) {
            this.inUrl = inUrl;
        }

        public String getOutLatLong() {
            return outLatLong;
        }

        public void setOutLatLong(String outLatLong) {
            this.outLatLong = outLatLong;
        }

        public String getOutUrl() {
            return outUrl;
        }

        public void setOutUrl(String outUrl) {
            this.outUrl = outUrl;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getModelNo() {
            return modelNo;
        }

        public void setModelNo(String modelNo) {
            this.modelNo = modelNo;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public void setApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
        }

        public String getAttendanceFrom() {
            return attendanceFrom;
        }

        public void setAttendanceFrom(String attendanceFrom) {
            this.attendanceFrom = attendanceFrom;
        }

        public String getHodAppr() {
            return hodAppr;
        }

        public void setHodAppr(String hodAppr) {
            this.hodAppr = hodAppr;
        }

        public String getHodRej() {
            return hodRej;
        }

        public void setHodRej(String hodRej) {
            this.hodRej = hodRej;
        }

        public String getHodErdat() {
            return hodErdat;
        }

        public void setHodErdat(String hodErdat) {
            this.hodErdat = hodErdat;
        }

        public String getHodTime() {
            return hodTime;
        }

        public void setHodTime(String hodTime) {
            this.hodTime = hodTime;
        }
}
}
