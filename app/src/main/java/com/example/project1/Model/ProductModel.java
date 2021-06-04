package com.example.project1.Model;

public class ProductModel {
    private String idproduct,image,title,idcategory
           ;
    private int count;
    private double price;
    private double pricecoupon;

    public ProductModel() {
    }

    public double getPricecoupon() {
        return pricecoupon;
    }

    public void setPricecoupon(double pricecoupon) {
        this.pricecoupon = pricecoupon;
    }



    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(String idcategory) {
        this.idcategory = idcategory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductModel(String image, String title, String idcategory, int count, double price, double pricecoupon) {
        this.image = image;
        this.title = title;
        this.idcategory = idcategory;
        this.count = count;
        this.price = price;
        this.pricecoupon = pricecoupon;
    }

    public ProductModel(String idproduct, String image,
                        String title, String idcategory,int count,double pricecoupon, double price) {
        this.idproduct = idproduct;
        this.image = image;
        this.title = title;
        this.idcategory = idcategory;
        this.pricecoupon = pricecoupon;
        this.count = count;
        this.price = price;
    }
}
