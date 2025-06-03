package com.aldhafara.useritemservice.repositories;

import com.aldhafara.useritemservice.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndLoadUser() {
        User user = new User("test_login");
        user.setEncryptedPassword("encrypted123");

        User saved = userRepository.save(user);

        Optional<User> loaded = userRepository.findById(saved.getId());
        assertTrue(loaded.isPresent());
        assertEquals("test_login", loaded.get().getLogin());
        assertEquals("encrypted123", loaded.get().getPassword());
    }
}
