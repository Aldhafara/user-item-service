package com.aldhafara.useritemservice.services;

import com.aldhafara.useritemservice.entities.Item;
import com.aldhafara.useritemservice.entities.User;
import com.aldhafara.useritemservice.repositories.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void createItemForUser(User user, String title) {
        Item item = new Item(user, title);
        itemRepository.save(item);
    }

    @Override
    public Page<Item> getItemsForUser(User user, Pageable pageable) {
        return itemRepository.findByOwnerId(user.getId(), pageable);
    }
}
