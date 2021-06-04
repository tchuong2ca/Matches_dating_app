package com.example.matches.Model;

public class Chat_Object {
    private String message;
    private Boolean currentuser;
    private String partnerAvt;

    public Chat_Object(String message, Boolean currentuser, String partnerAvt) {
        this.message = message;
        this.currentuser = currentuser;
        this.partnerAvt = partnerAvt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentuser() {
        return currentuser;
    }

    public void setCurrentuser(Boolean currentuser) {
        this.currentuser = currentuser;
    }

    public String getPartnerAvt() {
        return partnerAvt;
    }

    public void setPartnerAvt(String partnerAvt) {
        this.partnerAvt = partnerAvt;
    }
}
