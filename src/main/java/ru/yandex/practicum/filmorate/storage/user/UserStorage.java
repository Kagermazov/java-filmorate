package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    long addUser(User newUser);

    void updateUser(User updatedUser);

    List<User> findAllUsers();
}
