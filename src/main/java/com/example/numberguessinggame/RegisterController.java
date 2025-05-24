package com.example.numberguessinggame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField newUsernameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label messageLabel;

    @FXML
    void handleRegisterButtonAction(ActionEvent event) {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("All fields are required.");
            messageLabel.setStyle("-fx-text-fill: #e74c3c;"); // Error color
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        if (UserManager.registerUser(username, password)) {
            messageLabel.setText("Registration successful! Please login.");
            messageLabel.setStyle("-fx-text-fill: #27ae60;"); // Success color
            newUsernameField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
            // Optionally, navigate back to login after a delay or on button click
        } else {
            messageLabel.setText("Username already exists.");
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        try {
            Main.showLoginScreen();
        } catch (IOException e) {
            messageLabel.setText("Error loading login screen.");
            e.printStackTrace();
        }
    }
}