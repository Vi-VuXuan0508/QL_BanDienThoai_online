package com.example.project1.Model;

public class OrderDetail {
    private String idorderdetail,idorder,idproduct;
    private int count;
    public OrderDetail() {
    }

    public String getIdorderdetail() {
        return idorderdetail;
    }

    public void setIdorderdetail(String idorderdetail) {
        this.idorderdetail = idorderdetail;
    }

    public String getIdorder() {
        return idorder;
    }

    public void setIdorder(String idorder) {
        this.idorder = idorder;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OrderDetail(String idorderdetail, String idorder, String idproduct, int count) {
        this.idorderdetail = idorderdetail;
        this.idorder = idorder;
        this.idproduct = idproduct;
        this.count = count;
    }
}
