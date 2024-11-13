module com.example.newproj{
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires mysql.connector.j;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;

    opens com.example.newproj to javafx.fxml;
    opens com.example.newproj.controllers to javafx.fxml;
    exports com.example.newproj;
    exports com.example.newproj.controllers;
    exports com.example.newproj.Models;
    exports com.example.newproj.util;
}