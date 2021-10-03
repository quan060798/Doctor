package com.example.doctor;

public class History {
    private String cust_name;
    private String comment;

    public History(String cust_name, String comment) {
        this.cust_name = cust_name;
        this.comment = comment;
    }

    public History() {

    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
