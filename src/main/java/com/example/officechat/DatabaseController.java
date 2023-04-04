package com.example.officechat;

import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseController {
    public Connection databaseLink;
    public Connection getConnection(){
        String databaseName = "accountdatabase";
        String databaseUser = "root";
        String databasePassword = "root";
        String url = "jdbc:mysql://172.19.10.194:3306/" + databaseName;

        try{
          Class.forName("com.mysql.cj.jdbc.Driver");
          databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }
}
