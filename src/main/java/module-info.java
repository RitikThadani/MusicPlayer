module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;
    requires java.desktop;
    requires java.sql;
    requires jaudiotagger;
    requires mysql.connector.j;

    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
    exports com.example.demo2.controladores;
    opens com.example.demo2.controladores to javafx.fxml;
}