package com.project.xchange.model;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String from_ID;
    private String to_ID;
    private int Amount;
    private String Date;
    private String Transaction_Type;

    public Transaction() {
    }

    public Transaction(String from_ID, String to_ID, int amount, String date, String transaction_Type) {
        this.from_ID = from_ID;
        this.to_ID = to_ID;
        Amount = amount;
        Date = date;
        Transaction_Type = transaction_Type;
    }

    public String getFrom_ID() {
        return from_ID;
    }

    public String getTo_ID() {
        return to_ID;
    }

    public int getAmount() {
        return Amount;
    }

    public String getDate() {
        return Date;
    }

    public String getTransaction_Type() {
        return Transaction_Type;
    }

    public void setFrom_ID(String from_ID) {
        this.from_ID = from_ID;
    }

    public void setTo_ID(String to_ID) {
        this.to_ID = to_ID;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTransaction_Type(String transaction_Type) {
        Transaction_Type = transaction_Type;
    }
}
