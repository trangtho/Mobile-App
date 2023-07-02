package com.example.mobileapp.model;

public class Product {
    String Image;
    String ProductName;
    String Price;


    public Product(){

    };

    public Product(String image, String productName, String price) {
        Image = image;
        ProductName = productName;
        Price = price;
    }


    public String getImage() {
        return Image;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getPrice() {
        return Price;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setPrice(String price) {
        Price = price;
    }

}
