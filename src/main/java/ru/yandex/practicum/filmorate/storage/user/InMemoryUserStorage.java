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
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void addUser(@NonNull User newUser) {
        newUser.setId(getNextId());
        this.users.put(newUser.getId(), newUser);
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
    public @Nullable User getUserById(long userId) {
        checkIfNegative(userId);
        return this.users.get(userId);
    }

    @Override
    public void deleteUsers() {
        this.users.clear();
    }

    private long getNextId() {
        long currentMaxId = this.users.values().stream()
                .mapToLong(user -> Optional.ofNullable(user.getId()).orElseThrow())
                .max()
                .orElse(0);
        return currentMaxId + 1;
    }

    private void checkIfNegative(long number) {
        if (number < 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "One of ids is negative");
        }
    }
}
