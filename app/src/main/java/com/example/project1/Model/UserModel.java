package com.example.project1.Model;

import java.io.Serializable;

public class UserModel {
    private String iduser;
    private String username;
    private String password;
    private String status;
    private String fullname;
    private String email;
    private String address;
    private String image;
    private String date;
    private int phone;

    public UserModel(String iduser, String username, String password, String status, String fullname, String email, String address, String image, String date, int phone) {
        this.iduser = iduser;
        this.username = username;
        this.password = password;
        this.status = status;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.image = image;
        this.date = date;
        this.phone = phone;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public UserModel(String iduser) {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserModel() {
    }
}
