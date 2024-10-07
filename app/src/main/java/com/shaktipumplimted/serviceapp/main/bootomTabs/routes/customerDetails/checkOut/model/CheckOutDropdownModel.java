package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.checkOut.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckOutDropdownModel {

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

    public CheckOutDropdownModel withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CheckOutDropdownModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public CheckOutDropdownModel withResponse(List<Response> response) {
        this.response = response;
        return this;
    }

    public class Response {

        @SerializedName("help_code")
        @Expose
        private String helpCode;
        @SerializedName("help_name")
        @Expose
        private String helpName;

        public String getHelpCode() {
            return helpCode;
        }

        public void setHelpCode(String helpCode) {
            this.helpCode = helpCode;
        }

        public Response withHelpCode(String helpCode) {
            this.helpCode = helpCode;
            return this;
        }

        public String getHelpName() {
            return helpName;
        }

        public void setHelpName(String helpName) {
            this.helpName = helpName;
        }

        public Response withHelpName(String helpName) {
            this.helpName = helpName;
            return this;
        }
    }
}