package com.example.purchaserequests;

import com.example.purchaserequests.models.Managers;
import com.example.purchaserequests.models.Roles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
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

public class DirectoryRolesTableController extends MainController implements Initializable {
    @FXML
    public TableView<Roles> tableRoles;
    @FXML
    private TableColumnHeader tableColumnHeader;

    @FXML
    private TableRow<Roles> tableRow;

    @FXML
    private TableColumn<Roles, Integer> id;
    protected int idOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Заполняем значения на сцене
     */
    public void setData(int idUser, int idRole, String loginUser) throws SQLException {
        super.setData(idUser, idRole, loginUser);
        this.fieldTable(); //Заполняем таблицу
    }

    private void fieldTable() {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM `roles` ORDER BY name ASC";

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
                    case "name":
                        column.setText("Роль");
                        break;
                    default: column.setText(resultSet.getMetaData().getColumnName(i+1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                        break;
                }
                column.setCellValueFactory(new PropertyValueFactory<>(resultSet.getMetaData().getColumnName(i+1))); //Setting cell property value to correct variable from Person class.
                tableRoles.getColumns().add(column);
            }

            //Filling up tableView with data
            tableRoles.setItems(dbData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList dataBaseArrayList(ResultSet resultSet) throws SQLException{
        ArrayList<Roles> data =  new ArrayList<>();
        while (resultSet.next()) {
            Roles roles = new Roles();
            roles.id.set(resultSet.getInt("id"));
            roles.name.set(resultSet.getString("name"));
            data.add(roles);
        }
        return data;
    }
}
