package com.example.mobileapp.model;

public class Cart {
    private String ProductPicture;
    private String ProductName;
    private String Quantity;
    private String Price;

    public Cart(){}

    public Cart(String productPicture, String productName, String quantity, String price) {
        ProductPicture = productPicture;
        ProductName = productName;
        Quantity = quantity;
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

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}