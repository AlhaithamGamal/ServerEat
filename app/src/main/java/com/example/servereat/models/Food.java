package com.example.servereat.models;

public class Food {
    private  String Name;
    private  String Description;
    private  String Price;
    private String Discount;

   public  Food(){


    }

    public Food(String Name, String Description, String Price, String Discount, String Image, String MenuId) {
        this.Name = Name;
        this.Description = Description;
        this.Discount = Discount;
        this.Price = Price;
        this.Image = Image;
        this.MenuId = MenuId;

    }

    private String Image;
    private String MenuId;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
