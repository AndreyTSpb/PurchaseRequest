package com.example.purchaserequests.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PurchaseOrders {
    public IntegerProperty id = new SimpleIntegerProperty(); //variable names should be exactly as column names in SQL Database Table. In case if you want to use <int> type instead of <IntegerProperty>, then you need to use getter/setter procedures instead of xxxProperty() below
    public StringProperty id_client = new SimpleStringProperty();

    public StringProperty nameCustomer = new SimpleStringProperty();
    public StringProperty num_contract = new SimpleStringProperty();
    public StringProperty dt_create = new SimpleStringProperty();
    public StringProperty dt_desired = new SimpleStringProperty();
    public IntegerProperty id_menager_z = new SimpleIntegerProperty();
    public StringProperty name_m_z = new SimpleStringProperty();
    public StringProperty dt_start_job = new SimpleStringProperty();
    public StringProperty dt_close = new SimpleStringProperty();
    public StringProperty statuss = new SimpleStringProperty();


    public IntegerProperty idProperty() { //имя складывается из названия поля в таблице + Property [colName(id) + Property]
        return id;
    }

    public StringProperty id_clientProperty() {
        return id_client;
    }
    public StringProperty customerProperty() {
        return nameCustomer;
    }
    public StringProperty fioProperty() {
        return name_m_z;
    }

    public StringProperty num_contractProperty() {
        return num_contract;
    }
    public StringProperty dt_createProperty(){
        return dt_create;
    }
    public StringProperty dt_desiredProperty(){
        return dt_desired;
    }
    public IntegerProperty id_menager_zProperty(){
        return id_menager_z;
    }
    public StringProperty dt_start_jobProperty(){
        return dt_start_job;
    }
    public StringProperty dt_closeProperty(){
        return dt_close;
    }
    public StringProperty statusProperty(){
        return statuss;
    }

    public PurchaseOrders(int idValue, String idClient, String numContract, String dtCreate, String dtDesired, String dtStartJob, int idManagerZ, String dtClose, String status, String customer, String nameManagerZ) {
        id.set(idValue);
        id_client.set(idClient);
        num_contract.set(numContract);
        dt_create.set(dtCreate);
        dt_desired.set(dtDesired);
        id_menager_z.set(idManagerZ);
        dt_start_job.set(dtStartJob);
        dt_close.set(dtClose);
        statuss.set(status);
        nameCustomer.set(customer);
        name_m_z.set(nameManagerZ);
    }

    public PurchaseOrders(){}
}
