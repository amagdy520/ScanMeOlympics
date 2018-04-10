package com.scan.me;

/**
 * Created by mido on 09/04/18.
 */

public class User {
    public static final String STUDENT = "Student";
    public static final String ADMIN = "Admin";
    public static final String TUTOR = "Tutor";
    private String email, name, uid,mac ,year, department,section,image, type,hash;

    public User(String email, String name, String uid, String mac, String year, String department, String section, String type, String hash) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.mac = mac;
        this.year = year;
        this.department = department;
        this.section = section;
        this.type = type;
        this.hash = hash;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
