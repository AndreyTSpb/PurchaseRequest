package com.example.purchaserequests;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

import static com.example.purchaserequests.GetResultSetRowCount.getResultSetRowCount;

public class FormCreateOrder extends MainController implements Initializable {
    public MenuItem closeBtn;
    public TextField desiredPrice;
    public MenuItem employees;
    public MenuItem roles;



    @FXML
    private ComboBox customer;

    @FXML
    private TextField numContract;

    @FXML
    private DatePicker dtDesired;

    @FXML
    private TextField nameProduct;

    @FXML
    private TextField qntProduct;

    @FXML
    private TextArea noteManager;

    @FXML
    public void closeBtnClick(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }



    /**
     * Read data form
     */
    @FXML
    protected void pushBtnAddOrder(ActionEvent event) throws SQLException, IOException {
        String nameCustomer = (String) customer.getValue();
        String numContr =  numContract.getText();
        String dt = String.valueOf(dtDesired.getValue());
        String note = noteManager.getText();
        String nameProduct = this.nameProduct.getText();
        int qnt = Integer.parseInt(this.qntProduct.getText());
        Float desiredPrice = Float.valueOf(this.desiredPrice.getText());
        insertOrderInPurchaseOrders(nameCustomer, numContr, dt, nameProduct, qnt, note, desiredPrice);
        returnMain(event);
    }

    /**
     * Заполняем значения формы
     */
    public void setData(int idUser, int idRole, String loginUser) throws SQLException {
        this.loginUser = loginUser;
        this.idUser = idUser;
        this.idRole = idRole;
        openBtnDictory();

        /**
         * Зополняем выподающий списоок
         */
        customer.getItems().clear();
        String[] itemCustomers = getNameCustomers();
        for(int i= 0; i<itemCustomers.length; i++){
            customer.getItems().add(itemCustomers[i]);
        }

    }
    /**
     * Зополняем выподающий списоок клиентов
     */
    private String[] getNameCustomers() throws SQLException {

        String query = "SELECT * FROM customers";
        try (
                Connection conn = DBConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rez = stmt.executeQuery(query);
        ){
            ArrayList<String> ar = new ArrayList<String>();
            while (rez.next()){
                ar.add(rez.getString("customer"));
            }
            int countRow = getResultSetRowCount(rez); //получили количество строк в выборке
            String[] myArray = ar.toArray(new String[countRow]); //массив куда будем складывать названия

            rez.close();
            return myArray;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Записываем данные с формы в таблицу
     */
    private void insertOrderInPurchaseOrders(String nameCustomer, String numContract, String dtDesired, String nameProduct, int qntProduct, String noteManager, float desiredPrice) throws SQLException{
        String query = "INSERT INTO purchase_orders (id_manager, num_contract, dt_desired, note_menager, id_client ) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idUser);

            stmt.setString(2, numContract);

            java.sql.Date sqlDate = java.sql.Date.valueOf(dtDesired); //conver string to SL Date
            stmt.setDate(3, sqlDate);

            //stmt.setString(1, fName);
            stmt.setString(4, noteManager);

            CustomersInfo customersInfo = new CustomersInfo( nameCustomer);
            stmt.setInt(5, customersInfo.idCustomer);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // id order
                long id = rs.getLong(1);
                insertPurchaseOrderString((int) id, nameProduct, qntProduct, desiredPrice);
            }
            stmt.close();
        }
    }

    private void insertPurchaseOrderString(int idOrder, String nameProduct, int qntProduct, float desiredPrice){
        String query = "INSERT INTO purchase_order_string (id_purchase_order, name_product, qnt, desired_price) VALUES ( ?, ?, ?, ?);";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idOrder);
            stmt.setString(2, nameProduct);
            stmt.setInt(3, qntProduct);
            stmt.setFloat(4, desiredPrice);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
