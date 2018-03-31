package com.alaryani.aamrny.object;


public class User {
    private String idUser;
    private String fullName;
    private String linkImage;
    private String email;
    private String gender;
    private String address;
    private String phone;
    private String dob;
    private Double balance;
    private String isOnline;
    private String token;
    private String description;
    private CarObj carObj;
    private DriverObj driverObj;
    private String account;
    private String passengerRate;
    private String cityId;
    private String cityName;
    private String stateId;
    private String stateName;
    private String passengerRateCount;
    private String typeAccount;
    private String isActive;
    private String payout;

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        this.typeAccount = typeAccount;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPassengerRateCount() {
        return passengerRateCount;
    }

    public void setPassengerRateCount(String passengerRateCount) {
        this.passengerRateCount = passengerRateCount;
    }

    public String getPassengerRate() {
        return passengerRate;
    }

    public void setPassengerRate(String passengerRate) {
        this.passengerRate = passengerRate;
    }

    public User() {
        // TODO Auto-generated constructor stub
        carObj = new CarObj();
        driverObj = new DriverObj();
    }

    public DriverObj getDriverObj() {
        return driverObj;
    }


    public void setDriverObj(DriverObj driverObj) {
        this.driverObj = driverObj;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public CarObj getCarObj() {
        return carObj;
    }

    public void setCarObj(CarObj carObj) {
        this.carObj = carObj;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
