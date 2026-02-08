package org.example.model;

/**
 * represents a user of the quiz application
 * A user is identified by a username and has an assigned role as well as a highscore
 * user data is stored and loaded from JSON files
 */

public class User {
    /** Unique username of the user */
    private String username;
    /** role of the user (user or admin)*/
    private String role;
    /** current highscore*/
    private int highscore;

    // Leerer Konstruktor (wichtig für JSON!)
    public User() {
    }

    public User(String username, String role, int highscore) {
        /**
         * creates a new user with the given attributes
         */
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

    /**
     * updates the users highscore
     * @param highscore new highscore value
     */

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }
}

