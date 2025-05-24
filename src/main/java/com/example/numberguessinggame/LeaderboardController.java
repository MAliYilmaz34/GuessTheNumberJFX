package com.example.numberguessinggame;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class LeaderboardController {

    @FXML private TableView<LeaderboardEntry> overallTable;
    @FXML private TableColumn<LeaderboardEntry, Integer> overallRankColumn;
    @FXML private TableColumn<LeaderboardEntry, String> overallUsernameColumn;
    @FXML private TableColumn<LeaderboardEntry, Integer> overallScoreColumn;

    @FXML private TableView<LeaderboardEntry> easyTable;
    @FXML private TableColumn<LeaderboardEntry, Integer> easyRankColumn;
    @FXML private TableColumn<LeaderboardEntry, String> easyUsernameColumn;
    @FXML private TableColumn<LeaderboardEntry, Integer> easyScoreColumn;

    @FXML private TableView<LeaderboardEntry> mediumTable;
    @FXML private TableColumn<LeaderboardEntry, Integer> mediumRankColumn;
    @FXML private TableColumn<LeaderboardEntry, String> mediumUsernameColumn;
    @FXML private TableColumn<LeaderboardEntry, Integer> mediumScoreColumn;

    @FXML private TableView<LeaderboardEntry> hardTable;
    @FXML private TableColumn<LeaderboardEntry, Integer> hardRankColumn;
    @FXML private TableColumn<LeaderboardEntry, String> hardUsernameColumn;
    @FXML private TableColumn<LeaderboardEntry, Integer> hardScoreColumn;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        setupTable(overallTable, overallRankColumn, overallUsernameColumn, overallScoreColumn);
        setupTable(easyTable, easyRankColumn, easyUsernameColumn, easyScoreColumn);
        setupTable(mediumTable, mediumRankColumn, mediumUsernameColumn, mediumScoreColumn);
        setupTable(hardTable, hardRankColumn, hardUsernameColumn, hardScoreColumn);
        updateLeaderboard();
    }

    private void setupTable(
            TableView<LeaderboardEntry> table,
            TableColumn<LeaderboardEntry, Integer> rankColumn,
            TableColumn<LeaderboardEntry, String> usernameColumn,
            TableColumn<LeaderboardEntry, Integer> scoreColumn) {
        
        rankColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRank()).asObject());
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        scoreColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getScore()).asObject());
    }

    public void updateLeaderboard() {
        User[] users = UserManager.getLeaderboard();

        // Overall Leaderboard
        LeaderboardEntry[] overallEntries = Arrays.stream(users)
            .filter(user -> user.getHighScore() > 0)
            .sorted(Comparator.comparingInt(User::getHighScore).reversed())
            .map(user -> new LeaderboardEntry(0, user.getUsername(), user.getHighScore()))
            .toArray(LeaderboardEntry[]::new);
        
        // Easy Mode Leaderboard
        LeaderboardEntry[] easyEntries = Arrays.stream(users)
            .filter(user -> user.getEasyHighScore() > 0)
            .sorted(Comparator.comparingInt(User::getEasyHighScore).reversed())
            .map(user -> new LeaderboardEntry(0, user.getUsername(), user.getEasyHighScore()))
            .toArray(LeaderboardEntry[]::new);

        // Medium Mode Leaderboard
        LeaderboardEntry[] mediumEntries = Arrays.stream(users)
            .filter(user -> user.getMediumHighScore() > 0)
            .sorted(Comparator.comparingInt(User::getMediumHighScore).reversed())
            .map(user -> new LeaderboardEntry(0, user.getUsername(), user.getMediumHighScore()))
            .toArray(LeaderboardEntry[]::new);

        // Hard Mode Leaderboard
        LeaderboardEntry[] hardEntries = Arrays.stream(users)
            .filter(user -> user.getHardHighScore() > 0)
            .sorted(Comparator.comparingInt(User::getHardHighScore).reversed())
            .map(user -> new LeaderboardEntry(0, user.getUsername(), user.getHardHighScore()))
            .toArray(LeaderboardEntry[]::new);

        // Add ranks
        addRanks(overallEntries);
        addRanks(easyEntries);
        addRanks(mediumEntries);
        addRanks(hardEntries);

        // Update tables
        overallTable.setItems(FXCollections.observableArrayList(overallEntries));
        easyTable.setItems(FXCollections.observableArrayList(easyEntries));
        mediumTable.setItems(FXCollections.observableArrayList(mediumEntries));
        hardTable.setItems(FXCollections.observableArrayList(hardEntries));
    }

    private void addRanks(LeaderboardEntry[] entries) {
        for (int i = 0; i < entries.length; i++) {
            entries[i].setRank(i + 1);
        }
    }

    @FXML
    void handleBackButtonAction() {
        try {
            Main.showMainMenu(currentUser);  // Pass the current user back to the main menu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to represent leaderboard entries
    public static class LeaderboardEntry {
        private int rank;
        private final String username;
        private final int score;

        public LeaderboardEntry(int rank, String username, int score) {
            this.rank = rank;
            this.username = username;
            this.score = score;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getRank() { return rank; }
        public String getUsername() { return username; }
        public int getScore() { return score; }
    }
} 