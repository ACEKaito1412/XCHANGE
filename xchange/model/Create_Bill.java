package com.project.xchange.model;

public class Create_Bill {
    private String utility_type;
    private String company_name;
    private String customer_name;
    private String ref_id;
    private Double amount;
    private String date;
    private String bill_status;
    private String account_number;

    public Create_Bill(String utility_type, String company_name, String customer_name, String ref_id, Double amount, String date, String bill_status, String account_number) {
        this.utility_type = utility_type;
        this.company_name = company_name;
        this.customer_name = customer_name;
        this.ref_id = ref_id;
        this.amount = amount;
        this.date = date;
        this.bill_status = bill_status;
        this.account_number = account_number;
    }

    public Create_Bill() {
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBill_status() {
        return bill_status;
    }

    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }

    public String getUtility_type() {
        return utility_type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getRef_id() {
        return ref_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setUtility_type(String utility_type) {
        this.utility_type = utility_type;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
