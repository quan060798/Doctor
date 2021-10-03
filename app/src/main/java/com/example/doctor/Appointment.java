package com.example.doctor;

public class Appointment {
    private String doc_name;
    private String cust_name;
    private String cust_phonenum;
    private String Date;
    private String Time;
    private String doc_approval;
    private String decline_reason;
    private String bookedby;
    private String comment;

    public Appointment(String decline_reason) {
        this.decline_reason = decline_reason;
    }

    public Appointment() {
    }

    public String getBookedby() {
        return bookedby;
    }

    public void setBookedby(String bookedby) {
        this.bookedby = bookedby;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDecline_reason() {
        return decline_reason;
    }

    public void setDecline_reason(String decline_reason) {
        this.decline_reason = decline_reason;
    }

    public String getDoc_approval() {
        return doc_approval;
    }

    public void setDoc_approval(String doc_approval) {
        this.doc_approval = doc_approval;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_phonenum() {
        return cust_phonenum;
    }

    public void setCust_phonenum(String cust_phonenum) {
        this.cust_phonenum = cust_phonenum;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String toString(){
        return "Customer Name: " + this.cust_name+ " " + this.Date;
    }

}
