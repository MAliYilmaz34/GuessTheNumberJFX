package com.example.numberguessinggame;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String USERS_FILE_PATH = "users.json";
    private static Map<String, User> users = new HashMap<>();

    public static void loadUsers() {
        try {
            if (Files.exists(Paths.get(USERS_FILE_PATH))) {
                String content = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
                if (!content.isEmpty()) {
                    JSONArray usersArray = new JSONArray(content);
                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userJson = usersArray.getJSONObject(i);
                        User user = new User(userJson.getString("username"), userJson.getString("password"));
                        if (userJson.has("highScore")) {
                            user.setHighScore(userJson.getInt("highScore"));
                        }
                        if (userJson.has("gamesPlayed")) {
                            user.setGamesPlayed(userJson.getInt("gamesPlayed"));
                        }
                        if (userJson.has("easyHighScore")) {
                            user.setEasyHighScore(userJson.getInt("easyHighScore"));
                        }
                        if (userJson.has("mediumHighScore")) {
                            user.setMediumHighScore(userJson.getInt("mediumHighScore"));
                        }
                        if (userJson.has("hardHighScore")) {
                            user.setHardHighScore(userJson.getInt("hardHighScore"));
                        }
                        users.put(user.getUsername(), user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            // Initialize with an empty file if it's corrupted or first run after deletion
            saveUsers();
        } catch (org.json.JSONException e) {
            System.err.println("Error parsing users.json: " + e.getMessage() + ". The file might be corrupted or not a valid JSON array.");
            // Optionally, backup the corrupted file and create a new one
            try {
                Files.move(Paths.get(USERS_FILE_PATH), Paths.get(USERS_FILE_PATH + ".corrupted"));
            } catch (IOException ex) {
                System.err.println("Could not backup corrupted users file: " + ex.getMessage());
            }
            users.clear(); // Start with a fresh user map
            saveUsers();   // Create a new empty users file
        }
    }


    public static void saveUsers() {
        JSONArray usersArray = new JSONArray();
        for (User user : users.values()) {
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("password", user.getPassword());
            userJson.put("highScore", user.getHighScore());
            userJson.put("gamesPlayed", user.getGamesPlayed());
            userJson.put("easyHighScore", user.getEasyHighScore());
            userJson.put("mediumHighScore", user.getMediumHighScore());
            userJson.put("hardHighScore", user.getHardHighScore());
            usersArray.put(userJson);
        }
        try (FileWriter file = new FileWriter(USERS_FILE_PATH)) {
            file.write(usersArray.toString(4));
            file.flush();
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        users.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public static User authenticateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Authentication failed
    }

    public static User[] getLeaderboard() {
        return users.values().stream()
                .filter(user -> user.getHighScore() > 0)
                .sorted((u1, u2) -> Integer.compare(u2.getHighScore(), u1.getHighScore()))
                .toArray(User[]::new);
    }
}