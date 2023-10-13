package com.example.purchaserequests;

import com.example.purchaserequests.models.PurchaseOrders;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SimpleTable extends Application {
    private TableView<PurchaseOrders> tableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException {
        //Show window
        buildData();
        Parent root = tableView;
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public void buildData() throws ClassNotFoundException, SQLException {



        String query = "SELECT p_o.id AS id, p_o.id_client AS id_client, p_o.num_contract AS num_contract, p_o.dt_create AS dt_create, p_o.dt_desired AS dt_desired, p_o.id_menager_z AS id_menager_z, p_o.dt_start_job AS dt_start_job, p_o.dt_close AS dt_close, p_o.status AS status FROM `purchase_orders` AS p_o " +
                "WHERE p_o.id_manager = (SELECT id FROM managers WHERE login = 'spb.tav') AND p_o.del = 0" +
                " ORDER BY dt_create DESC;";
        ResultSet resultSet;

        try(Connection conn = DBConnector.getConnection()){

            //ResultSet
            resultSet = conn.createStatement().executeQuery(query);

            ObservableList dbData = FXCollections.observableArrayList(dataBaseArrayList(resultSet));

            //Giving readable names to columns
            for(int i=0 ; i<resultSet.getMetaData().getColumnCount(); i++) {
                TableColumn column = new TableColumn<>();
                switch (resultSet.getMetaData().getColumnName(i+1)) {
                    case "id":
                        column.setText("ID #");
                        break;
                    case "id_client":
                        column.setText("Клиент");
                        break;
                    case "num_contract":
                        column.setText("Номер контракта");
                        break;
//                    case "dt_create":
//                        column.setText("Дата заказа");
//                        break;
                    default: column.setText(resultSet.getMetaData().getColumnName(i+1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                        break;
                }
                column.setCellValueFactory(new PropertyValueFactory<>(resultSet.getMetaData().getColumnName(i+1))); //Setting cell property value to correct variable from Person class.
                tableView.getColumns().add(column);
            }

            //Filling up tableView with data
            tableView.setItems(dbData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //extracting data from ResulSet to ArrayList
    private ArrayList dataBaseArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<PurchaseOrders> data =  new ArrayList<>();
        while (resultSet.next()) {
            PurchaseOrders purchaseOrders = new PurchaseOrders();

            purchaseOrders.id.set(resultSet.getInt("id"));
//            purchaseOrders.id_client.set(resultSet.getString("id_client"));
//            purchaseOrders.num_contract.set(resultSet.getString("num_contract"));
//            purchaseOrders.dt_create.set(String.valueOf(resultSet.getDate("dt_create")));
//            purchaseOrders.dt_desired.set(String.valueOf(resultSet.getDate("dt_desired")));
            data.add(purchaseOrders);
        }
        return data;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
