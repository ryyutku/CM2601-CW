package com.example.newsrecommendation;

public class Administrator extends User{

    public Administrator(int userId, String username, String password) {
        super(userId, username, password);
        super.setAdmin(true);
    }


}
