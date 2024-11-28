package com.example.v8;

public class Administrator extends User{

    public Administrator(int userId, String userName, String password) {
        super(userId, userName, password);
        super.setAdmin(true);
    }


}
