package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.reports.gatePassReports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GatePassReportModel {
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
    public class Response {

        @SerializedName("chln_inv")
        @Expose
        private String chlnInv;
        @SerializedName("matnr")
        @Expose
        private String matnr;
        @SerializedName("maktx")
        @Expose
        private String maktx;
        @SerializedName("ch_qty")
        @Expose
        private String chQty;

        public String getChlnInv() {
            return chlnInv;
        }

        public void setChlnInv(String chlnInv) {
            this.chlnInv = chlnInv;
        }

        public String getMatnr() {
            return matnr;
        }

        public void setMatnr(String matnr) {
            this.matnr = matnr;
        }

        public String getMaktx() {
            return maktx;
        }

        public void setMaktx(String maktx) {
            this.maktx = maktx;
        }

        public String getChQty() {
            return chQty;
        }

        public void setChQty(String chQty) {
            this.chQty = chQty;
        }

    }
}
