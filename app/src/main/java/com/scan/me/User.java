package com.scan.me;

/**
 * Created by mido on 09/04/18.
 */

public class User {
    public static final String STUDENT = "Student";
    public static final String ADMIN = "Admin";
    public static final String TUTOR = "Tutor";
    private String email, name, uid, year, image, type;

    public User(String email, String name, String year, String image, String type) {
        this.email = email;
        this.name = name;
        this.year = year;
        this.image = image;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
