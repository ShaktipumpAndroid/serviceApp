package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model;

public class DsrDetailsModel {
    String uniqId,dsrActivity,dsrPurpose,dsrOutcome,date,time,lat,lng;

    boolean isDataSavedLocally;

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getDsrActivity() {
        return dsrActivity;
    }

    public void setDsrActivity(String dsrActivity) {
        this.dsrActivity = dsrActivity;
    }

    public String getDsrPurpose() {
        return dsrPurpose;
    }

    public void setDsrPurpose(String dsrPurpose) {
        this.dsrPurpose = dsrPurpose;
    }

    public String getDsrOutcome() {
        return dsrOutcome;
    }

    public void setDsrOutcome(String dsrOutcome) {
        this.dsrOutcome = dsrOutcome;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public boolean isDataSavedLocally() {
        return isDataSavedLocally;
    }

    public void setDataSavedLocally(boolean dataSavedLocally) {
        isDataSavedLocally = dataSavedLocally;
    }

    @Override
    public String toString() {
        return "DsrDetailsModel{" +
                "uniqId='" + uniqId + '\'' +
                ", dsrActivity='" + dsrActivity + '\'' +
                ", dsrPurpose='" + dsrPurpose + '\'' +
                ", dsrOutcome='" + dsrOutcome + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", isDataSavedLocally=" + isDataSavedLocally +
                '}';
    }
}
