package com.gigaweb.mytrucks.models;

import java.util.Date;

public class Trip {
    private int id;
    private String date;
    private String tripNumber;
    private String truckNumber;
    private String containerNumber;
    private String goods;
    private String origin;
    private String destination;
    private String eta;
    private String distance;
    private int fuelConsumption;
    private int issues;
    private String status;


    public Trip(int id, String date, String tripNumber, String truckNumber, String containerNumber, String goods,
                String origin, String destination, String eta, String distance, int fuelConsumption, int issues, String status) {
        this.id = id;
        this.date = date;
        this.tripNumber = tripNumber;
        this.truckNumber = truckNumber;
        this.containerNumber = containerNumber;
        this.goods = goods;
        this.origin = origin;
        this.destination = destination;
        this.eta = eta;
        this.distance = distance;
        this.fuelConsumption = fuelConsumption;
        this.issues = issues;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTripNumber() {
        return tripNumber;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public String getGoods() {
        return goods;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getEta() {
        return eta;
    }

    public String getDistance() {
        return distance;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public int getIssues() {
        return issues;
    }

    public String getStatus() {
        return status;
    }
}
