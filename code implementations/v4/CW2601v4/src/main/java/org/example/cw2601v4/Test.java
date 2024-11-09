package org.example.cw2601v4;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Test {

    public void readCSV() throws IOException {
        System.out.println("Inside the readCSV method");
        String file = "src/main/resources/article.csv";
        BufferedReader reader = null;
        String line = "";

        try {
            int count_row =0;

            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                // Use a regular expression to split on commas not inside quotes
                String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Check each value and set to null if empty
                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].trim(); // Remove any surrounding whitespace
                    if (row[i].isEmpty()) {
                        row[i] = null;
                    }
                }
                // For example, you could then print each value or store it in an object
//                System.out.println("Author: " + row[0]+" |Title: " + row[1]+" |Category: " + row[6]);

                System.out.println(count_row+" |Category: "+row[4]+" |Title: "+row[0]);
                System.out.println("------------------------");
                count_row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
        System.out.println("Finished reading the csv");
    }

    public static void main(String[] args) throws IOException {
        new Test().readCSV();
    }

}
