module com.example.barcode {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires lombok;
    requires java.sql;

    opens com.example.barcode.entity to javafx.base;
    opens com.example.barcode to javafx.fxml;
    exports com.example.barcode;
}