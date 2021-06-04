package com.example.matches.Model;

public class Match_Object {

    private String partnerId;
            private String name ,des, imgUrl;

    public Match_Object(String partnerId, String name, String des, String imgUrl) {
        this.partnerId = partnerId;
        this.name = name;
        this.des = des;
        this.imgUrl = imgUrl;
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
}
