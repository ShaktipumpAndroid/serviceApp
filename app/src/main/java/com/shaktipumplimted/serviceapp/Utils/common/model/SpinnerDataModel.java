package com.shaktipumplimted.serviceapp.Utils.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpinnerDataModel {

 private String id , name;

    public SpinnerDataModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpinnerDataModel(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
