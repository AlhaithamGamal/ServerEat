package com.example.servereat.models;

public class Order {
    private String ProductId;
    private String Productname;
    private String Quantity;
    private String Price;
    private String Discount;


    Order(){

    }
    public Order(String productId, String productname, String quantity, String price, String discount) {
        setProductId(productId);
        setProductname(productname);
        setQuantity(quantity);
        setPrice(price);
        setDiscount(discount);
    }


    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
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

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
