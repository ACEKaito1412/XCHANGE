package com.project.xchange.model;

public class Balance {
    private double balance;

    public Balance(){
        this.balance = 0.0;
    }

    public Balance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
