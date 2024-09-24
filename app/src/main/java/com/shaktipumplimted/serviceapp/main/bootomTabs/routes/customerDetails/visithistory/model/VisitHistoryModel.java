package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.visithistory.model;

public class VisitHistoryModel {
    private  String name, visitDate,description;

    public VisitHistoryModel(String name, String visitDate, String description) {
        this.name = name;
        this.visitDate = visitDate;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
