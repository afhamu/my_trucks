package com.gigaweb.mytrucks.models;

public class Truck {

    public String truckNumber;
    public String make;
    public String driver;
    public String position;
    public String driverPhone;
    public String totalExpenses;
    public String totalIncome;
    public String city;
    public String location;

    public Truck(String truckNumber, String make, String driver, String position, String driverPhone, String totalExpenses, String totalIncome, String city) {
        this.truckNumber = truckNumber;
        this.make = make;
        this.driver = driver;
        this.position = position;
        this.driverPhone = driverPhone;
        this.totalExpenses = totalExpenses;
        this.totalIncome = totalIncome;
        this.city = city;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public String getMake() {
        return make;
    }

    public String getDriver() {
        return driver;
    }

    public String getPosition() {
        return position;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public String getTotalExpenses() {
        return totalExpenses;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public String getCity() {
        return city;
    }

    public String getLocation() {
        return location;
    }
}
