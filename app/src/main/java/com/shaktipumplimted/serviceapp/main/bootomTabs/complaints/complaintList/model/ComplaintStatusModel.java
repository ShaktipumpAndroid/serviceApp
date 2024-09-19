package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model;

public class ComplaintStatusModel {
    String status,id;
    boolean isSelected;

    public ComplaintStatusModel( String id,String status,boolean isSelected) {
        this.id = id;
        this.status = status;
        this.isSelected = isSelected;
    }

    public ComplaintStatusModel() {

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
