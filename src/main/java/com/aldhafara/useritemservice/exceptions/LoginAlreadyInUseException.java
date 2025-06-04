package com.aldhafara.useritemservice.exceptions;

public class LoginAlreadyInUseException extends RuntimeException {
    public LoginAlreadyInUseException() {
        super("Login already in use");
    }
}
