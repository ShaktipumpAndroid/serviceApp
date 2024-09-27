package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingReasonModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pending_reason")
    @Expose
    private List<PendingReason> pendingReason;

    public List<PendingReason> getPendingReason() {
        return pendingReason;
    }

    public void setPendingReason(List<PendingReason> pendingReason) {
        this.pendingReason = pendingReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class PendingReason {

        @SerializedName("cmp_pen_re")
        @Expose
        private String cmpPenRe;
        @SerializedName("name")
        @Expose
        private String name;

        public String getCmpPenRe() {
            return cmpPenRe;
        }

        public void setCmpPenRe(String cmpPenRe) {
            this.cmpPenRe = cmpPenRe;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
