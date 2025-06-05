package com.aldhafara.useritemservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Login cannot be blank")
        String login,
        @Size(min = 6, max = 50,
                message = "Password must be between 6 and 50 character")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Password must contain at least one lowercase letter, one uppercase letter and one number")
        @NotBlank(message = "Password cannot be blank")
        String password) {}
