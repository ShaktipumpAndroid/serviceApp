package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DsrDropdownModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DsrDropdownModel withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DsrDropdownModel withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public DsrDropdownModel withData(List<Datum> data) {
        this.data = data;
        return this;
    }
    public class Datum {

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

        public Datum withHelpCode(String helpCode) {
            this.helpCode = helpCode;
            return this;
        }

        public String getHelpName() {
            return helpName;
        }

        public void setHelpName(String helpName) {
            this.helpName = helpName;
        }

        public Datum withHelpName(String helpName) {
            this.helpName = helpName;
            return this;
        }

    }

}