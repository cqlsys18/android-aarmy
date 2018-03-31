package com.alaryani.aamrny.object;

public class CurrentOrder {
    private String id;
    private String passengerId;
    private String link;
    private String startTime;
    private String startLat;
    private String startLong;
    private String endLat;
    private String endLong;
    private String dateCreated;
    private String driverId;
    private String startLocation;
    private String endLocation;
    private String status;
    private String endTime;
    private String distance;
    private String estimateFare;
    private String actualFare;
    private String driverRate;
    private String passengerRate;
    private String driverName;
    private String driver_rate;
    private String imageDriver;
    private String carPlate;
    private String carImage;
    private String driver_phone;
    private String passengerName;
    private String passenger_rate;
    private String imagePassenger;
    private String passenger_phone;
    private String estimate_fare;
    private String pickUpAtA;
    private String workAtB;
    private String startTimeWorking;
    private String endTimeWorking;
    private String driverRateTrip;
    private String passengerRateTrip;
    private String identity;
    private String totalTime;

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getDriverRateTrip() {
        return driverRateTrip;
    }

    public void setDriverRateTrip(String driverRateTrip) {
        this.driverRateTrip = driverRateTrip;
    }

    public String getPassengerRateTrip() {
        return passengerRateTrip;
    }

    public void setPassengerRateTrip(String passengerRateTrip) {
        this.passengerRateTrip = passengerRateTrip;
    }

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

    public String getWorkAtB() {
        return workAtB;
    }

    public void setWorkAtB(String workAtB) {
        this.workAtB = workAtB;
    }

    public String getPickUpAtA() {
        return pickUpAtA;
    }

    public void setPickUpAtA(String pickUpAtA) {
        this.pickUpAtA = pickUpAtA;
    }

    public String getEstimate_fare() {
        return estimate_fare;
    }

    public void setEstimate_fare(String estimate_fare) {
        this.estimate_fare = estimate_fare;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLong() {
        return endLong;
    }

    public void setEndLong(String endLong) {
        this.endLong = endLong;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEstimateFare() {
        return estimateFare;
    }

    public void setEstimateFare(String estimateFare) {
        this.estimateFare = estimateFare;
    }

    public String getActualFare() {
        return actualFare;
    }

    public void setActualFare(String actualFare) {
        this.actualFare = actualFare;
    }

    public String getDriverRate() {
        return driverRate;
    }

    public void setDriverRate(String driverRate) {
        this.driverRate = driverRate;
    }

    public String getPassengerRate() {
        return passengerRate;
    }

    public void setPassengerRate(String passengerRate) {
        this.passengerRate = passengerRate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriver_rate() {
        return driver_rate;
    }

    public void setDriver_rate(String driver_rate) {
        this.driver_rate = driver_rate;
    }

    public String getImageDriver() {
        return imageDriver;
    }

    public void setImageDriver(String imageDriver) {
        this.imageDriver = imageDriver;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getDriver_phone(boolean hidePhone) {
        if (!hidePhone) {
            return driver_phone;
        }
        if (!driver_phone.isEmpty() && driver_phone.length() > 6)
            return driver_phone.substring(0, driver_phone.length() - 6) + "xxx xxx";
        else
            return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassenger_rate() {
        return passenger_rate;
    }

    public void setPassenger_rate(String passenger_rate) {
        this.passenger_rate = passenger_rate;
    }

    public String getImagePassenger() {
        return imagePassenger;
    }

    public void setImagePassenger(String imagePassenger) {
        this.imagePassenger = imagePassenger;
    }

    public String getPassenger_phone(boolean hidePhone) {
        if (!hidePhone) {
            return passenger_phone;
        }
        if (passenger_phone != null && passenger_phone.length() > 6)
            return passenger_phone.substring(0, passenger_phone.length() - 6) + "xxx xxx";
        else
            return passenger_phone;
    }

    public void setPassenger_phone(String passenger_phone) {
        this.passenger_phone = passenger_phone;
    }
}
