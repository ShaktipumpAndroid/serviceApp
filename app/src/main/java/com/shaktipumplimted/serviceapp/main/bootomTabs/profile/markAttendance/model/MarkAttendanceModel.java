package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model;

public class MarkAttendanceModel {
    String uniqId,attendanceDate,attendanceTime,attendanceStatus,attendanceImg,latitude,longitude;

    boolean isDataSavedLocally;
    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getAttendanceImg() {
        return attendanceImg;
    }

    public void setAttendanceImg(String attendanceImg) {
        this.attendanceImg = attendanceImg;
    }

    public boolean isDataSavedLocally() {
        return isDataSavedLocally;
    }

    public void setDataSavedLocally(boolean dataSavedLocally) {
        isDataSavedLocally = dataSavedLocally;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MarkAttendanceModel{" +
                "uniqId='" + uniqId + '\'' +
                ", attendanceDate='" + attendanceDate + '\'' +
                ", attendanceTime='" + attendanceTime + '\'' +
                ", attendanceStatus='" + attendanceStatus + '\'' +
                ", attendanceImg='" + attendanceImg + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", isDataSavedLocally=" + isDataSavedLocally +
                '}';
    }
}
