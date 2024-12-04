package com.example.v10;


//Singleton class to used to share instances of classes and the database instances
public class Singleton {

    private static Singleton instance;
//    Database connection
    private DBConnector dbConnector;

//    User instance
    private User user;



    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }

        return instance;
    }

    public DBConnector getDBConnector() {
        return dbConnector;
    }

    public void setDBConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
