package com.aldhafara.useritemservice.repositories;

import com.aldhafara.useritemservice.entities.Item;
import com.aldhafara.useritemservice.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new User("test_login"));
    }

    @Test
    void shouldSaveAndLoadItem() {
        Item item = new Item(owner,"test_item");

        Item saved = itemRepository.save(item);

        Optional<Item> loaded = itemRepository.findById(saved.getId());
        assertTrue(loaded.isPresent());
        assertEquals("test_login", loaded.get().getOwner().getLogin());
        assertEquals("test_item", loaded.get().getName());
    }

    @Test
    void shouldFindItemsByOwnerId() {
        Item item1 = new Item(owner, "item1");
        Item item2 = new Item(owner, "item2");

        itemRepository.saveAll(List.of(item1, item2));

        List<Item> found = itemRepository.findByOwnerId(owner.getId());
        assertEquals(2, found.size());
    }

}
