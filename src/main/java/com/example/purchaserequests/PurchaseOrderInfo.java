package com.example.purchaserequests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PurchaseOrderInfo {
    public int idOrder;
    public int idManager;
    public String nameManager;
    public String emailManager;
    public int idClient;
    public String nameClient;
    public String numContract;
    public Date dtCreate;
    public Date dtDesired;
    public int idManagerZ;
    public String namePurchaseManager;
    public String emailPurchaseManager;
    public Date dtStartJob;
    public Date dtClose;
    public int status;
    public String nameStatus;
    public Date dtConfirm;
    public int confirmed;
    public String nameConfirmed;
    public String noteManager;
    public String notePurchase;
    public String noteComm;
    public int del;

    /**
     * Данные Строки по заявке
     */
    public int idOrderString;
    public String nameProduct;
    public String descriptionProduct;
    public int qnt;
    public float purchasePrice;
    public Date dtDelivery;
    public String noteProduct;
    public int delProduct;
    public float desiredPrice;

    /**
     * Выборка полных данных по одной заявке
     * @param idOrder
     */
    public PurchaseOrderInfo(int idOrder){
        /**
         * Получение основных данных
         */
        String query = "SELECT p_o.id AS id, " +
                "p_o.id_manager AS id_manager, "+
                "m.fio AS manager, " +
                "m.email AS email_manager, " +
                "p_o.id_client AS id_client, "+
                "c.customer AS customer, " +
                "p_o.num_contract AS num_contract, " +
                "p_o.dt_create AS dt_create, " +
                "p_o.dt_desired AS dt_desired, " +
                "p_o.id_menager_z AS id_manager_z, " +
                "mz.fio AS fio_z, " +
                "mz.email AS email_z, " +
                "p_o.dt_start_job AS dt_start_job, " +
                "p_o.dt_close AS dt_close, " +
                "p_o.status AS status, " +
                "p_o.dt_confirm AS dt_confirm, " +
                "p_o.confirmed AS confirmed, " +
                "p_o.note_menager AS note_manager, " +
                "p_o.note_purchase AS note_purchase, " +
                "p_o.note_comm AS note_comm " +
                "FROM `purchase_orders` AS p_o " +
                "LEFT JOIN `managers` AS mz ON p_o.id_menager_z = mz.id " +
                "LEFT JOIN `managers` AS m ON p_o.id_manager = m.id " +
                "LEFT JOIN `customers` AS c ON p_o.id_client = c.id " +
                "WHERE p_o.id = ?";
        try(Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, idOrder);

            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    this.idOrder        = rez.getInt("id");
                    this.idManager      = rez.getInt("id_manager");
                    this.nameManager    = rez.getString("manager");
                    this.emailManager   = rez.getString("email_manager");
                    this.idClient       = rez.getInt("id_client");
                    this.nameClient     = rez.getString("customer");
                    this.numContract    = rez.getString("num_contract");
                    this.dtCreate       = rez.getDate("dt_create");
                    this.dtDesired      = rez.getDate("dt_desired");
                    this.idManagerZ     = rez.getInt("id_manager_z");
                    this.namePurchaseManager    = rez.getString("fio_z");
                    this.emailPurchaseManager   = rez.getString("email_z");
                    this.dtStartJob     = rez.getDate("dt_start_job");
                    this.dtClose        = rez.getDate("dt_close");
                    this.status         = rez.getInt("status");
                    this.dtConfirm      = rez.getDate("dt_confirm");
                    this.confirmed      = rez.getInt("confirmed");
                    this.noteManager    = rez.getString("note_manager");
                    this.notePurchase   = rez.getString("note_purchase");
                    this.noteComm       = rez.getString("note_comm");
                }
                /**
                 * Получение даннх по строке
                 */
                getOrderString(idOrder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getOrderString(int idOrder){
        String query = "SELECT * FROM `purchase_order_string` WHERE id_purchase_order = ? AND del=0;";
        System.out.println(query);
        try(Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, idOrder);

            System.out.println("this.nameProduct");

            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    this.idOrderString      = rez.getInt("id");
                    this.nameProduct        = rez.getString("name_product");
                    this.descriptionProduct = rez.getString("description_product");
                    this.desiredPrice       = rez.getFloat("desired_price");
                    this.qnt                = rez.getInt("qnt");
                    this.purchasePrice      = rez.getFloat("purchase_price");
                    this.dtDelivery         = rez.getDate("dt_delivery");
                    this.noteProduct        = rez.getString("note");
                    this.delProduct         = rez.getInt("del");
                    System.out.println(this.nameProduct);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
