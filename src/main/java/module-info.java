module com.example.knights_tours {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.knights_tours to javafx.fxml;
    exports com.example.knights_tours;
}