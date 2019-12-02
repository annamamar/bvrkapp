package com.bvrk.mobile.android.pojo;

public class userlist_pojo {

    public String id;
    public String name;
    public String mobile;
    public String username;
    public String location;
    public String address;
    public String usertype;
    public String wallet;
    public String ordercount;

    public userlist_pojo(String id,String name, String mobile, String username, String location, String address, String usertype, String wallet,String ordercount) {
        this.id = id;
        this.name       = name;
        this.mobile     = mobile;
        this.username   = username;
        this.location   = location;
        this.address    = address;
        this.usertype   = usertype;
        this.wallet     = wallet;
        this.ordercount = ordercount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(String ordercount) {
        this.ordercount = ordercount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "userlist_pojo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", usertype='" + usertype + '\'' +
                ", wallet='" + wallet + '\'' +
                ", ordercount='" + ordercount + '\'' +
                '}';
    }
}
