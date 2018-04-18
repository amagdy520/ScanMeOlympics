package com.scan.me.User;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mido on 09/04/18.
 */

public class User  implements Parcelable {
    public static final String STUDENT = "Student";
    public static final String ADMIN = "Admin";
    public static final String TUTOR = "Tutor";
    private String email,password, name, uid, mac, year, department, section, image, type, hash, code, number,id;
    long codeTime;

    public User() {

    }
    public User(String email,String password, String name, String number, String uid, String mac, String year, String department, String section, String type, String hash) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.mac = mac;
        this.year = year;
        this.department = department;
        this.section = section;
        this.type = type;
        this.hash = hash;
        this.number = number;
        this.password=password;
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        uid = in.readString();
        mac = in.readString();
        year = in.readString();
        department = in.readString();
        section = in.readString();
        image = in.readString();
        type = in.readString();
        hash = in.readString();
        code = in.readString();
        number = in.readString();
        id = in.readString();
        codeTime = in.readLong();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getCodeTime() {
        return codeTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCodeTime(long codeTime) {
        this.codeTime = codeTime;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(uid);
        dest.writeString(mac);
        dest.writeString(year);
        dest.writeString(department);
        dest.writeString(section);
        dest.writeString(image);
        dest.writeString(type);
        dest.writeString(hash);
        dest.writeString(code);
        dest.writeString(number);
        dest.writeString(id);
        dest.writeLong(codeTime);
    }
}
