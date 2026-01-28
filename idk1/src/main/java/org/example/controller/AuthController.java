package org.example.controller;

import org.example.model.User;
import org.example.persistence.UserRepository;

/**
 * Handles authentication use cases (e.g., login) and keeps GUI free of logic.
 */
public class AuthController {

    private final UserRepository userRepository = new UserRepository();

    /**
     * Checks whether a user exists. Returns the user object if found.
     *
     * @param username entered username
     * @return User if found, otherwise null
     */
    public User login(String username) {
        if (username == null) return null;
        String trimmed = username.trim();
        if (trimmed.isEmpty()) return null;

        return userRepository.findUser(trimmed);
    }
}

