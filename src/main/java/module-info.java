module com.example.barcode {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.barcode to javafx.fxml;
    exports com.example.barcode;
}