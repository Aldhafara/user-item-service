package com.aldhafara.useritemservice.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String login;

    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Item> items = new ArrayList<>();

    public User() {
    }

    public User(String login) {
        this.login = login;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void addItem(Item item) {
        items.add(item);
        item.setOwner(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setOwner(null);
    }
}
