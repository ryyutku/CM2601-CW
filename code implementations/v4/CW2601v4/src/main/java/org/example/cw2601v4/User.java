package org.example.cw2601v4;

public class User {

//    Holds the users data
    private int userId;
    private String username;
    private String password;


    private String[] user_preferences;
    private String[] history;



    public int getUserId() {
        return userId;
    }

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
}