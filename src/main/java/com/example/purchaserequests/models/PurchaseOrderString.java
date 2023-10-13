package com.example.purchaserequests.models;

import javafx.beans.property.*;

public class PurchaseOrderString {
    public IntegerProperty id = new SimpleIntegerProperty();
    public IntegerProperty idPurchaseOrder = new SimpleIntegerProperty();
    public StringProperty nameProduct = new SimpleStringProperty();
    public StringProperty descriptionProduct = new SimpleStringProperty();
    public FloatProperty desiredPrice = new SimpleFloatProperty();

    public IntegerProperty qnt = new SimpleIntegerProperty();
    public FloatProperty purchasePrice = new SimpleFloatProperty();
    public IntegerProperty del = new SimpleIntegerProperty();

    public IntegerProperty idProperty() { //имя складывается из названия поля в таблице + Property [colName(id) + Property]
        return id;
    }
    public IntegerProperty id_purchase_orderProperty() { //имя складывается из названия поля в таблице + Property [colName(id) + Property]
        return idPurchaseOrder;
    }
    public StringProperty name_productProperty() { return nameProduct; }
    public StringProperty description_productProperty() { return descriptionProduct; }
    public FloatProperty desired_priceProperty() { return  desiredPrice; }
    public FloatProperty purchase_priceProperty() { return  purchasePrice; }
    public IntegerProperty delProperty() { return del; }
    public IntegerProperty qntProperty() { return qnt; }

    public PurchaseOrderString(){};

    /**
     *
     * @param id
     * @param idPurchaseOrder
     * @param nameProduct
     * @param descriptionProduct
     * @param desiredPrice
     * @param purchasePrice
     * @param del
     */
    public PurchaseOrderString(int id, int idPurchaseOrder, String nameProduct, int qnt,String descriptionProduct, float desiredPrice, float purchasePrice, int del){
        this.id.set(id);
        this.idPurchaseOrder.set(idPurchaseOrder);
        this.nameProduct.set(nameProduct);
        this.descriptionProduct.set(descriptionProduct);
        this.desiredPrice.set(desiredPrice);
        this.purchasePrice.set(purchasePrice);
        this.del.set(del);
        this.qnt.set(qnt);
    }
}
