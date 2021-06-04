package com.example.project1.Model;

public class OrderModel {
    private String idorder,time,status;
    private double pricetotal,pricecoupon,priceship,pricefinal;

    public OrderModel(String idorder, String time, String status,
                      double pricetotal, double pricecoupon,
                      double priceship, double pricefinal) {
        this.idorder = idorder;
        this.time = time;
        this.status = status;
        this.pricetotal = pricetotal;
        this.pricecoupon = pricecoupon;
        this.priceship = priceship;
        this.pricefinal = pricefinal;
    }

    public OrderModel() {
    }

    public String getIdorder() {
        return idorder;
    }

    public void setIdorder(String idorder) {
        this.idorder = idorder;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public double getPricetotal() {
        return pricetotal;
    }

    public void setPricetotal(double pricetotal) {
        this.pricetotal = pricetotal;
    }

    public double getPricecoupon() {
        return pricecoupon;
    }

    public void setPricecoupon(double pricecoupon) {
        this.pricecoupon = pricecoupon;
    }

    public double getPriceship() {
        return priceship;
    }

    public void setPriceship(double priceship) {
        this.priceship = priceship;
    }

    public double getPricefinal() {
        return pricefinal;
    }

    public void setPricefinal(double pricefinal) {
        this.pricefinal = pricefinal;
    }


}
