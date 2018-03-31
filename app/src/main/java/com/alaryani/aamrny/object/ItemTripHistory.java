package com.alaryani.aamrny.object;

public class ItemTripHistory {
    private int driverId;
    private int passengerId;
    private String startTime;
    private String endTime;
    private int tripId;
    private Double distance;
    private String startLocaton;
    private String endLocation;
    private Double totalPoint;
    private String status;
    private String token;
    private String totalTime;
    private String actualFare;
    private String link;
    private String actualReceive;
    private String startTimeWorking;
    private String endTimeWorking;
    private String paymentMethod;


    public String getStartTimeWorking() {
        return startTimeWorking;
    }

    public void setStartTimeWorking(String startTimeWorking) {
        this.startTimeWorking = startTimeWorking;
    }

    public String getEndTimeWorking() {
        return endTimeWorking;
    }

    public void setEndTimeWorking(String endTimeWorking) {
        this.endTimeWorking = endTimeWorking;
    }

    public String getActualReceive() {
        return actualReceive;
    }

    public void setActualReceive(String actualReceive) {
        this.actualReceive = actualReceive;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getActualFare() {
        return actualFare;
    }

    public void setActualFare(String actualFare) {
        this.actualFare = actualFare;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getStartLocaton() {
        return startLocaton;
    }

    public void setStartLocaton(String startLocaton) {
        this.startLocaton = startLocaton;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public Double getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Double totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
