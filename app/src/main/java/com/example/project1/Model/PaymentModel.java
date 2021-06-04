package com.example.project1.Model;

public class PaymentModel {
    private String idpay,iduser,idorder;
    private double price;

    public PaymentModel() {
    }

    public PaymentModel(String idpay, String iduser, String idorder, double price) {
        this.idpay = idpay;
        this.iduser = iduser;
        this.idorder = idorder;
        this.price = price;
    }

    public String getIdpay() {
        return idpay;
    }

    public void setIdpay(String idpay) {
        this.idpay = idpay;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIdorder() {
        return idorder;
    }

    public void setIdorder(String idorder) {
        this.idorder = idorder;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
