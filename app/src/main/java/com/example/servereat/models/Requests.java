package com.example.servereat.models;

import java.util.List;

public class Requests {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String date;
    private String time;
    private String comment;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    private List<Order> foods;

    Requests() {


    }

    public Requests(String phone, String name, String address, String total,String date,String time, List<Order> foods,String comment) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.date = date;
        this.time = time;
        this.status = "0";
        this.comment = comment;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTotal() {
        return total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
