package com.scan.me;

/**
 * Created by mido on 09/04/18.
 */

public class Room {
    public static final String HALL = "hall";
    public static final String LAB = "lab";
    private String id, number, type;
    private double latitude, longitude;

    public Room(String number, String type, double latitude, double longitude) {
        this.number = number;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
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
