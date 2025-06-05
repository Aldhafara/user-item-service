package com.aldhafara.useritemservice.controllers;

import com.aldhafara.useritemservice.dto.AuthRequest;
import com.aldhafara.useritemservice.dto.AuthResponse;
import com.aldhafara.useritemservice.exceptions.InvalidCredentialsException;
import com.aldhafara.useritemservice.exceptions.LoginAlreadyInUseException;
import com.aldhafara.useritemservice.services.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@Tag(name = "Authentication")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register to the platform",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Registering successful"
                    ),
                    @ApiResponse(responseCode = "409",
                            description = "Login already in use"
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid login or password"
                    )
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequest request) {
        try {
            authService.register(request.login(), request.password());
            return ResponseEntity.noContent().build();
        } catch (LoginAlreadyInUseException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @Operation(summary = "Authenticate with the platform",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = AuthResponse.class,
                                            example = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJyb2xlIjoiU3R1ZGVudCJ9.IxBkuQHrrwJrc8_IA5DPdGhBKx43iYsricXKXUQt_8o\" }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Invalid credentials"
                    )
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            String token = authService.authenticate(request.login(), request.password());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (InvalidCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
