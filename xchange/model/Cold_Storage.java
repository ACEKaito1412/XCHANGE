package com.project.xchange.model;

public class Cold_Storage {
    private String key;
    private String senders_id;
    private String receivers_id;
    private String order_id;
    private Double amount;


    public Cold_Storage() {
    }

    public Cold_Storage(String key, String senders_id, String receivers_id, String order_id, Double amount) {
        this.key = key;
        this.senders_id = senders_id;
        this.receivers_id = receivers_id;
        this.order_id = order_id;
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSenders_id() {
        return senders_id;
    }

    public void setSenders_id(String senders_id) {
        this.senders_id = senders_id;
    }

    public String getReceivers_id() {
        return receivers_id;
    }

    public void setReceivers_id(String receivers_id) {
        this.receivers_id = receivers_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
