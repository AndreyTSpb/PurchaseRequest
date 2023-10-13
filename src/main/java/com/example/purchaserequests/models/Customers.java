package com.example.purchaserequests.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customers {
    public IntegerProperty id       = new SimpleIntegerProperty();
    public StringProperty customer  = new SimpleStringProperty();
    public StringProperty code      = new SimpleStringProperty();
    public IntegerProperty del      = new SimpleIntegerProperty();

    public IntegerProperty idProperty() { return id; }
    public StringProperty customerProperty(){
        return  customer;
    }
    public StringProperty codeProperty(){ return  code; }
    public IntegerProperty delProperty(){
        return  del;
    }

    public Customers(){}

    public Customers(int idValue, String nameValue, String codeValue, int delValue){
        id.set(idValue);
        customer.set(nameValue);
        code.set(codeValue);
        del.set(delValue);
    }
}
