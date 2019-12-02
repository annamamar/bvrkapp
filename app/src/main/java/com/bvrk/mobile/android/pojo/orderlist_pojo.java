package com.bvrk.mobile.android.pojo;

public class orderlist_pojo {

    String order_id,order_status;
    String order_date,order_amount;
    String order_vendor,order_client;
    String order_type,order_miscdetails;
    String vendor_amount,order_credit;
    String order_udate;

    public String getOrder_udate() {
        return order_udate;
    }

    public void setOrder_udate(String order_udate) {
        this.order_udate = order_udate;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getVendor_amount() {
        return vendor_amount;
    }

    public void setVendor_amount(String vendor_amount) {
        this.vendor_amount = vendor_amount;
    }

    public orderlist_pojo(String order_id, String order_date, String order_amount, String order_vendor,String vendor_amount, String order_client, String order_type,String order_status, String order_miscdetails,String order_credit,String order_udate) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.order_amount = order_amount;
        this.order_vendor = order_vendor;
        this.vendor_amount = vendor_amount;
        this.order_client = order_client;
        this.order_type = order_type;
        this.order_miscdetails = order_miscdetails;
        this.order_status = order_status;
        this.order_credit = order_credit;
        this.order_udate = order_udate;
    }

    public String getOrder_credit() {
        return order_credit;
    }

    public void setOrder_credit(String order_credit) {
        this.order_credit = order_credit;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_vendor() {
        return order_vendor;
    }

    public void setOrder_vendor(String order_vendor) {
        this.order_vendor = order_vendor;
    }

    public String getOrder_client() {
        return order_client;
    }

    public void setOrder_client(String order_client) {
        this.order_client = order_client;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_miscdetails() {
        return order_miscdetails;
    }

    public void setOrder_miscdetails(String order_miscdetails) {
        this.order_miscdetails = order_miscdetails;
    }

    @Override
    public String toString() {
        return "orderlist_pojo { " +
                "order_id='" + order_id + '\'' +
                ", order_date='" + order_date + '\'' +
                ", order_amount='" + order_amount + '\'' +
                ", order_vendor='" + order_vendor + '\'' +
                ", order_client='" + order_client + '\'' +
                ", order_type='" + order_type + '\'' +
                ", order_miscdetails='" + order_miscdetails + '\'' +
                " }";
    }
}
