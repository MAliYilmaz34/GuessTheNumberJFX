package com.example.numberguessinggame;

public class User {
    private String username;
    private String password; // No encryption as requested
    private int highScore;
    private int gamesPlayed;
    private int easyHighScore;
    private int mediumHighScore;
    private int hardHighScore;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.highScore = 0;
        this.gamesPlayed = 0;
        this.easyHighScore = 0;
        this.mediumHighScore = 0;
        this.hardHighScore = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getEasyHighScore() {
        return easyHighScore;
    }

    public int getMediumHighScore() {
        return mediumHighScore;
    }

    public int getHardHighScore() {
        return hardHighScore;
    }

    public void updateScore(int attempts, int maxNumber) {
        gamesPlayed++;
        int score;
        
        if (maxNumber == 10) { // Easy mode
            score = 100 - (attempts * 10);
            if (score > easyHighScore) {
                easyHighScore = score;
            }
        } else if (maxNumber == 100) { // Medium mode
            score = 500 - (attempts * 15);
            if (score > mediumHighScore) {
                mediumHighScore = score;
            }
        } else { // Hard mode
            score = 1000 - (attempts * 20);
            if (score > hardHighScore) {
                hardHighScore = score;
            }
        }

        // Update overall high score if needed
        if (score > highScore) {
            highScore = score;
        }
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setEasyHighScore(int score) {
        this.easyHighScore = score;
    }

    public void setMediumHighScore(int score) {
        this.mediumHighScore = score;
    }

    public void setHardHighScore(int score) {
        this.hardHighScore = score;
    }

    // For saving to JSON, ensure getters match field names if using a library like Jackson
    // or implement simple toString/fromString for manual saving.
}