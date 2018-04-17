package com.scan.me;

public class Room
{
    public static final String STAGE = "Stage";
    public static final String HALL = "Hall";
    public static final String LAB = "Lab";
    private String id, number, type,floor;
    private double latitude, longitude;

    public Room()
    {

    }

    public Room(String number, String type, String floor, double latitude, double longitude) {
        this.number = number;
        this.type = type;
        this.floor = floor;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
