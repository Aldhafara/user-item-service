package com.aldhafara.useritemservice.repositories;

import com.aldhafara.useritemservice.entities.Item;
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

    @Autowired
    private ItemRepository itemRepository;

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

    @Test
    void deletingUserShouldAlsoDeleteTheirItems() {
        User user = new User("test_login");

        Item item1 = new Item(user, "item1");
        Item item2 = new Item(user, "item2");

        user.addItem(item1);
        user.addItem(item2);

        user = userRepository.save(user);
        assertEquals(2, itemRepository.findAll().size());

        userRepository.delete(user);

        assertEquals(0, itemRepository.findAll().size());
    }

    @Test
    void shouldFindUserByLogin() {
        User user = new User("test_login");
        user.setEncryptedPassword("secret");
        userRepository.save(user);

        Optional<User> found = userRepository.findByLogin("test_login");

        assertTrue(found.isPresent());
        assertEquals("test_login", found.get().getLogin());
        assertEquals("secret", found.get().getPassword());
    }

    @Test
    void shouldReturnEmptyOptionalWhenLoginNotFound() {
        Optional<User> found = userRepository.findByLogin("nonexistent_login");

        assertTrue(found.isEmpty());
    }
}
