package com.shaktipumplimted.serviceapp.Utils.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonRespModel {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("response")
    @Expose
    private List<String> response;

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

    public void setResponse(List<String> response) {
        this.response = response;
    }

    public CommonRespModel withResponse(List<String> response) {
        this.response = response;
        return this;
    }

}

