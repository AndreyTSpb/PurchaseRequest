package com.example.purchaserequests.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Roles {
    public IntegerProperty id = new SimpleIntegerProperty();
    public StringProperty name = new SimpleStringProperty();
    public IntegerProperty del = new SimpleIntegerProperty();


    public IntegerProperty idProperty() { //имя складывается из названия поля в таблице + Property [colName(id) + Property]
        return id;
    }

    public StringProperty nameProperty() { return name; }

    public IntegerProperty delProperty() { //имя складывается из названия поля в таблице + Property [colName(id) + Property]
        return del;
    }

    public Roles() {
    }

    public Roles(int id, String name, int del){
        this.id.set(id);
        this.name.set(name);
        this.del.set(del);
    }
}
