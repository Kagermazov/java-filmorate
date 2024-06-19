package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest {
    User testUser;
    User testUser2;
    UserStorage storage;

    @BeforeEach
    void setUp() {
        this.testUser = new User(1L, "email@.ru", "login", "name", LocalDate.EPOCH, null);
        this.testUser2 = new User(2L, "email@.ru", "login", "name", LocalDate.EPOCH, null);
        this.storage = new InMemoryUserStorage();
    }

    @DisplayName("addUser_method shouldn't thrown an exception when a new user added")
    @Test
    void addUser_methodShouldNotThrowExceptionWhenNewUserAdded() {
        this.storage.addUser(testUser);
        assertDoesNotThrow(() -> this.storage.getUserById(1L));
    }

    @DisplayName("addUser_method should throw an exception when user with the same id is added")
    @Test
    void addUser() {
        this.storage.addUser(this.testUser);
        assertThrows(ValidationException.class, () -> this.storage.addUser(this.testUser));
    }

    @DisplayName("findAllUsers method should return a list")
    @Test
    void findAllUsers_methodShouldReturnList() {
        this.storage.addUser(this.testUser);
        this.storage.addUser(this.testUser2);
        assertEquals(2, this.storage.findAllUsers().size());
    }

    @Test
    void getUserById() {

    }

    @Test
    void deleteUsers() {
    }
}