package com.example.purchaserequests;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * одключение к БД
 */
public class DBConnector {
    public static Connection getConnection() throws SQLException{
        String url = "jdbc:mysql://localhost:3306/";
        String dataBase = "procurement_register";
        String userName = "root";
        String password = "root";
        return DriverManager.getConnection(url+dataBase, userName, password);
    }
}
