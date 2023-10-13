package com.example.purchaserequests;



import com.example.purchaserequests.models.PurchaseOrders;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;


import javafx.event.EventHandler;


import static com.example.purchaserequests.GetResultSetRowCount.getResultSetRowCount;

public class MainBoard extends MainController implements Initializable {

    private ObservableList<ObservableList> data;

    @FXML
    private TableView<PurchaseOrders> tableOrders;

    @FXML
    private TableColumnHeader tableColumnHeader;

    @FXML
    private TableRow<PurchaseOrders> tableRow;

    @FXML
    private TableColumn<PurchaseOrders, Integer> id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Заполняем значения на сцене
     */
    public void setData(int idUser, int idRole, String loginUser) throws SQLException {
        super.setData(idUser, idRole, loginUser);
        fillTableOrders();
        setupListener();
    }

    /**
     * Open form create new order
     */
    @FXML
    protected void createOrder(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("formCreateOrder.fxml"));
        Parent root = loader.load();
        FormCreateOrder formCreateOrder = loader.getController();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Set Data to FXML through controller
        formCreateOrder.setData(this.idUser,this.idRole,this.loginUser);
    }

    private void setupListener(){
        tableOrders.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    idOrder = tableOrders.getSelectionModel().getSelectedItem().idProperty().getValue();
                    try {
                        viewPurchaseOrder(mouseEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    /**
     *
     * @param mouseEvent
     * @throws IOException
     * @throws SQLException
     */
    private void viewPurchaseOrder(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("formViewOrder.fxml"));
        Parent root = loader.load();

        FormViewOrder formViewOrder = loader.getController();

        stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Set Data to FXML through controller
        formViewOrder.setData(this.idUser, this.idRole, this.loginUser, this.idOrder);
    }

    /**
     * Заполняем таблицу данными из БД
     */
    private void fillTableOrders(){

        this.data = FXCollections.observableArrayList();

        String query = "SELECT p_o.id AS id, " +
                "c.customer AS customer, " +
                "p_o.num_contract AS num_contract, " +
                "p_o.dt_create AS dt_create, " +
                "p_o.dt_desired AS dt_desired, " +
                "m.fio AS fio, " +
                "p_o.dt_start_job AS dt_start_job, " +
                "p_o.dt_close AS dt_close, " +
                "(CASE p_o.status " +
                "WHEN 1 THEN 'В работе' " +
                "WHEN 2 THEN 'На утверждении' " +
                "WHEN 3 THEN 'Выполнено' " +
                "WHEN 4 THEN 'Отказано'" +
                "ELSE 'Ожидает' " +
                "END) AS status " +
                "FROM `purchase_orders` AS p_o " +
                    "LEFT JOIN `managers` AS m ON p_o.id_menager_z = m.id " +
                    "LEFT JOIN `customers` AS c ON p_o.id_client = c.id " +
                "WHERE p_o.id_manager = (SELECT id FROM managers WHERE login = '"+loginUser+"') AND p_o.del = 0 ORDER BY dt_create DESC;";
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
                    case "customer":
                        column.setText("Клиент");
                        break;
                    case "num_contract":
                        column.setText("Номер контракта");
                        break;
                    case "dt_create":
                        column.setText("Дата заказа");
                        break;
                    case "fio":
                        column.setText("Менеджер");
                        break;
                    default: column.setText(resultSet.getMetaData().getColumnName(i+1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                        break;
                }
                column.setCellValueFactory(new PropertyValueFactory<>(resultSet.getMetaData().getColumnName(i+1))); //Setting cell property value to correct variable from Person class.
                tableOrders.getColumns().add(column);
            }

            //Filling up tableView with data
            tableOrders.setItems(dbData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение данных из таблици
     * extracting data from ResulSet to ArrayList
     */
    private ArrayList dataBaseArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<PurchaseOrders> data =  new ArrayList<>();
        while (resultSet.next()) {
            PurchaseOrders purchaseOrders = new PurchaseOrders();
            purchaseOrders.id.set(resultSet.getInt("id"));
            purchaseOrders.nameCustomer.set(resultSet.getString("customer"));
            purchaseOrders.num_contract.set(resultSet.getString("num_contract"));
            purchaseOrders.dt_create.set(String.valueOf(resultSet.getDate("dt_create")));
            purchaseOrders.dt_desired.set(String.valueOf(resultSet.getDate("dt_desired")));
            purchaseOrders.name_m_z.set(resultSet.getString("fio"));
            purchaseOrders.dt_start_job.set(String.valueOf(resultSet.getDate("dt_start_job")));
            purchaseOrders.dt_close.set(String.valueOf(resultSet.getDate("dt_close")));
            purchaseOrders.statuss.set(resultSet.getString("status"));
            data.add(purchaseOrders);
        }
        return data;
    }
}
