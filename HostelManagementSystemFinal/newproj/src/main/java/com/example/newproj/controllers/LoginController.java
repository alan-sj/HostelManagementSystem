package com.example.newproj.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.newproj.Main;
import com.example.newproj.util.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TextField userNameTF;

    @FXML
    void login(ActionEvent event) throws SQLException {
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = DatabaseUtil.getConnection();

        String userName = userNameTF.getText();
        String password = passwordTF.getText();

        String verifyLogin = "SELECT count(1) FROM user WHERE username = '" + userName + "' AND password = '" + password + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()) {
                if(queryResult.getInt(1) == 1) {
                    loginButton.setText("Login Successful");
                    Main mainApp = new Main();
                    Main.loadDashboardScene();
                } else {
                    loginButton.setText("Invalid Login. Retry?");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

}

