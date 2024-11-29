package com.example.v9;

public class Administrator extends User{

    public Administrator(int userId, String userName, String password) {
        super(userId, userName, password);
        super.setAdmin(true);
    }


}
