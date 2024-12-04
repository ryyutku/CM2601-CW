package com.example.v10;

public class User {

    //    User class
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

//    Setters and getters for the users' attributes
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
