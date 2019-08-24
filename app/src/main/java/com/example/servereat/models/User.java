package com.example.servereat.models;

public class User {
    private String name;
    private String phone;
    private String password;
    private String IsStaff;

    public User(String name, String phone, String password, String isStaff) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        IsStaff = isStaff;
    }

    User(){


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
