package com.aldhafara.useritemservice.services;

import com.aldhafara.useritemservice.entities.Item;
import com.aldhafara.useritemservice.entities.User;
import com.aldhafara.useritemservice.repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void shouldCreateItemForUser() {
        // given
        User user = new User("user");
        String itemName = "My test item";

        // when
        itemService.createItemForUser(user, itemName);

        // then
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemCaptor.capture());

        Item savedItem = itemCaptor.getValue();
        assertEquals(itemName, savedItem.getName());
        assertEquals(user, savedItem.getOwner());
    }

    @Test
    void shouldReturnItemsForUser() {
        // given
        User user = new User("user");
        Pageable pageable = PageRequest.of(0, 10);
        List<Item> itemList = List.of(new Item(user, "Item A"), new Item(user, "Item B"));
        Page<Item> expectedPage = new PageImpl<>(itemList);

        when(itemRepository.findByOwnerId(user.getId(), pageable)).thenReturn(expectedPage);

        // when
        Page<Item> result = itemService.getItemsForUser(user, pageable);

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals("Item A", result.getContent().get(0).getName());
        assertEquals("Item B", result.getContent().get(1).getName());
        verify(itemRepository).findByOwnerId(user.getId(), pageable);
    }
}
