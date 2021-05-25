package com.example.matches;
public class Cards {
    private String userId;
    private String name;
    private String imgUrl;

    public Cards(String userId, String name, String imgUrl) {
        this.userId = userId;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
