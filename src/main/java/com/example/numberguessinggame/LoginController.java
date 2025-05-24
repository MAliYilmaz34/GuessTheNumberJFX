package com.example.numberguessinggame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        User user = UserManager.authenticateUser(username, password);
        if (user != null) {
            errorLabel.setText("");
            System.out.println("Login successful for: " + user.getUsername());
            try {
                Main.showMainMenu(user);
            } catch (IOException e) {
                errorLabel.setText("Error loading main menu.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    void handleRegisterLinkAction(ActionEvent event) {
        try {
            Main.showRegisterScreen();
        } catch (IOException e) {
            errorLabel.setText("Error loading registration screen.");
            e.printStackTrace();
        }
    }
}