package com.scan.me;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mido on 11/04/18.
 */

public class Reservation implements Parcelable {

    String name, tutorName, tutorId, id, from, to, hash, code, date,roomNumber,floor;
    boolean attend;

    Reservation() {
    }

    public Reservation(String name, String tutorName, String tutorId, String from, String to, String hash, String date, boolean attend) {
        this.name = name;
        this.tutorName = tutorName;
        this.tutorId = tutorId;
        this.from = from;
        this.to = to;
        this.hash = hash;
        this.attend = attend;
        this.date = date;
    }


    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected Reservation(Parcel in) {
        name = in.readString();
        tutorName = in.readString();
        tutorId = in.readString();
        id = in.readString();
        from = in.readString();
        to = in.readString();
        hash = in.readString();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isAttend() {
        return attend;
    }

    public void setAttend(boolean attend) {
        this.attend = attend;
    }


    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tutorName);
        dest.writeString(tutorId);
        dest.writeString(id);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(hash);
        dest.writeString(code);
        dest.writeString(date);
        dest.writeString(roomNumber);
        dest.writeString(floor);
        dest.writeByte((byte) (attend ? 1 : 0));
    }
}
