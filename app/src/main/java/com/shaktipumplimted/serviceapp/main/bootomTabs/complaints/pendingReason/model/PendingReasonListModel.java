package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.pendingReason.model;

public class PendingReasonListModel {

    String date,followupDate,employee,remark;

    public PendingReasonListModel(String date, String followupDate, String employee, String remark) {
        this.date = date;
        this.followupDate = followupDate;
        this.employee = employee;
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(String followupDate) {
        this.followupDate = followupDate;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
