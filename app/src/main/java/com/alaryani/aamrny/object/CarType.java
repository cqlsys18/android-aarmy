package com.alaryani.aamrny.object;

/**
 * Created by Administrator on 4/8/2017.
 */

public class CarType {
    private String id;
    private String imageType;
    private String imageMarker;
    private String name;
    private String link;
    private String start_fare;
    private String fee_per_minute;
    private String fee_per_kilometer;
    private String gotoA;
    private String workAtA;
    private String workAtB;
    private String taskDefaultTime;
    private String taskRate;

    public String getTaskDefaultTime() {
        return taskDefaultTime;
    }

    public void setTaskDefaultTime(String taskDefaultTime) {
        this.taskDefaultTime = taskDefaultTime;
    }

    public String getTaskRate() {
        return taskRate;
    }

    public void setTaskRate(String taskRate) {
        this.taskRate = taskRate;
    }

    public String getGotoA() {
        return gotoA;
    }

    public void setGotoA(String gotoA) {
        this.gotoA = gotoA;
    }

    public String getWorkAtA() {
        return workAtA;
    }

    public void setWorkAtA(String workAtA) {
        this.workAtA = workAtA;
    }

    public String getWorkAtB() {
        return workAtB;
    }

    public void setWorkAtB(String workAtB) {
        this.workAtB = workAtB;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageMarker() {
        return imageMarker;
    }

    public void setImageMarker(String imageMarker) {
        this.imageMarker = imageMarker;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStart_fare() {
        return start_fare;
    }

    public void setStart_fare(String start_fare) {
        this.start_fare = start_fare;
    }

    public String getFee_per_minute() {
        return fee_per_minute;
    }

    public void setFee_per_minute(String fee_per_minute) {
        this.fee_per_minute = fee_per_minute;
    }

    public String getFee_per_kilometer() {
        return fee_per_kilometer;
    }

    public void setFee_per_kilometer(String fee_per_kilometer) {
        this.fee_per_kilometer = fee_per_kilometer;
    }

    public CarType() {
    }

    public CarType(String image, String name) {
        this.imageType = image;
        this.name = name;
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

}
