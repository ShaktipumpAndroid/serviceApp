package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingReasonListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("complain_action")
        @Expose
        private List<ComplainAction> complainAction;

        public List<ComplainAction> getComplainAction() {
            return complainAction;
        }

        public void setComplainAction(List<ComplainAction> complainAction) {
            this.complainAction = complainAction;
        }
        public static class ComplainAction {

            @SerializedName("pernr")
            @Expose
            private String pernr;
            @SerializedName("ename")
            @Expose
            private String ename;
            @SerializedName("cmpno")
            @Expose
            private String cmpno;
            @SerializedName("fdate")
            @Expose
            private String fdate;
            @SerializedName("aedtm")
            @Expose
            private String aedtm;
            @SerializedName("chtm")
            @Expose
            private String chtm;
            @SerializedName("action")
            @Expose
            private String action;
            @SerializedName("counter")
            @Expose
            private String counter;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("cmp_pen_re")
            @Expose
            private String cmpPenRe;

            public String getPernr() {
                return pernr;
            }

            public void setPernr(String pernr) {
                this.pernr = pernr;
            }

            public String getEname() {
                return ename;
            }

            public void setEname(String ename) {
                this.ename = ename;
            }

            public String getCmpno() {
                return cmpno;
            }

            public void setCmpno(String cmpno) {
                this.cmpno = cmpno;
            }

            public String getFdate() {
                return fdate;
            }

            public void setFdate(String fdate) {
                this.fdate = fdate;
            }

            public String getAedtm() {
                return aedtm;
            }

            public void setAedtm(String aedtm) {
                this.aedtm = aedtm;
            }

            public String getChtm() {
                return chtm;
            }

            public void setChtm(String chtm) {
                this.chtm = chtm;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public String getCounter() {
                return counter;
            }

            public void setCounter(String counter) {
                this.counter = counter;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCmpPenRe() {
                return cmpPenRe;
            }

            public void setCmpPenRe(String cmpPenRe) {
                this.cmpPenRe = cmpPenRe;
            }

        }
    }
}
