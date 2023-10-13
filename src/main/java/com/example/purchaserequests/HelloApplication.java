package com.example.purchaserequests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainBoard.fxml"));
        Parent root = fxmlLoader.load();

        MainBoard mainBoard = fxmlLoader.getController();

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Список заявок в закупку!");
        stage.setScene(scene);
        stage.show();
        //Set Data to FXML through controller
        String loginUser = "spb.tav";
        /**
         * Получаем данные о пользователе
         */
        ManagerInfo userInfo = new ManagerInfo(loginUser);
        int idUser = userInfo.idUser;
        int idRole = userInfo.idRole;

        mainBoard.setData(idUser, idRole, loginUser);
    }

    public static void main(String[] args) {
        launch();
    }
}