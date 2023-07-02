package com.example.mobileapp.model;

public class Favorite {
    private String ProductPicture;
    private String ProductName;
    private String Price;


    public Favorite(){}

    public Favorite(String productPicture, String productName, String price) {
        ProductPicture = productPicture;
        ProductName = productName;
        Price = price;
    }

    public String getProductPicture() {
        return ProductPicture;
    }

    public void setProductPicture(String productPicture) {
        ProductPicture = productPicture;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
