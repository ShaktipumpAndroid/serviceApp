package com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model;

public class LocalConveyanceModel {
    String uniqId,startLatitude, startLongitude,endLatitude,endLongitude,startAddress,endAddress,startDate,startTime,endDate,endTime,startImgPath,endImgPath;

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartImgPath() {
        return startImgPath;
    }

    public void setStartImgPath(String startImgPath) {
        this.startImgPath = startImgPath;
    }

    public String getEndImgPath() {
        return endImgPath;
    }

    public void setEndImgPath(String endImgPath) {
        this.endImgPath = endImgPath;
    }

    @Override
    public String toString() {
        return "LocalConveyanceModel{" +
                "uniqId='" + uniqId + '\'' +
                ", startLatitude='" + startLatitude + '\'' +
                ", startLongitude='" + startLongitude + '\'' +
                ", endLatitude='" + endLatitude + '\'' +
                ", endLongitude='" + endLongitude + '\'' +
                ", startAddress='" + startAddress + '\'' +
                ", endAddress='" + endAddress + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startImgPath='" + startImgPath + '\'' +
                ", endImgPath='" + endImgPath + '\'' +
                '}';
    }
}
