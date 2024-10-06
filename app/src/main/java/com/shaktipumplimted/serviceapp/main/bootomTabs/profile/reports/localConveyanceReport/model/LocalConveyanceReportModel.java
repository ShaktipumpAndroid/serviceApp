package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.localConveyanceReport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocalConveyanceReportModel {
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

        @SerializedName("begda")
        @Expose
        private String begda;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("endda")
        @Expose
        private String endda;
        @SerializedName("end_time")
        @Expose
        private String endTime;
        @SerializedName("travel_mode")
        @Expose
        private String travelMode;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("start_location")
        @Expose
        private String startLocation;
        @SerializedName("end_location")
        @Expose
        private String endLocation;

        public String getBegda() {
            return begda;
        }

        public void setBegda(String begda) {
            this.begda = begda;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndda() {
            return endda;
        }

        public void setEndda(String endda) {
            this.endda = endda;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getTravelMode() {
            return travelMode;
        }

        public void setTravelMode(String travelMode) {
            this.travelMode = travelMode;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(String startLocation) {
            this.startLocation = startLocation;
        }

        public String getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(String endLocation) {
            this.endLocation = endLocation;
        }
    }
    }
