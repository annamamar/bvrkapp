package com.bvrk.mobile.android.pojo;

import java.io.Serializable;

public class userinfo implements Serializable {

    public String name;
    public String mobile;
    public String username;
    public String address;
    public String location;
    public String usertype;

    @Override
    public String toString() {
        return "userinfo{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
