package com.example.v9;


import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {

    private Connection connection;

    //    Constructor connects with the database
    public DBConnector() {

        try{
//            Connecting to the database mysql
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database","root","root");
            System.out.println("Connected to database(DBConnector)");


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public  Connection getConnection(){
        return this.connection;
    }

    public void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }




}
