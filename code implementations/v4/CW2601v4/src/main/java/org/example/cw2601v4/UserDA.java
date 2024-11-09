package org.example.cw2601v4;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserDA {

    private DBConnector connection;
    Scanner scanner = new Scanner(System.in);


    public UserDA(DBConnector dbConnector) {
        this.connection = dbConnector;
    }


    //    to check if the user exist if the username exist then return true
//    to check if the username exists - return true if it does exist
    public boolean checkUser(String username){
        String query = "SELECT * FROM user WHERE username = ?";

        try (
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
        ) {
            // Set the value of the placeholder
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if(username.equals(resultSet.getString("username"))){
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }

//    After checking if the user exist checks whether the password is correct for it
    public boolean checkPassword(String username,String password){
//        Gets the password from the user corresponding to the username
        String query = "SELECT password FROM user WHERE username = ? ";

        try(
                PreparedStatement stmt = connection.getConnection().prepareStatement(query)
        ){
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){ //Move the cursor the next row, and if username exist compare the passwords
                String hashedPassword = resultSet.getString("password");
                if(hashedPassword.equals(password)){
                    return true;
                }
                return resultSet.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

//    To add a username when signing up
    public void addUser(String username,String password){

        String query = "INSERT INTO user (username, password) VALUES (?, ?)";

        try(
                PreparedStatement statement = connection.getConnection().prepareStatement(query);
                ){

//            Check if the user exist
            if(!checkUser(username)){
                String hashedPassword = password;

                statement.setString(1, username);
                statement.setString(2, hashedPassword);

                statement.executeUpdate();
                System.out.println("Added user "+username);
            }else{
                System.out.println("User already exists in the name of "+username);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void changePassword(){
        String query = "UPDATE user SET password = ? WHERE username = ?";
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        try(
                PreparedStatement statement = connection.getConnection().prepareStatement(query);
                ){
            if(checkUser(username)){
//                If the user exist you can enter the new password
                System.out.println("Enter password: ");
                String currentPass = scanner.nextLine();
                if(checkPassword(username,currentPass)){
//                    If the current password is valid proceed to change password
                    System.out.println("Type in new password: ");
                    String newPass = scanner.nextLine();
                    statement.setString(1, newPass);
                    statement.setString(2, username);
                    statement.executeUpdate();
                    System.out.println("Changed password of user "+username);
                }else{
                    System.out.println("Wrong password entered");
                }
            }else{
                System.out.println("User does not exist in the name of "+username);
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }
//    add methods for the user/can be used for changing names as well
//    -Add user
//    -Add/change password
//    -Add other details -->(preferences, liked books,........)


    public static void main(String[] args){
        UserDA user = new UserDA(new DBConnector());
        user.addUser("user7","pass7");
        user.changePassword();
    }



}
