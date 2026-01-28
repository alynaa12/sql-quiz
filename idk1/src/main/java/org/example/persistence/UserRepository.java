package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Handles persistence of users stored in data/users.json.
 */
public class UserRepository {

    private static final String USER_FILE = "data/users.json";

    /**
     * Returns the highscore of a given user as String.
     *
     * @param username username to search for
     * @return highscore as string, or error message
     */
    public String getUserHighscore(String username) {
        try {
            Gson gson = new Gson();
            Type userListType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(new FileReader(USER_FILE), userListType);

            if (users == null) return "User not found";

            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return String.valueOf(user.getHighscore());
                }
            }
        } catch (Exception e) {
            return "Error reading users";
        }

        return "User not found";
    }

    /**
     * Increases the highscore of a user by a given amount and saves to file.
     *
     * @param username username to update
     * @param amount amount to add
     */
    public void increaseHighscore(String username, int amount) {
        try {
            Gson gson = new Gson();
            Type userListType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(new FileReader(USER_FILE), userListType);

            if (users == null) return;

            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    user.setHighscore(user.getHighscore() + amount);
                }
            }

            FileWriter writer = new FileWriter(USER_FILE);
            gson.toJson(users, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not update highscore", e);
        }
    }

    /**
     * Finds a user by username (case-insensitive).
     * Used for login.
     *
     * @param username entered username
     * @return user if found, otherwise null
     */
    public User findUser(String username) {
        try {
            Gson gson = new Gson();
            Type userListType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(new FileReader(USER_FILE), userListType);

            if (users == null) return null;

            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(username)) {
                    return user;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Could not read users.json", e);
        }
    }
}


