package com.example.atmmachine;

public class UserData {
    public String uid;
    String userName;
    public String email;
    public int balance;

    public UserData(String uid, String userName, String email, int balance) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
        this.balance = balance;
    }

    public UserData(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserData(String userName, String email, int balance) {
        this.userName = userName;
        this.email = email;
        this.balance = balance;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
