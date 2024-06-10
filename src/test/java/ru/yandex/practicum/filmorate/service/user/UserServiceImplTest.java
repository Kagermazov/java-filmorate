package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    UserService testService;
    User testUser;

    @BeforeEach
    void beforeEach() {
        this.testService = new UserServiceImpl(new InMemoryUserStorage());
        this.testUser = new User(1L, "email@.ru", "login", "name", LocalDate.EPOCH, null);
    }

    @DisplayName("The addFriend method should save ids of friends in their fields")
    @Test
    void addFriend() {
        User testUser2 = new User(2L, "email@.ru", "login", "name", LocalDate.EPOCH, null);

        testService.addUser(testUser);
        testService.addUser(testUser2);
        testService.addFriend(testUser.getId(), testUser2.getId());

        assertEquals(1, testUser.getFriends().size());
        assertEquals(1, testUser2.getFriends().size());
        assertTrue(testUser.getFriends().contains(testUser2.getId()));
        assertTrue(testUser2.getFriends().contains(testUser.getId()));
    }

    @DisplayName("findAllUserFriends method should return a list with long values")
    @Test
    void findAllUsers() {
        User testUser = new User(1L, "email@.ru", "login", "name", LocalDate.EPOCH,
                Set.of(2L));
        User testUser2 = new User(2L, "email@.ru", "login", "name", LocalDate.EPOCH,
                Set.of(1L));

        this.testService.addUser(testUser);
        this.testService.addUser(testUser2);

        assertEquals(List.of(testUser2), this.testService.findAllUserFriends(1L));
        assertEquals(List.of(testUser), this.testService.findAllUserFriends(2L));
    }

    @Test
    void unfriend() {
        this.testService.addUser(this.testUser);

        assertThrows(ValidationException.class, () -> this.testService.unfriend(1L, 2L));
    }
}