module com.example.numero_primo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.numero_primo to javafx.fxml;
    exports com.example.numero_primo;
}