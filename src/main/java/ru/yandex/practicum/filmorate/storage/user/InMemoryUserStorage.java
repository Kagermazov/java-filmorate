package ru.yandex.practicum.filmorate.storage.user;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long counter;

    @Override
    public long addUser(@NonNull User newUser) {
        newUser.setId(getNextId());
        this.users.put(newUser.getId(), newUser);
        return newUser.getId();
    }

    @Override
    public void updateUser(@NonNull User updatedUser) {
        Long updatedUserId = updatedUser.getId();

        if (!this.users.containsKey(updatedUserId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no user with id " + updatedUserId);
        }

        this.users.replace(updatedUserId, updatedUser);
    }

    @Override
    public List<User> findAllUsers() {
        return this.users.values().stream().toList();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

    }

    @Override
    public Long countUsers() {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    public @Nullable User getUserById(long userId) {
        return this.users.get(userId);
    }

    public void deleteUsers() {
        this.users.clear();
    }

    private long getNextId() {
        return ++this.counter;
    }
}
