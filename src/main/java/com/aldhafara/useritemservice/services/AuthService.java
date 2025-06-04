package com.aldhafara.useritemservice.services;

import com.aldhafara.useritemservice.exceptions.InvalidCredentialsException;
import com.aldhafara.useritemservice.exceptions.LoginAlreadyInUseException;

public interface AuthService {
    String register(String login, String password) throws LoginAlreadyInUseException;

    String authenticate(String login, String rawPassword) throws InvalidCredentialsException;
}
