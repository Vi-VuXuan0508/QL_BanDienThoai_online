package com.example.project1.Model;


public class Coupon{
   private String idcoupon,iduser,image,status,title,time;
   private int percent;
   private String seri;

   public String getIdcoupon() {
      return idcoupon;
   }

   public void setIdcoupon(String idcoupon) {
      this.idcoupon = idcoupon;
   }

   public String getIduser() {
      return iduser;
   }

   public void setIduser(String iduser) {
      this.iduser = iduser;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public int getPercent() {
      return percent;
   }

   public void setPercent(int percent) {
      this.percent = percent;
   }

   public String getSeri() {
      return seri;
   }

   public void setSeri(String seri) {
      this.seri = seri;
   }

   public Coupon() {
   }

   public Coupon(String idcoupon, String iduser, String image, String status, String title, String time, int percent, String seri) {
      this.idcoupon = idcoupon;
      this.iduser = iduser;
      this.image = image;
      this.status = status;
      this.title = title;
      this.time = time;
      this.percent = percent;
      this.seri = seri;
   }
}
