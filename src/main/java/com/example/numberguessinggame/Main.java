package com.example.numberguessinggame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    private static Stage primaryStage;
    private static final double WINDOW_WIDTH = 620;
    private static final double WINDOW_HEIGHT = 580;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setResizable(false);  // Lock window size
        
        // Add application icon
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("image.png")));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }
        
        UserManager.loadUsers();
        showLoginScreen();
    }

    public static void showLoginScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = createStyledScene(loader.load());
        primaryStage.setTitle("Number Guesser - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showRegisterScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("register.fxml"));
        Scene scene = createStyledScene(loader.load());
        primaryStage.setTitle("Number Guesser - Register");
        primaryStage.setScene(scene);
    }

    public static void showMainMenu(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainMenu.fxml"));
        Scene scene = createStyledScene(loader.load());
        
        MainMenuController controller = loader.getController();
        controller.setCurrentUser(user);

        primaryStage.setTitle("Number Guesser - Main Menu");
        primaryStage.setScene(scene);
    }

    public static void showGameScreen(User user, int maxNumber) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("game.fxml"));
        Scene scene = createStyledScene(loader.load());

        GameController controller = loader.getController();
        controller.initializeGame(user, maxNumber);

        primaryStage.setTitle("Number Guesser - Game On!");
        primaryStage.setScene(scene);
    }

    public static void showLeaderboard(User currentUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("leaderboard.fxml"));
        Scene scene = createStyledScene(loader.load());

        LeaderboardController controller = loader.getController();
        controller.setCurrentUser(currentUser);

        primaryStage.setTitle("Number Guesser - Leaderboard");
        primaryStage.setScene(scene);
    }

    private static Scene createStyledScene(Parent root) {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        URL css = Main.class.getResource("styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}