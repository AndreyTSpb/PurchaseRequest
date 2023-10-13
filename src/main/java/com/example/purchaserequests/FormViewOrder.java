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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.purchaserequests.GetResultSetRowCount.getResultSetRowCount;

public class FormViewOrder extends MainController implements Initializable {
    public TextField nameCustomer;
    public TextField numContract;
    public DatePicker dateDesired;
    public MenuItem closeBtn;
    public TextField nameManager;
    public TextField emailManager;
    public TextField namePurManager;
    public TextField emailPurManager;
    public Tab tabManager;
    public Tab tabPurchase;
    public Tab tabCommerc;
    public Button updateOrder;
    public Button deleteOrder;
    public TextField desiredPrice;
    public Button btnConfirm;
    public Button btnRefuse;
    public TextArea noteComfirm;


    @FXML
    private Button pushBtnRequestOrder;

    @FXML
    private TextField nameProduct;
    @FXML
    private TextField qntProduct;
    @FXML
    private TextArea noteManager;
    @FXML
    private TextField supplierCost;
    @FXML
    private DatePicker dateDelivery;
    @FXML
    private TextArea purchaseNote;
    @FXML
    private Button pushSendApprovalOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    /**
     * Сахранение изменений для менеджера
     */
    @FXML
    protected void updateOrderManager(){
        /**
         * Обновляем желаемые дату и примечание
         */
        String query = "UPDATE `purchase_orders` SET dt_desired = ?, note_menager = ? WHERE `purchase_orders`.`id` = ?;";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(this.dateDesired.getValue()));
            stmt.setString(2, this.noteManager.getText());
            stmt.setInt(3, this.idOrder);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String queryItem = "UPDATE `purchase_order_string` SET `name_product` = ?, `qnt` = ?, `desired_price` = ? WHERE `purchase_order_string`.`id_purchase_order` = ?;";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryItem)) {
            stmt.setString(1, this.nameProduct.getText());
            stmt.setInt(2, Integer.parseInt(this.qntProduct.getText()));
            stmt.setFloat(3, Float.parseFloat(this.desiredPrice.getText()));
            stmt.setInt(4, this.idOrder);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void deleteOrderManager(){
        String query = "UPDATE `purchase_orders` SET del = '1' WHERE `purchase_orders`.id = ?;";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.idOrder);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushBtnRequestOrder(ActionEvent actionEvent) {
        //
    }

    public void pushSendApprovalOrder(ActionEvent actionEvent) {
    }

    /**
     * Заполняем значения на сцене
     */
    public void setData(int idRow, int idUser, int idRole, String loginUser) throws SQLException {
        this.idOrder    = idRow;
        this.idUser     = idUser;
        this.idRole     = idRole;
        this.loginUser  = loginUser;
        openBtnDictory();
        this.changeStatusInJob();// еняем статус и присваиваем менеджера

        /**
         * Заполнения данных по заказу из таблицы
         */
        this.getDataOrderV2();
    }

    /**
     * Get data order
     */
    private void getDataOrder() throws SQLException {
        String query = "SELECT p_o.id AS id, " +
                "m.fio AS manager, " +
                "m.email AS email_manager, " +
                "c.customer AS customer, " +
                "p_o.num_contract AS num_contract, " +
                "p_o.dt_create AS dt_create, " +
                "p_o.dt_desired AS dt_desired, " +
                "mz.fio AS fio_z, " +
                "mz.email AS email_z, " +
                "p_o.dt_start_job AS dt_start_job, " +
                "p_o.dt_close AS dt_close, " +
                "p_o.note_menager AS note_menager, " +
                "p_o.note_purchase AS note_purchase, " +
                "(CASE p_o.status " +
                "WHEN 1 THEN 'В работе' " +
                "WHEN 2 THEN 'На утверждении' " +
                "WHEN 3 THEN 'Выполнено' " +
                "WHEN 4 THEN 'Отказано'" +
                "ELSE 'Ожидает' " +
                "END) AS status " +
                "FROM `purchase_orders` AS p_o " +
                "LEFT JOIN `managers` AS mz ON p_o.id_menager_z = mz.id " +
                "LEFT JOIN `managers` AS m ON p_o.id_manager = m.id " +
                "LEFT JOIN `customers` AS c ON p_o.id_client = c.id " +
                "WHERE p_o.id = ?";

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);){
            stmt.setInt(1, this.idOrder);

            try (ResultSet rez = stmt.executeQuery()) {
                while (rez.next()) {
                    /*Order manager*/
                    this.nameManager.setText(rez.getString("manager"));
                    this.emailManager.setText(rez.getString("email_manager"));
                    this.nameCustomer.setText(rez.getString("customer"));
                    this.numContract.setText(rez.getString("num_contract"));
                    //this.dateDesired.setText(rez.getString("dt_desired"));
                    this.dateDesired.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(rez.getDate("dt_desired")) ));
                    this.noteManager.setText(rez.getString("note_menager"));
                    /*request purchase*/
                    this.namePurManager.setText(rez.getString("fio_z"));
                    this.emailPurManager.setText(rez.getString("email_z"));
                    this.purchaseNote.setText(rez.getString("note_purchase"));

                    /**
                     * Проверка на статус заявки, для блокировки органов управления на форме
                     * Ожидает:  блокируем вкладку менеджера по закупкам и подтверждение заявки
                     * В работе:  Открывается вкладка для менеджера закупки
                     * На утверждении:
                     * Отказано:
                     */
                    switch (rez.getString("status")){
                        case "Ожидает":
                            this.tabPurchase.setDisable(true);
                            this.deleteOrder.setDisable(false);
                            this.updateOrder.setDisable(false);
                            break;
                        case "В работе":
                            this.tabPurchase.setDisable(false);
                            this.tabCommerc.setDisable(true);
                            break;
                        case "На утверждении":
                            this.tabPurchase.setDisable(false);
                            this.tabCommerc.setDisable(false);
                            this.dateDelivery.setEditable(false);
                            this.purchaseNote.setEditable(false);
                            this.supplierCost.setEditable(false);
                            this.pushBtnRequestOrder.setDisable(true);
                            this.pushSendApprovalOrder.setDisable(true);
                            break;
                        default:
                            this.deleteOrder.setDisable(true);
                            this.updateOrder.setDisable(true);
                            this.dateDelivery.setEditable(false);
                            this.purchaseNote.setEditable(false);
                            this.supplierCost.setEditable(false);
                            this.pushBtnRequestOrder.setDisable(true);
                            this.pushSendApprovalOrder.setDisable(true);
                            break;
                    }
                    
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void blockedElement(int status){
        /**
         * Проверка на статус заявки, для блокировки органов управления на форме
         * Ожидает:  блокируем вкладку менеджера по закупкам и подтверждение заявки
         * В работе:  Открывается вкладка для менеджера закупки
         * На утверждении:
         * Отказано:
         */
        switch (status){
            case 0:
                /*Ожидает*/
                this.tabPurchase.setDisable(true); //вкладка закупки
                this.tabCommerc.setDisable(true);
                /**
                 * Разделение на доступ взависимости от ролей ролей
                 */
                this.enableBtnManager();
                this.enableFieldManager();

                break;
            case 1:
                /*В работе*/
                this.tabPurchase.setDisable(false);//включение вкладки закупка
                this.tabCommerc.setDisable(true); //выключена вкладка подтверждения
                this.disableBtnManager();
                this.disableFieldManager();
                /**
                 * Разделение на доступ взависимости от ролей ролей
                 */
                this.enableFieldPurchase();
                this.enableBtnPurchase();

                break;
            case 2:
                /*На подтверждении*/
                this.tabPurchase.setDisable(false); //включение вкладки закупка
                this.tabCommerc.setDisable(false); //включение вкладки подтверждения

                this.disableFieldPurchase();
                this.disableBtnManager();
                this.disableFieldManager();
                this.disableBtnPurchase();
                /**
                 * Разделение на доступ взависимости от ролей ролей
                 */
                this.enableConfirm();

                break;
            default:
                this.disableFieldPurchase();
                this.disableBtnManager();
                this.disableFieldManager();
                this.disableBtnPurchase();
                this.disableConfirm();
                break;
        }
    }

    private void enableConfirm(){
        this.btnConfirm.setDisable(false);
        this.btnRefuse.setDisable(false);
    }
    private  void disableConfirm(){
        this.btnConfirm.setDisable(true);
        this.btnRefuse.setDisable(true);
    }
    private void enableFieldPurchase(){
        this.dateDelivery.setEditable(true);
        this.purchaseNote.setEditable(true);
        this.supplierCost.setEditable(true);
    }
    private void disableFieldPurchase(){
        this.dateDelivery.setEditable(false);
        this.purchaseNote.setEditable(false);
        this.supplierCost.setEditable(false);
    }
    private void enableBtnPurchase(){
        this.pushBtnRequestOrder.setDisable(false);
        this.pushSendApprovalOrder.setDisable(false);
    }
    private void disableBtnPurchase(){
        this.pushBtnRequestOrder.setDisable(true);
        this.pushSendApprovalOrder.setDisable(true);
    }
    private void disableBtnManager(){
        this.deleteOrder.setDisable(true); //кнопка удалить заявку
        this.updateOrder.setDisable(true); //кнопка обновить заявку
    }
    private void enableBtnManager(){
        this.deleteOrder.setDisable(false); //кнопка удалить заявку
        this.updateOrder.setDisable(false); //кнопка обновить заявку
    }
    private void disableFieldManager(){
        this.dateDesired.setEditable(false); //поле желаемая дата
        this.nameProduct.setEditable(false); //поле название продукта
        this.qntProduct.setEditable(false); //поле количество к заказу
        this.desiredPrice.setEditable(false); //поле желаемая цена
    }
    private void enableFieldManager(){
        this.dateDesired.setEditable(true); //поле желаемая дата
        this.nameProduct.setEditable(true); //поле название продукта
        this.qntProduct.setEditable(true); //поле количество к заказу
        this.desiredPrice.setEditable(true); //поле желаемая цена
        this.noteManager.setEditable(true);
    }

    private void getDataOrderV2(){
        PurchaseOrderInfo purchaseOrderInfo = new PurchaseOrderInfo(this.idOrder);
        /*Вкладка менеджера*/
        //Основная часть
        this.nameManager.setText(purchaseOrderInfo.nameManager);
        this.emailManager.setText(purchaseOrderInfo.emailManager);
        this.nameCustomer.setText(purchaseOrderInfo.nameClient);
        this.numContract.setText(purchaseOrderInfo.numContract);
        if(purchaseOrderInfo.dtDesired != null) {
            this.dateDesired.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(purchaseOrderInfo.dtDesired)));
        }
        this.noteManager.setText(purchaseOrderInfo.noteManager);
        //Описание товара
        this.nameProduct.setText(purchaseOrderInfo.nameProduct);
        this.qntProduct.setText(String.valueOf(purchaseOrderInfo.qnt));
        this.desiredPrice.setText(String.valueOf(purchaseOrderInfo.desiredPrice));

        /*Вкладка менеджера закупки*/
        //основная часть
        this.namePurManager.setText(purchaseOrderInfo.namePurchaseManager);
        this.emailPurManager.setText(purchaseOrderInfo.emailPurchaseManager);
        this.purchaseNote.setText(purchaseOrderInfo.notePurchase);
        //Про товар
        this.supplierCost.setText(String.valueOf(purchaseOrderInfo.purchasePrice));
        System.out.println(purchaseOrderInfo.dtDelivery);
        if(purchaseOrderInfo.dtDelivery != null){
            this.dateDelivery.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(purchaseOrderInfo.dtDelivery) ));
        }

        /*Вкладка подтверждения*/

        /*Активация елементов управления*/
        blockedElement(purchaseOrderInfo.status);
    }

    /**
     * Отмечаем как в работе,
     * если открыл менеджер по закупке (4)
     */
    private void changeStatusInJob(){
        if(this.idRole == 4){
            String query = "UPDATE `purchase_orders` SET id_menager_z = ?, status = '1', `dt_start_job` = ?  WHERE `purchase_orders`.`id` = ? AND status = 0;";
            try (Connection conn = DBConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, this.idUser);
                java.sql.Timestamp dateNow = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                stmt.setTimestamp(2, dateNow);
                stmt.setInt(3, this.idOrder);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
