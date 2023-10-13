package com.example.purchaserequests;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.purchaserequests.GetResultSetRowCount.getResultSetRowCount;

public class FormCreatEmployeeController extends MainController implements Initializable{
    public Button btnAddEmployee;
    public TextField familia;
    public TextField name;
    public TextField surname;
    public TextField email;
    public TextField login;
    public ComboBox role;
    private int idRow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void setData(int idUser, int idRole, String loginUser, int idRow) throws SQLException {
        this.idUser = idUser;
        this.idRole = idRole;
        this.loginUser = loginUser;
        this.idRow = idRow;


        /**
         * Взависимости от того передан ли айди строки или нет
         * меняем поведение кнопки "сохранить"
         */
        if(this.idRow > 0){
            //Заполнение данных формы
            this.fillFormFields();
        }else{
            //Combobox
            this.role.getItems().clear();
            String[] itemRoles = getRole();
            for(int i= 0; i<itemRoles.length; i++){
                this.role.getItems().add(itemRoles[i]);
            }
            //добавит
            btnAddEmployee.setText("Добавить");
            btnAddEmployee.setOnAction(event -> {
                this.creatEmployee();
                try {
                    //возврвт в список аользователей
                    this.returnPage(event);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Заполняем данные формы
     * @throws SQLException
     */
    private void fillFormFields() throws SQLException {
        this.role.getItems().clear();
        String[] itemRoles = getRole();
        for(int i= 0; i<itemRoles.length; i++){
            this.role.getItems().add(itemRoles[i]);
        }
        ManagerInfo managerInfo = new ManagerInfo(this.idRow);
        //Поля
        String[] partsFio = managerInfo.fio.split(" ");
        this.familia.setText(partsFio[0]);
        this.name.setText(partsFio[1]);
        this.surname.setText(partsFio[2]);
        this.email.setText(managerInfo.email);
        this.login.setText(managerInfo.loginUser);
        this.role.getSelectionModel().select(managerInfo.idRole-1);

        //События на кнопку
        this.btnAddEmployee.setText("Обновить");
        btnAddEmployee.setOnAction(event -> {
            this.updateEmployee();
            try {
                //возврвт в список пользователей
                this.returnPage(event);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Обновляем информацию о менеджере
     */
    private void updateEmployee() {
        String query = "UPDATE `managers` " +
                "SET `fio` = ?, " +
                "`email` = ?, " +
                "`login` = ?, " +
                "`id_role` = ? " +
                "WHERE `managers`.`id` = ?;";
        String fio = this.familia.getText() + " " + this.name.getText() + " " + this.surname.getText();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fio);
            stmt.setString(2, this.email.getText());
            stmt.setString(3, this.login.getText());
            String role = (String) this.role.getValue();
            stmt.setInt(4, this.getIdRole(role));
            stmt.setInt(5, this.idRow);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохраняем сотрудника в базу
     */
    private void creatEmployee() {
        String query = "INSERT INTO `managers` (`fio`, `email`, `login`, `id_role`) VALUES (?, ?, ?, ?);";
        String fio = this.familia.getText() + " " + this.name.getText() + " " + this.surname.getText();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fio);
            stmt.setString(2, this.email.getText());
            stmt.setString(3, this.login.getText());
            String role = (String) this.role.getValue();
            stmt.setInt(4, this.getIdRole(role));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * получаем идентификатор роли
     * @param role
     * @return
     */
    private int getIdRole(String role){
        int id = 0;
        String query = "SELECT id FROM `roles` WHERE name = ?;";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role);
            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    id = rez.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Возврат на список пользователей
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    private void returnPage(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("directoryEmployeeTable.fxml"));
        Parent root = loader.load();
        DirectoryEmployeeTableController directoryEmployeeTableController = loader.getController();

        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Set Data to FXML through controller
        directoryEmployeeTableController.setData(this.idUser,this.idRole,this.loginUser);
    }

    /**
     * Получаем список ноименований ролей
     * для селектора на форме
     * @return
     * @throws SQLException
     */
    private String[] getRole() throws SQLException {

        String query = "SELECT * FROM roles";
        try (
                Connection conn = DBConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rez = stmt.executeQuery(query);
        ){
            ArrayList<String> ar = new ArrayList<String>();
            while (rez.next()){
                ar.add(rez.getString("name"));
            }
            int countRow = getResultSetRowCount(rez); //получили количество строк в выборке
            String[] myArray = ar.toArray(new String[countRow]); //массив куда будем складывать названия

            rez.close();
            return myArray;
        }
    }
}
