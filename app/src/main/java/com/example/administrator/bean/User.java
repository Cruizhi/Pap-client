package com.example.administrator.bean;

/**
 * Created by Administrator on 2017/9/30.
 */

public class User {
    private String username;
    private String userid;
    private String password;
    private String address;
    private String phone;
    private String email;

//    public User(){}
//
//    public User(String userid,String username,String password,String address,String phone,String email){
//        this.userid = userid;
//        this.username = username;
//        this.password = password;
//        this.address = address;
//        this.phone = phone;
//        this.email = email;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
