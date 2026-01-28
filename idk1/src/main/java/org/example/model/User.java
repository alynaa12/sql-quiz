package org.example.model;

public class User {
    private String username;
    private String role;
    private int highscore;

    // Leerer Konstruktor (wichtig für JSON!)
    public User() {
    }

    public User(String username, String role, int highscore) {
        this.username = username;
        this.role = role;
        this.highscore = highscore;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }
}

