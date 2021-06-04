package com.example.project1.Model;

public class CartModel {
    private String idcart,idproduct,iduser;
    private int cout;

    public CartModel() {
    }

    public String getIdcart() {
        return idcart;
    }

    public void setIdcart(String idcart) {
        this.idcart = idcart;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public CartModel(String idcart, String idproduct, String iduser, int cout) {
        this.idcart = idcart;
        this.idproduct = idproduct;
        this.iduser = iduser;
        this.cout = cout;
    }
}
