package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintListModel implements Serializable {

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

    public ComplaintListModel withStatus(String status) {
        this.status = status;
        return this;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public ComplaintListModel withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    public static class Datum implements Serializable{

        @SerializedName("caddress")
        @Expose
        private String caddress;
        @SerializedName("cmpno")
        @Expose
        private String cmpno;
        @SerializedName("mblno")
        @Expose
        private String mblno;
        @SerializedName("mblno1")
        @Expose
        private String mblno1;
        @SerializedName("cstname")
        @Expose
        private String cstname;
        @SerializedName("kunnr")
        @Expose
        private String kunnr;
        @SerializedName("name1")
        @Expose
        private String name1;
        @SerializedName("pernr")
        @Expose
        private String pernr;
        @SerializedName("ename")
        @Expose
        private String ename;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("matnr")
        @Expose
        private String matnr;
        @SerializedName("maktx")
        @Expose
        private String maktx;
        @SerializedName("vbeln")
        @Expose
        private String vbeln;
        @SerializedName("fkdat")
        @Expose
        private String fkdat;
        @SerializedName("fwrd_to")
        @Expose
        private String fwrdTo;
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
        @SerializedName("cmp_pen_re")
        @Expose
        private String cmpPenRe;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("current_status")
        @Expose
        private String currentStatus;

        public String getCaddress() {
            return caddress;
        }

        public void setCaddress(String caddress) {
            this.caddress = caddress;
        }

        public Datum withCaddress(String caddress) {
            this.caddress = caddress;
            return this;
        }

        public String getCmpno() {
            return cmpno;
        }

        public void setCmpno(String cmpno) {
            this.cmpno = cmpno;
        }

        public Datum withCmpno(String cmpno) {
            this.cmpno = cmpno;
            return this;
        }

        public String getMblno() {
            return mblno;
        }

        public void setMblno(String mblno) {
            this.mblno = mblno;
        }

        public Datum withMblno(String mblno) {
            this.mblno = mblno;
            return this;
        }

        public String getMblno1() {
            return mblno1;
        }

        public void setMblno1(String mblno1) {
            this.mblno1 = mblno1;
        }

        public Datum withMblno1(String mblno1) {
            this.mblno1 = mblno1;
            return this;
        }

        public String getCstname() {
            return cstname;
        }

        public void setCstname(String cstname) {
            this.cstname = cstname;
        }

        public Datum withCstname(String cstname) {
            this.cstname = cstname;
            return this;
        }

        public String getKunnr() {
            return kunnr;
        }

        public void setKunnr(String kunnr) {
            this.kunnr = kunnr;
        }

        public Datum withKunnr(String kunnr) {
            this.kunnr = kunnr;
            return this;
        }

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public Datum withName1(String name1) {
            this.name1 = name1;
            return this;
        }

        public String getPernr() {
            return pernr;
        }

        public void setPernr(String pernr) {
            this.pernr = pernr;
        }

        public Datum withPernr(String pernr) {
            this.pernr = pernr;
            return this;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public Datum withEname(String ename) {
            this.ename = ename;
            return this;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Datum withStatus(String status) {
            this.status = status;
            return this;
        }

        public String getMatnr() {
            return matnr;
        }

        public void setMatnr(String matnr) {
            this.matnr = matnr;
        }

        public Datum withMatnr(String matnr) {
            this.matnr = matnr;
            return this;
        }

        public String getMaktx() {
            return maktx;
        }

        public void setMaktx(String maktx) {
            this.maktx = maktx;
        }

        public Datum withMaktx(String maktx) {
            this.maktx = maktx;
            return this;
        }

        public String getVbeln() {
            return vbeln;
        }

        public void setVbeln(String vbeln) {
            this.vbeln = vbeln;
        }

        public Datum withVbeln(String vbeln) {
            this.vbeln = vbeln;
            return this;
        }

        public String getFkdat() {
            return fkdat;
        }

        public void setFkdat(String fkdat) {
            this.fkdat = fkdat;
        }

        public Datum withFkdat(String fkdat) {
            this.fkdat = fkdat;
            return this;
        }

        public String getFwrdTo() {
            return fwrdTo;
        }

        public void setFwrdTo(String fwrdTo) {
            this.fwrdTo = fwrdTo;
        }

        public Datum withFwrdTo(String fwrdTo) {
            this.fwrdTo = fwrdTo;
            return this;
        }

        public String getFdate() {
            return fdate;
        }

        public void setFdate(String fdate) {
            this.fdate = fdate;
        }

        public Datum withFdate(String fdate) {
            this.fdate = fdate;
            return this;
        }

        public String getAedtm() {
            return aedtm;
        }

        public void setAedtm(String aedtm) {
            this.aedtm = aedtm;
        }

        public Datum withAedtm(String aedtm) {
            this.aedtm = aedtm;
            return this;
        }

        public String getChtm() {
            return chtm;
        }

        public void setChtm(String chtm) {
            this.chtm = chtm;
        }

        public Datum withChtm(String chtm) {
            this.chtm = chtm;
            return this;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Datum withAction(String action) {
            this.action = action;
            return this;
        }

        public String getCounter() {
            return counter;
        }

        public void setCounter(String counter) {
            this.counter = counter;
        }

        public Datum withCounter(String counter) {
            this.counter = counter;
            return this;
        }

        public String getCmpPenRe() {
            return cmpPenRe;
        }

        public void setCmpPenRe(String cmpPenRe) {
            this.cmpPenRe = cmpPenRe;
        }

        public Datum withCmpPenRe(String cmpPenRe) {
            this.cmpPenRe = cmpPenRe;
            return this;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public Datum withLat(String lat) {
            this.lat = lat;
            return this;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public Datum withLng(String lng) {
            this.lng = lng;
            return this;
        }

        public String getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(String currentStatus) {
            this.currentStatus = currentStatus;
        }

        public Datum withCurrentStatus(String currentStatus) {
            this.currentStatus = currentStatus;
            return this;
        }

        @Override
        public String toString() {
            return "Datum{" +
                    "caddress='" + caddress + '\'' +
                    ", cmpno='" + cmpno + '\'' +
                    ", mblno='" + mblno + '\'' +
                    ", mblno1='" + mblno1 + '\'' +
                    ", cstname='" + cstname + '\'' +
                    ", kunnr='" + kunnr + '\'' +
                    ", name1='" + name1 + '\'' +
                    ", pernr='" + pernr + '\'' +
                    ", ename='" + ename + '\'' +
                    ", status='" + status + '\'' +
                    ", matnr='" + matnr + '\'' +
                    ", maktx='" + maktx + '\'' +
                    ", vbeln='" + vbeln + '\'' +
                    ", fkdat='" + fkdat + '\'' +
                    ", fwrdTo='" + fwrdTo + '\'' +
                    ", fdate='" + fdate + '\'' +
                    ", aedtm='" + aedtm + '\'' +
                    ", chtm='" + chtm + '\'' +
                    ", action='" + action + '\'' +
                    ", counter='" + counter + '\'' +
                    ", cmpPenRe='" + cmpPenRe + '\'' +
                    ", lat='" + lat + '\'' +
                    ", lng='" + lng + '\'' +
                    ", currentStatus='" + currentStatus + '\'' +
                    '}';
        }
    }
}