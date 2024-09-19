package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.model;

public class CompForwardListModel {
    String code,name,isSelectedId;

    public CompForwardListModel(String code, String name,String isSelectedId) {
        this.code = code;
        this.name = name;
        this.isSelectedId = isSelectedId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsSelectedId() {
        return isSelectedId;
    }

    public void setIsSelectedId(String isSelectedId) {
        this.isSelectedId = isSelectedId;
    }
}
