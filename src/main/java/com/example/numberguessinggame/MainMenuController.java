package com.example.numberguessinggame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Text welcomeText;
    @FXML
    private RadioButton easyRadio;
    @FXML
    private RadioButton mediumRadio;
    @FXML
    private RadioButton hardRadio;
    @FXML
    private Button playButton;

    private ToggleGroup difficultyGroup;
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            welcomeText.setText("Welcome, " + user.getUsername() + "!");
        }
    }

    @FXML
    public void initialize() {
        difficultyGroup = new ToggleGroup();
        easyRadio.setToggleGroup(difficultyGroup);
        mediumRadio.setToggleGroup(difficultyGroup);
        hardRadio.setToggleGroup(difficultyGroup);
        easyRadio.setUserData(10);
        mediumRadio.setUserData(100);
        hardRadio.setUserData(1000);
        mediumRadio.setSelected(true); // Default selection
    }

    @FXML
    void handlePlayButtonAction(ActionEvent event) {
        RadioButton selectedRadio = (RadioButton) difficultyGroup.getSelectedToggle();
        if (selectedRadio == null) {
            // Should not happen if a default is selected, but good practice
            System.err.println("No difficulty selected!"); // Or show an alert
            return;
        }
        int maxNumber = (int) selectedRadio.getUserData();
        System.out.println("Starting game for " + currentUser.getUsername() + " with max number: " + maxNumber);
        try {
            Main.showGameScreen(currentUser, maxNumber);
        } catch (IOException e) {
            e.printStackTrace();
            // Show error to user
        }
    }
    @FXML
    void handleLogoutButtonAction(ActionEvent event) {
        currentUser = null; // Clear current user
        try {
            Main.showLoginScreen();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error (e.g., show an alert)
        }
    }

    @FXML
    void handleLeaderboardButtonAction(ActionEvent event) {
        try {
            Main.showLeaderboard(currentUser);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error (e.g., show an alert)
        }
    }
}