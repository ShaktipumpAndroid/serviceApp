package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model;

public class AllAttendanceRecordModel {
    private String uniqId,attendanceDate, attendanceInTime,attendanceOutTime;

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

    public String getAttendanceInTime() {
        return attendanceInTime;
    }

    public void setAttendanceInTime(String attendanceInTime) {
        this.attendanceInTime = attendanceInTime;
    }

    public String getAttendanceOutTime() {
        return attendanceOutTime;
    }

    public void setAttendanceOutTime(String attendanceOutTime) {
        this.attendanceOutTime = attendanceOutTime;
    }

    @Override
    public String toString() {
        return "AllAttendanceRecordModel{" +
                "attendanceDate='" + attendanceDate + '\'' +
                ", attendanceInTime='" + attendanceInTime + '\'' +
                ", attendanceOutTime='" + attendanceOutTime + '\'' +
                '}';
    }
}
