package com.example.alirzaycefaydal.ecommerceserviceside.Models;

import java.util.List;

public class Request {
    private String name;
    private String adress;
    private String phone;
    private String total;
    private List<Order> foods;
    private String status;

    public Request(){}

    public Request(String name, String adress, String phone, String total, List<Order> foods) {
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.total = total;
        this.foods = foods;
        this.status="0";  //default is 0. 0:Placed- 1:Shipping- 2:shipped
    }


    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
