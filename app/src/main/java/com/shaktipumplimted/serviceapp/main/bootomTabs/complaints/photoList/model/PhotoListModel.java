package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.photoList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoListModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Count")
    @Expose
    private String count;
    @SerializedName("response")
    @Expose
    private List<Response> response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public class Response {

        @SerializedName("image_1")
        @Expose
        private String image1;
        @SerializedName("lines")
        @Expose
        private String lines;

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getLines() {
            return lines;
        }

        public void setLines(String lines) {
            this.lines = lines;
        }

    }
}
