package com.scan.me;

/**
 * Created by mido on 11/04/18.
 */

public class UserAttend {
    String id, uid, name, image;
    boolean attend;

    public UserAttend() {
    }

    public UserAttend(String id, String uid, String name, String image, boolean attend) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.attend = attend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAttend() {
        return attend;
    }

    public void setAttend(boolean attend) {
        this.attend = attend;
    }
}
