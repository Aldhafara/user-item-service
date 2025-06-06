package com.aldhafara.useritemservice.repositories;

import com.aldhafara.useritemservice.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByOwnerId(UUID ownerId);
    Page<Item> findByOwnerId(UUID ownerId, Pageable pageable);
}
