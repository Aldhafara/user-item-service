package com.aldhafara.useritemservice.services;

import com.aldhafara.useritemservice.entities.Item;
import com.aldhafara.useritemservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {
    void createItemForUser(User user, String title);
    Page<Item> getItemsForUser(User user, Pageable pageable);
}
