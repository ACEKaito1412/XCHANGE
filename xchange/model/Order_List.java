package com.project.xchange.model;

import android.widget.TextView;

import java.util.Date;

public class Order_List {
    private String key;
    private String productKey;
    private String productImage;
    private String productName;
    private String storageKey;
    private String orderStatus;
    private String orderDate;
    private double productPrice;
    private int productQuantity;

    public Order_List() {
    }

    public Order_List(String key, String productKey, String productImage, String productName, String storageKey, String orderStatus, String orderDate, double productPrice, int productQuantity) {
        this.key = key;
        this.productKey = productKey;
        this.productImage = productImage;
        this.productName = productName;
        this.storageKey = storageKey;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
