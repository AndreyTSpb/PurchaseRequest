package com.example.purchaserequests;

import com.example.purchaserequests.models.Managers;
import com.example.purchaserequests.models.PurchaseOrders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DirectoryEmployeeTableController extends MainController implements Initializable {

    @FXML
    public TableView<Managers> tableEmployee;
    @FXML
    private TableColumnHeader tableColumnHeader;

    @FXML
    private TableRow<Managers> tableRow;

    @FXML
    private TableColumn<Managers, Integer> id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Заполняем значения на сцене
     */
    public void setData(int idUser, int idRole, String loginUser) throws SQLException {
        this.idUser = idUser;
        this.idRole = idRole;
        this.loginUser = loginUser;
        this.fieldTable();

        setupListener(); //ставим слушателя на нажатие в таблице
    }

    private void setupListener(){
        tableEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    idOrder = tableEmployee.getSelectionModel().getSelectedItem().idProperty().getValue();
                    try {
                        viewManager(mouseEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void viewManager(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("formCreatEmployee.fxml"));
        Parent root = loader.load();
        FormCreatEmployeeController formCreateOrder = loader.getController();

        stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Set Data to FXML through controller
        formCreateOrder.setData(this.idUser,this.idRole,this.loginUser, idOrder);
    }

    private void fieldTable(){
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        String query = "SELECT " +
                "m.id AS id, " +
                "m.fio AS fio, " +
                "m.email AS email, " +
                "m.login AS login, " +
                "r.name AS role " +
                "FROM `managers` AS m LEFT JOIN roles AS r ON m.id_role = r.id " +
                "ORDER BY m.fio ASC;";

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
                    case "email":
                        column.setText("email");
                        break;
                    case "login":
                        column.setText("Логин");
                        break;
                    case "role":
                        column.setText("Роль");
                        break;
                    case "name":
                        column.setText("Роль");
                        break;
                    case "fio":
                        column.setText("Менеджер");
                        break;
                    default: column.setText(resultSet.getMetaData().getColumnName(i+1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                        break;
                }
                column.setCellValueFactory(new PropertyValueFactory<>(resultSet.getMetaData().getColumnName(i+1))); //Setting cell property value to correct variable from Person class.
                tableEmployee.getColumns().add(column);
            }

            //Filling up tableView with data
            tableEmployee.setItems(dbData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private ArrayList dataBaseArrayList(ResultSet resultSet) throws SQLException{
        ArrayList<Managers> data =  new ArrayList<>();
        while (resultSet.next()) {
            Managers manager = new Managers();
            manager.id.set(resultSet.getInt("id"));
            manager.fio.set(resultSet.getString("fio"));
            manager.email.set(resultSet.getString("email"));
            manager.login.set(resultSet.getString("login"));
            manager.name.set(resultSet.getString("role"));
            data.add(manager);
        }
        return data;
    }

    @FXML
    public void creatEmployee(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("formCreatEmployee.fxml"));
        Parent root = loader.load();
        FormCreatEmployeeController formCreateOrder = loader.getController();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Set Data to FXML through controller
        formCreateOrder.setData(this.idUser,this.idRole,this.loginUser, 0);
    }
}
