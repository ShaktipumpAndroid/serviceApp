package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ComplaintDropdownModel {

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

    public ComplaintDropdownModel withStatus(String status) {
        this.status = status;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ComplaintDropdownModel withData(Data data) {
        this.data = data;
        return this;
    }

    public class Data {

        @SerializedName("complain_category")
        @Expose
        private List<ComplainCategory> complainCategory;
        @SerializedName("complain_defect")
        @Expose
        private List<ComplainDefect> complainDefect;
        @SerializedName("complain_related_to")
        @Expose
        private List<ComplainRelatedTo> complainRelatedTo;
        @SerializedName("complain_closer")
        @Expose
        private List<ComplainCloser> complainCloser;

        public List<ComplainCategory> getComplainCategory() {
            return complainCategory;
        }

        public void setComplainCategory(List<ComplainCategory> complainCategory) {
            this.complainCategory = complainCategory;
        }

        public Data withComplainCategory(List<ComplainCategory> complainCategory) {
            this.complainCategory = complainCategory;
            return this;
        }

        public List<ComplainDefect> getComplainDefect() {
            return complainDefect;
        }

        public void setComplainDefect(List<ComplainDefect> complainDefect) {
            this.complainDefect = complainDefect;
        }

        public Data withComplainDefect(List<ComplainDefect> complainDefect) {
            this.complainDefect = complainDefect;
            return this;
        }

        public List<ComplainRelatedTo> getComplainRelatedTo() {
            return complainRelatedTo;
        }

        public void setComplainRelatedTo(List<ComplainRelatedTo> complainRelatedTo) {
            this.complainRelatedTo = complainRelatedTo;
        }

        public Data withComplainRelatedTo(List<ComplainRelatedTo> complainRelatedTo) {
            this.complainRelatedTo = complainRelatedTo;
            return this;
        }

        public List<ComplainCloser> getComplainCloser() {
            return complainCloser;
        }

        public void setComplainCloser(List<ComplainCloser> complainCloser) {
            this.complainCloser = complainCloser;
        }

        public Data withComplainCloser(List<ComplainCloser> complainCloser) {
            this.complainCloser = complainCloser;
            return this;
        }

    }
    public class ComplainRelatedTo {

        @SerializedName("cmpln_related_to")
        @Expose
        private String cmplnRelatedTo;
        @SerializedName("cat_id")
        @Expose
        private String catId;

        public String getCmplnRelatedTo() {
            return cmplnRelatedTo;
        }

        public void setCmplnRelatedTo(String cmplnRelatedTo) {
            this.cmplnRelatedTo = cmplnRelatedTo;
        }

        public ComplainRelatedTo withCmplnRelatedTo(String cmplnRelatedTo) {
            this.cmplnRelatedTo = cmplnRelatedTo;
            return this;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public ComplainRelatedTo withCatId(String catId) {
            this.catId = catId;
            return this;
        }

    }

    public class ComplainDefect {

        @SerializedName("defect")
        @Expose
        private String defect;
        @SerializedName("cat_id")
        @Expose
        private String catId;

        public String getDefect() {
            return defect;
        }

        public void setDefect(String defect) {
            this.defect = defect;
        }

        public ComplainDefect withDefect(String defect) {
            this.defect = defect;
            return this;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public ComplainDefect withCatId(String catId) {
            this.catId = catId;
            return this;
        }

    }

    public class ComplainCloser {

        @SerializedName("mandt")
        @Expose
        private String mandt;
        @SerializedName("extwg")
        @Expose
        private String extwg;
        @SerializedName("reason")
        @Expose
        private String reason;

        public String getMandt() {
            return mandt;
        }

        public void setMandt(String mandt) {
            this.mandt = mandt;
        }

        public ComplainCloser withMandt(String mandt) {
            this.mandt = mandt;
            return this;
        }

        public String getExtwg() {
            return extwg;
        }

        public void setExtwg(String extwg) {
            this.extwg = extwg;
        }

        public ComplainCloser withExtwg(String extwg) {
            this.extwg = extwg;
            return this;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public ComplainCloser withReason(String reason) {
            this.reason = reason;
            return this;
        }

    }

    public class ComplainCategory {

        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("cat_id")
        @Expose
        private String catId;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public ComplainCategory withCategory(String category) {
            this.category = category;
            return this;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public ComplainCategory withCatId(String catId) {
            this.catId = catId;
            return this;
        }

    }
}



