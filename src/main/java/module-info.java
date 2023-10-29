module com.example.purchaserequests {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jdk.security.auth;

    opens com.example.purchaserequests to javafx.fxml;
    exports com.example.purchaserequests;
    exports com.example.purchaserequests.models;
    opens com.example.purchaserequests.models to javafx.fxml;
}