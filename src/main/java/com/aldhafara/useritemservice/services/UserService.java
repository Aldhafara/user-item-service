package com.aldhafara.useritemservice.services;

import com.aldhafara.useritemservice.entities.User;
import com.aldhafara.useritemservice.exceptions.UserNotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(String login, String password);

    Optional<User> findById(UUID id);

    void deleteUser(UUID id);

    void addItemToUser(UUID userId, String itemName) throws UserNotFoundException;
}
