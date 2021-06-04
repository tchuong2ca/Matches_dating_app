package com.example.matches.Model;
public class Users {
    private String userId;
    private String name;
    private String imgUrl;
private String age;
private String address;
private String hobbies;
private String contact;
    private String description;

    public Users(String userId, String name, String imgUrl, String age, String address, String hobbies, String contact, String description) {
        this.userId = userId;
        this.name = name;
        this.imgUrl = imgUrl;
        this.age = age;
        this.address = address;
        this.hobbies = hobbies;
        this.contact = contact;
        this.description = description;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String phone) {
        this.contact = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
