package com.example.atmmachine;

public class User {
    public String uid;
    String userName;
    public String email;
    public String password;

    public User(String uid, String userName, String email, String password) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
