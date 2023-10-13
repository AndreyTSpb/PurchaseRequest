package com.example.purchaserequests.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Managers {
    public IntegerProperty id = new SimpleIntegerProperty();
    public StringProperty fio = new SimpleStringProperty();
    public StringProperty email = new SimpleStringProperty();
    public StringProperty login = new SimpleStringProperty();
    public IntegerProperty id_role = new SimpleIntegerProperty();
    public StringProperty role = new SimpleStringProperty();
    public StringProperty name = new SimpleStringProperty();
    public IntegerProperty del = new SimpleIntegerProperty();


    public IntegerProperty idProperty() { //имя складывается из названия поля в таблице + Property [colName(id) + Property]
        return id;
    }

    public StringProperty fioProperty(){
        return  fio;
    }
    public StringProperty emailProperty(){
        return  email;
    }
    public StringProperty loginProperty(){
        return  login;
    }
    public IntegerProperty id_roleProperty(){
        return  id_role;
    }
    public StringProperty roleProperty(){
        return  role;
    }
    public StringProperty nameProperty(){
        return  name;
    }
    public IntegerProperty delProperty(){
        return  del;
    }



    public Managers(int idValue, String fioValue, String emailValue, String loginValue, int id_roleValue, int delValue, String role){
        id.set(idValue);
        fio.set(fioValue);
        email.set(emailValue);
        login.set(loginValue);
        id_role.set(id_roleValue);
        this.role.set(role);
        name.set(role);
        del.set(delValue);
    }

    public Managers(){}
}
