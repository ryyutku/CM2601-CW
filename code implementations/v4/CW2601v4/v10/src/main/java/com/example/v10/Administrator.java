package com.example.v10;

public class Administrator extends User{

    public Administrator(int userId, String username, String password) {
        super(userId, username, password);
        super.setAdmin(true);
    }


}
