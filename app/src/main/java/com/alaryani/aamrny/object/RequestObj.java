package com.alaryani.aamrny.object;

public class RequestObj {
	private String id;
	private String passengerId;
	private String passengerImage;
	private String passengerName;
	private String passengerphone;
	private String driverId;
	private String requestTime;
	private String link;
	private String startTime;
	private String startLat;
	private String startLong;
	private String startLocation;
	private String endLat;
	private String endLong;
	private String endLocation;
	private String passengerRate;
	private String estimate_fare;
	private String identity;

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getEstimate_fare() {
		return estimate_fare;
	}

	public void setEstimate_fare(String estimate_fare) {
		this.estimate_fare = estimate_fare;
	}

	public String getPassengerImage() {
		return passengerImage;
	}

	public void setPassengerImage(String passengerImage) {
		this.passengerImage = passengerImage;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getPassengerphone() {
		if (!passengerphone.isEmpty() && passengerphone.length() > 6)
			return passengerphone.substring(0, passengerphone.length() - 6) + "xxx xxx";
		else
			return passengerphone;
	}

	public void setPassengerphone(String passengerphone) {
		this.passengerphone = passengerphone;
	}

	public String getPassengerRate() {
		return passengerRate;
	}

	public void setPassengerRate(String passenger) {
		this.passengerRate = passenger;
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

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
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

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
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

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}
}
