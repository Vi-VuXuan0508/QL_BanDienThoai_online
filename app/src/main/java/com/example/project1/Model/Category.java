package com.example.project1.Model;

public class Category {
    private String idcategory;
    private String name;
    private String image;

    public String getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(String idcategory) {
        this.idcategory = idcategory;
    }

    public Category(String idcategory, String name, String image) {
        this.idcategory = idcategory;
        this.name = name;
        this.image = image;
    }

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Category() {
    }

    @Override
    public String toString() {
        return name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
