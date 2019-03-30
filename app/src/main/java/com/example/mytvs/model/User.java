package com.example.mytvs.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String passWord;


    public User() { }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
