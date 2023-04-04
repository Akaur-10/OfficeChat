package com.example.officechat;

import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class NotificationRunner extends Task<Integer> {
    DatabaseController connectNow = new DatabaseController();
    Connection connectDB = connectNow.getConnection();
    String user;
    int currMessages;
    int storedMessages = 0;
    boolean bool = true;


    @Override
    protected Integer call() throws Exception {
        user = getSenderName();
        String sql = "select count(messages) from messages where reciever = '" + user + "' and archived = '0';";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sql);
            queryResult.next();
            currMessages = queryResult.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        while(bool==true) {
            try {
                Statement statement2 = connectDB.createStatement();
                ResultSet queryResult = statement2.executeQuery(sql);
                queryResult.next();
                storedMessages = queryResult.getInt(1);
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }

            if(storedMessages>currMessages){
                updateValue(storedMessages);
                currMessages = storedMessages;
                System.out.println("X updated = "+currMessages);
            }
            else if (storedMessages<currMessages) {
                currMessages = storedMessages;
            }
            Thread y = new Thread();
            y.sleep(3000);
        }
        return 0;
    }

    public String getSenderName() throws IOException {
        String data = "";
        try {
            File myObj = new File("loggedIn.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
}
