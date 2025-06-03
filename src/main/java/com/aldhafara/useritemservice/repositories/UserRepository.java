package com.aldhafara.useritemservice.repositories;

import com.aldhafara.useritemservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID> {
}
