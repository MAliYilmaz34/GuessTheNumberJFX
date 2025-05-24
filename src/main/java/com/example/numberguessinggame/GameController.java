package com.example.numberguessinggame;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class GameController {

    @FXML private StackPane gameRootPane;
    @FXML private Rectangle backgroundRect;
    @FXML private TextField guessField;
    @FXML private Text feedbackLabel;
    @FXML private Text animatedGuessText;
    @FXML private ListView<Integer> guessesListView;
    @FXML private Button newGameButton;
    @FXML private Button guessButton;
    @FXML private Text currentScoreText;
    @FXML private Text difficultyText;

    private User currentUser;
    private int maxNumber;
    private int numberToGuess;
    private int attempts;
    private ObservableList<Integer> guessedNumbers;
    private int currentScore;
    private int baseScore;
    private int penaltyPerGuess;

    private MediaPlayer winPlayer;
    private MediaPlayer hintPlayer;

    public void initializeGame(User user, int maxNumber) {
        this.currentUser = user;
        this.maxNumber = maxNumber;
        this.guessedNumbers = FXCollections.observableArrayList();
        guessesListView.setItems(guessedNumbers);

        // Set base score and penalty based on difficulty
        if (maxNumber == 10) {
            baseScore = 100;
            penaltyPerGuess = 10;
            difficultyText.setText("Difficulty: Easy");
        } else if (maxNumber == 100) {
            baseScore = 500;
            penaltyPerGuess = 15;
            difficultyText.setText("Difficulty: Medium");
        } else {
            baseScore = 1000;
            penaltyPerGuess = 20;
            difficultyText.setText("Difficulty: Hard");
        }

        currentScore = baseScore;
        currentScoreText.setText("Current Score: " + currentScore);

        loadSounds();
        startNewRound();
    }

    private void loadSounds() {
        try {
            URL winResource = getClass().getResource("win.wav");
            URL hintResource = getClass().getResource("hint.mp3");

            if (winResource != null) {
                winPlayer = new MediaPlayer(new Media(winResource.toString()));
            }
            if (hintResource != null) {
                hintPlayer = new MediaPlayer(new Media(hintResource.toString()));
            }
        } catch (Exception e) {
            // Sound is not critical for gameplay, so just log the error
            System.err.println("Error loading game sounds: " + e.getMessage());
        }
    }

    private void playSound(MediaPlayer player) {
        if (player != null) {
            player.stop();
            player.play();
        }
    }

    private void startNewRound() {
        Random random = new Random();
        numberToGuess = random.nextInt(maxNumber) + 1;
        attempts = 0;
        currentScore = baseScore;
        
        guessedNumbers.clear();
        guessField.clear();
        guessField.setDisable(false);
        guessButton.setDisable(false);
        newGameButton.setVisible(false);
        
        feedbackLabel.setText("Guess a number between 1 and " + maxNumber);
        currentScoreText.setText("Current Score: " + currentScore);
        animatedGuessText.setText("");
        animatedGuessText.setOpacity(0);
        backgroundRect.setFill(Color.web("#f0f4f8"));
    }

    @FXML
    void handleGuessButtonAction(ActionEvent event) {
        String guessText = guessField.getText();
        int guess;
        
        try {
            guess = Integer.parseInt(guessText);
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Please enter a valid number.");
            feedbackLabel.setFill(Color.RED);
            return;
        }

        if (guess < 1 || guess > maxNumber) {
            feedbackLabel.setText("Please enter a number between 1 and " + maxNumber);
            feedbackLabel.setFill(Color.RED);
            return;
        }

        attempts++;
        updateCurrentScore();
        guessedNumbers.add(guess);
        guessField.clear();

        animateGuessedNumber(guess, () -> processGuess(guess));
    }

    private void updateCurrentScore() {
        currentScore = baseScore - (attempts * penaltyPerGuess);
        currentScoreText.setText("Current Score: " + currentScore);
    }

    private void animateGuessedNumber(int number, Runnable onFinished) {
        animatedGuessText.setText(String.valueOf(number));
        animatedGuessText.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), animatedGuessText);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(e -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });
        fadeIn.play();
    }

    private void processGuess(int guess) {
        if (guess == numberToGuess) {
            int finalScore = baseScore - (attempts * penaltyPerGuess);
            feedbackLabel.setText(String.format("Congratulations, %s! You won %d points in %d tries!", 
                currentUser.getUsername(), finalScore, attempts));
            feedbackLabel.setFill(Color.GREEN);
            backgroundRect.setFill(Color.LIGHTGREEN);
            playSound(winPlayer);
            
            newGameButton.setVisible(true);
            guessField.setDisable(true);
            guessButton.setDisable(true);
            
            currentUser.updateScore(attempts, maxNumber);
            UserManager.saveUsers();
        } else {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(800), animatedGuessText);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setDelay(Duration.millis(500));

            if (guess < numberToGuess) {
                feedbackLabel.setText("Too Low! Try again.");
            } else {
                feedbackLabel.setText("Too High! Try again.");
            }
            feedbackLabel.setFill(Color.web("#d35400"));
            playSound(hintPlayer);
            fadeOut.play();
        }
    }

    @FXML
    void handleNewGameButtonAction(ActionEvent event) {
        startNewRound();
    }

    @FXML
    void handleMenuButtonAction(ActionEvent event) {
        try {
            if (winPlayer != null) winPlayer.stop();
            if (hintPlayer != null) hintPlayer.stop();
            Main.showMainMenu(currentUser);
        } catch (IOException e) {
            System.err.println("Error returning to menu: " + e.getMessage());
        }
    }
}