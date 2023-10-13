package com.example.purchaserequests;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    public MenuItem employees;
    public MenuItem roles;

    protected String loginUser;
    protected int idUser;
    protected int idRole;

    public int idOrder; //строка в таблице

    /**
     * Пункт из меню выход
     */
    @FXML
    public MenuBar myMenuBar;
    @FXML
    public MenuItem tableOrdersBtn;
    @FXML
    private MenuItem closeBtn;

    @FXML
    public void openTableOrders(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainBoard.fxml"));
        Parent root = fxmlLoader.load();

        MainBoard mainBoard = fxmlLoader.getController();
        //для работы с меню по другому обращаться к сцене
        stage = (Stage) myMenuBar.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //fill table
        mainBoard.setData(this.idUser, this.idRole, this.loginUser);
    }
    @FXML
    public void openDirectoryRoles(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("directoryRolesTable.fxml"));
        Parent root = fxmlLoader.load();

        DirectoryRolesTableController directoryRolesTableController = fxmlLoader.getController();
        //для работы с меню по другому обращаться к сцене
        this.stage = (Stage) myMenuBar.getScene().getWindow();
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
        //fill table
        directoryRolesTableController.setData(this.idUser, this.idRole, this.loginUser);
    }

    /**
     * Переход к справочнику
     */
    @FXML
    protected void openDirectoryEmployee(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("directoryEmployeeTable.fxml"));
        Parent root = fxmlLoader.load();

        DirectoryEmployeeTableController directoryEmployeeTableController = fxmlLoader.getController();
        //для работы с меню по другому обращаться к сцене
        this.stage = (Stage) myMenuBar.getScene().getWindow();
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
        //fill table
        directoryEmployeeTableController.setData(this.idUser, this.idRole, this.loginUser);
    }

    /**
     * Close prog
     */
    @FXML
    protected void closeBtnClick(){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void returnMain(ActionEvent event) throws IOException, SQLException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainBoard.fxml"));
        Parent root = fxmlLoader.load();

        MainBoard mainBoard = fxmlLoader.getController();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //fill table
        mainBoard.setData(this.idUser, this.idRole, this.loginUser);
    }

    protected void openBtnDictory(){
        if(this.idRole == 1){
            this.employees.setDisable(false);
            this.roles.setDisable(false);
        }
    }

    public void setData(int idUser, int idRole, String loginUser, int idRow) throws SQLException {
        this.loginUser  = loginUser;
        this.idUser     = idUser;
        this.idRole     = idRole;
        this.idOrder    = idRow;
        openBtnDictory();
    }

    public void setData(int idUser, int idRole, String loginUser) throws SQLException {
        this.loginUser = loginUser;
        this.idUser = idUser;
        this.idRole = idRole;
        openBtnDictory();
    }
}
