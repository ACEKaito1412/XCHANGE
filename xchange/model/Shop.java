package com.project.xchange.model;

public class Shop {
    private String Image;
    private String Item_Name;
    private Double Price;

    public Shop( String image, String item_Name, Double price) {
        Image = image;
        Item_Name = item_Name;
        Price = price;
    }

    public Shop() {
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}
