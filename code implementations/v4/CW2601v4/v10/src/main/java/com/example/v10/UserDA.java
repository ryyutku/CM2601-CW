package com.example.v10;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Class to handle the connection between the user and the Database
public class UserDA {
    DBConnector connection;


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

//    method to get userId from the database from the given username
    public int getUserId(String username) {
        String query = "SELECT userId FROM user WHERE username = ?";
        int userId = -1; // Default value if user is not found

        try (
                PreparedStatement stmt = connection.getConnection().prepareStatement(query)
        ) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            // Check if there is a result
            if (resultSet.next()) {
                userId = resultSet.getInt("userId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userId;
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

    //    To add a username when signing up, and adding the user to the database
    public void addUser(String username,String password){

        String query = "INSERT INTO user (username, password,role) VALUES (?, ?, ?)";

        try(
                PreparedStatement statement = connection.getConnection().prepareStatement(query);
        ){

//            Check if the user exist
            if(!checkUser(username)){
                String hashedPassword = password;

                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                statement.setString(3, "user");


                statement.executeUpdate();
                System.out.println("Added user "+username);
            }else{
                System.out.println("User already exists in the name of "+username);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

//    Method to handle interaction between the userpreference table in the Database

//    Check if the user has a record of the preference corresponding to the category
    public boolean checkUserPreferenceExists(int userId, String category) {
        String query = "SELECT pscore FROM upreferences WHERE userId = ? AND category = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
            // Set query parameters
            statement.setInt(1, userId);
            statement.setString(2, category);

            try (ResultSet resultSet = statement.executeQuery()) {
                // If there is a result, the combination exists
                if (resultSet.next()) { // Check if a row exists
                    double pscore = resultSet.getDouble("pscore"); // Retrieve the pscore
                    System.out.println("pscore = " + pscore);
                    return true; // Indicates the user preference exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Indicates the user preference does not exist
    }






// If th user has a preference, get the preference score from the userpreference table in the database
    public double accessPreferenceScore(int userId, String category) throws SQLException {
        System.out.println("Inside the accesPrefeneceScore method inside UserDA(acces)");
        System.out.println("UserId: "+userId+" Category: "+category);
        String query = "SELECT pscore FROM upreferences WHERE userId = ? AND category = ?";

        System.out.println("Category in accessPreferenceScore: "+category);



        try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
            // Set query parameters
            statement.setInt(1, userId);
            statement.setString(2, category);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // If there is a result, the combination exists

                while(resultSet.next()){
                    double pscore = resultSet.getDouble("pscore");
                    System.out.println("pscore = "+pscore);
                    return pscore;

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1; // can return -1 as well
        }

    }


//    Method to make a preference record if there is not one for user and category ***********
public void insertPreference(int userId, String category) {
    String insertQuery = "INSERT INTO upreferences (userId, category) VALUES (?, ?)";

    try (
            PreparedStatement insertStatement = connection.getConnection().prepareStatement(insertQuery)
    ) {
        // Set query parameters
        insertStatement.setInt(1, userId);        // userId
        insertStatement.setString(2, category);    // articleId

        // Execute the insert
        int rowsAffected = insertStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("New record successfully inserted into userpreference.");
        } else {
            System.out.println("Failed to insert the record into userprefernce.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//Method to update a preference for the current users's corresponding category if a record doesn't exist
    public void updatePreference(int userId, String category,double score) throws SQLException {

        System.out.println("Inside the updatePreference");
        System.out.println("Category in updatePreference: "+category);



//        check if the combination of the user_id and the category exist, if not make a row
        if(checkUserPreferenceExists(userId, category)){
            System.out.println("UpdatePreference:=> the user exist");

//            access its preference score and then add to it
            double pscore= accessPreferenceScore(userId,category);
            double fscore = pscore + score;
            updatePreferenceScore(userId, category,fscore);
        }else{
//            When there isn't a row in the user preference table with the combination
            insertPreference(userId,category);
            updatePreferenceScore(userId,category,score);
        }

    }

//Update the user preference score into the database
    public boolean updatePreferenceScore(int userId, String category, double newScore) {
        String updateQuery = "UPDATE upreferences SET pscore = ? WHERE userId = ? AND category = ?";

        System.out.println("Inside the updatePreferenceScore");
        System.out.println("Category in updatePreferenceScore: "+category);



        try (
                PreparedStatement updateStatement = connection.getConnection().prepareStatement(updateQuery)) {

            // Set query parameters
            updateStatement.setDouble(1, newScore); // New pScore value
            updateStatement.setInt(2, userId);
            updateStatement.setString(3, category);

            // Execute update
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("pScore updated successfully.");
                return true; // Indicates the update was successful
            } else {
                System.out.println("No matching record found for the specified userId and category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Indicates that no update occurred
    }


//     ==================================METHODS TO UPDATE THE HISTORY TABLE=======================================

//    method to check if the combination of "userId" "articleId" exist and get the "historyId"
    public boolean accessHistory(int userId, int articleId) throws SQLException {
        System.out.println("Inside the accesPrefeneceScore method inside UserDA(acces)");
        System.out.println("UserId: "+userId+" articleId: "+articleId);
        String query = "SELECT historyId FROM userhistory WHERE userId = ? AND articleId = ?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
            // Set query parameters

            statement.setInt(1, userId);
            statement.setInt(2, articleId);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // If there is a result, the combination exists

                while(resultSet.next()){
                    double historyId = resultSet.getDouble("historyId");
                    System.out.println("historyId = "+historyId);
                    return true;

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; // can return -1 as well
        }
    }



//    method to update interaction
    public void updateInteraction(String interaction,int userId, int articleId) throws SQLException {
        String updateQuery = "UPDATE userhistory SET interaction = ? WHERE articleId = ? and userId = ?";
        if( accessHistory(userId, articleId) ){
            try (
                    PreparedStatement updateStatement = connection.getConnection().prepareStatement(updateQuery)) {

                // Set query parameters
                updateStatement.setString(1, interaction); // New pScore value
                updateStatement.setInt(2, articleId); //need to get the userHistory id {{{{FIGURE OUT}}}}
                updateStatement.setInt(3,userId);


                // Execute update
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("pScore updated successfully.");
                } else {
                    System.out.println("No matching record found for the specified userId and category.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("The record doesn't exist in the user history table");
        }
    }



//    method to update the ratings
    public void updateRatings(int rating,int userId, int articleId) throws SQLException {
        String updateQuery = "UPDATE userhistory SET ratings = ? WHERE articleId = ? and userId = ?";
        if( accessHistory(userId, articleId) ){
            try (
                    PreparedStatement updateStatement = connection.getConnection().prepareStatement(updateQuery)) {

                // Set query parameters
                updateStatement.setInt(1, rating); // New pScore value
                updateStatement.setInt(2, articleId); //need to get the userHistory id {{{{FIGURE OUT}}}}
                updateStatement.setInt(3,userId);


                // Execute update
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("pScore updated successfully.");
                } else {
                    System.out.println("No matching record found for the specified userId and category.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("The record doesn't exist in the user history table");
        }
    }

    // Method to insert a new record into userhistory table
    public void insertUserHistory(int userId, int articleId) {
        String insertQuery = "INSERT INTO userhistory (userId, articleId) VALUES (?, ?)";

        try (
                PreparedStatement insertStatement = connection.getConnection().prepareStatement(insertQuery)
        ) {
            // Set query parameters
            insertStatement.setInt(1, userId);        // userId
            insertStatement.setInt(2, articleId);    // articleId

            // Execute the insert
            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("New record successfully inserted into userhistory.");
            } else {
                System.out.println("Failed to insert the record into userhistory.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateHistory(int userId,int articleId) throws SQLException {
        if(accessHistory(userId, articleId) ){
            System.out.println("The history record already exist");

        }else{
            insertUserHistory(userId, articleId);
        }
    }



}
