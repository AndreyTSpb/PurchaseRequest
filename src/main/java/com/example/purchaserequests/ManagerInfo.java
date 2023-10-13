package com.example.purchaserequests;

import java.sql.*;
import java.util.ArrayList;

import static com.example.purchaserequests.GetResultSetRowCount.getResultSetRowCount;

public class ManagerInfo {
    public int idUser;
    public String fio;
    public String email;
    public String loginUser;
    public int idRole;

    public ManagerInfo(int id){
        String query = "SELECT * FROM managers WHERE id = ?";
        try(Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, id);

            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    idUser = id;
                    fio = rez.getString("fio");
                    email = rez.getString("email");
                    loginUser = rez.getString("login");
                    idRole = rez.getInt("id_role");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ManagerInfo(String login){
        String query = "SELECT * FROM managers WHERE login = ?";

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setString(1, login);

            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    idUser = rez.getInt("id");
                    fio = rez.getString("fio");
                    email = rez.getString("email");
                    loginUser = login;
                    idRole = rez.getInt("id_role");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

}
