package com.project.xchange.model;

public class Cart {
    private String key;
    private String product_name;
    private String product_price;
    private int quantity;
    private String image_url;
    private String status;

    public Cart(String key,
                String product_name,
                String product_price,
                String image_url,
                int quantity,
                String status) {
        this.key = key;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.image_url = image_url;
        this.status = status;
    }

    public Cart() {
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
