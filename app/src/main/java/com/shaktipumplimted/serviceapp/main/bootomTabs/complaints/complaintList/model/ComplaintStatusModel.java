package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplaintStatusModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public static class Datum {

        @SerializedName("valpos")
        @Expose
        private String valpos;
        @SerializedName("domvalue_l")
        @Expose
        private String domvalueL;

        boolean isSelected;

        public Datum(String valpos, String domvalueL, boolean isSelected) {
            this.valpos = valpos;
            this.domvalueL = domvalueL;
            this.isSelected = isSelected;
        }

        public Datum() {

        }

        public String getValpos() {
            return valpos;
        }

        public void setValpos(String valpos) {
            this.valpos = valpos;
        }

        public String getDomvalueL() {
            return domvalueL;
        }

        public void setDomvalueL(String domvalueL) {
            this.domvalueL = domvalueL;
        }
        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        @Override
        public String toString() {
            return "Datum{" +
                    "valpos='" + valpos + '\'' +
                    ", domvalueL='" + domvalueL + '\'' +
                    ", isSelected=" + isSelected +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ComplaintStatusModel{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
