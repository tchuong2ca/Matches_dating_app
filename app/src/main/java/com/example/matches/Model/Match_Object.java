package com.example.matches.Model;

public class Match_Object {

    private String partnerId;
            private String name ,des, imgUrl;
    private String notificationKey, lastmessage, sentby;

    public Match_Object(String partnerId, String name, String des, String imgUrl, String notificationKey, String lastmessage, String sentby) {
        this.partnerId = partnerId;
        this.name = name;
        this.des = des;
        this.imgUrl = imgUrl;
        this.notificationKey = notificationKey;
        this.lastmessage = lastmessage;
        this.sentby = sentby;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }
}
