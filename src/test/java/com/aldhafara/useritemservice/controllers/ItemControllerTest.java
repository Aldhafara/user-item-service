package com.aldhafara.useritemservice.controllers;

import com.aldhafara.useritemservice.dto.CreateItemRequest;
import com.aldhafara.useritemservice.dto.ItemResponse;
import com.aldhafara.useritemservice.entities.Item;
import com.aldhafara.useritemservice.entities.User;
import com.aldhafara.useritemservice.services.ItemService;
import com.aldhafara.useritemservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemControllerTest {

    private final ItemService itemService = mock(ItemService.class);
    private final UserService userService = mock(UserService.class);
    private final ItemController controller = new ItemController(3, itemService, userService);

    private final User testUser = new User("login");
    private final UUID userId = UUID.randomUUID();
    private final Principal principal = userId::toString;

    @Test
    void shouldCreateItemAndReturnNoContent() {
        // given
        CreateItemRequest request = new CreateItemRequest("new item");
        when(userService.findById(userId)).thenReturn(Optional.of(testUser));

        // when
        ResponseEntity<Void> response = controller.createItem(request, principal);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(itemService).createItemForUser(testUser, "new item");
    }

    @Test
    void shouldReturnItemsForUser() {
        // given
        List<Item> items = List.of(
                new Item(testUser, "Item 1"),
                new Item(testUser, "Item 2")
        );
        when(userService.findById(userId)).thenReturn(Optional.of(testUser));
        when(itemService.getItemsForUser(eq(testUser), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(items));

        // when
        ResponseEntity<List<ItemResponse>> response = controller.getItems(principal, 0, 50);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ItemResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals("Item 1", body.get(0).name());
        assertEquals("Item 2", body.get(1).name());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // given
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // when / then
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> controller.getItems(principal, 0, 50));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getMessage().contains(userId.toString()));
    }

    @Test
    void shouldUseDefaultPageSizeWhenSizeNotProvided() {
        // given
        when(userService.findById(userId)).thenReturn(Optional.of(testUser));
        when(itemService.getItemsForUser(eq(testUser), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of()));

        // when
        ResponseEntity<List<ItemResponse>> response = controller.getItems(principal, 0, null);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(itemService).getItemsForUser(eq(testUser), pageRequestCaptor.capture());

        assertEquals(3, pageRequestCaptor.getValue().getPageSize());
    }
}
