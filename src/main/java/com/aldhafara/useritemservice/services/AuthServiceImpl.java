package com.aldhafara.useritemservice.services;

import com.aldhafara.useritemservice.entities.User;
import com.aldhafara.useritemservice.exceptions.InvalidCredentialsException;
import com.aldhafara.useritemservice.exceptions.LoginAlreadyInUseException;
import com.aldhafara.useritemservice.repositories.UserRepository;
import com.aldhafara.useritemservice.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String register(String login, String password) throws LoginAlreadyInUseException {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new LoginAlreadyInUseException();
        }

        User user = new User(login);
        user.setEncryptedPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return jwtTokenProvider.generateToken(user.getId());
    }

    @Override
    public String authenticate(String login, String rawPassword) throws InvalidCredentialsException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(InvalidCredentialsException::new);

        boolean isPasswordValid = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!isPasswordValid) {
            throw new InvalidCredentialsException();
        }

        return jwtTokenProvider.generateToken(user.getId());
    }
}
