package com.example.alirzaycefaydal.ecommerceserviceside.Models;

public class User {

    private String name, user_id, isStuff,image,token_id,phone;

    public User(){
    }

    public User(String name, String user_id, String isStuff, String image, String token_id, String phone) {
        this.name = name;
        this.user_id = user_id;
        this.isStuff = isStuff;
        this.image = image;
        this.token_id = token_id;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIsStuff() {
        return isStuff;
    }

    public void setIsStuff(String isStuff) {
        this.isStuff = isStuff;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
