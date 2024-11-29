package com.example.v8;

public class User {

    //    Holds the users data
    private int userId;
    private String username;
    private String password;

    private boolean admin;


    public User(){}

    public User(int userId, String username, String password){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {return userId;}

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}