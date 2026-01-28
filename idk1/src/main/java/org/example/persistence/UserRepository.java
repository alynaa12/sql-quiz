package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.User;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class UserRepository {

    private static final String USER_FILE = "data/users.json";

    public String getUserHighscore(String username) {
        try {
            Gson gson = new Gson();
            Type userListType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(new FileReader(USER_FILE), userListType);

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
}

