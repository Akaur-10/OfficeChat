module com.example.officechat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.officechat to javafx.fxml;
    exports com.example.officechat;
}