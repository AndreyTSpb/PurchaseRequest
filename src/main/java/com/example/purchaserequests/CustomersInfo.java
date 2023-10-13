package com.example.purchaserequests;

import com.example.purchaserequests.models.Customers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomersInfo {
    public int idCustomer;
    public String nameCustomer;
    public String code;

    public CustomersInfo(int id){
        String query = "SELECT * FROM customers WHERE id = ?";
        try(Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, id);

            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    idCustomer = id;
                    nameCustomer = rez.getString("customer");
                    code = rez.getString("code");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public CustomersInfo(String customer){
        String query = "SELECT * FROM customers WHERE customer = ?";
        try(Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, customer);

            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    idCustomer      = rez.getInt("id");
                    nameCustomer    = customer;
                    code            = rez.getString("code");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
