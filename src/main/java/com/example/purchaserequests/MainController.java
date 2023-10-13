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

    protected int idOrder;

    /**
     * Пункт из меню выход
     */
    @FXML
    public MenuBar myMenuBar;
    @FXML
    private MenuItem closeBtn;

    public void openDirectoryRoles(ActionEvent actionEvent) {
        //
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
        }
    }
}
