package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ComplaintListModel  implements Serializable {
    String compNo,customerName,customerMobile,customerAddress,comDate,complaintStatus;

    public ComplaintListModel(String compNo, String customerName, String customerMobile, String customerAddress, String comDate, String complaintStatus) {
        this.compNo = compNo;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerAddress = customerAddress;
        this.comDate = comDate;
        this.complaintStatus = complaintStatus;
    }

    public String getCompNo() {
        return compNo;
    }

    public void setCompNo(String compNo) {
        this.compNo = compNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getComDate() {
        return comDate;
    }

    public void setComDate(String comDate) {
        this.comDate = comDate;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }
}
