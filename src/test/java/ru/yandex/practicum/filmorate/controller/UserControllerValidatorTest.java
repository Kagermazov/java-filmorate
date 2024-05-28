package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerValidatorTest {

    UserController controller;
    
    @BeforeEach
    public void beforeEach() {
        this.controller = new UserController();
    }

    @DisplayName("Should throw an exception if an user's email is empty or blank")
    @Test
    void shouldThrowExceptionIfUserEmailDoesNotExist() {
        User testUser = new User(-1, "", "login", "Name", LocalDate.EPOCH);
        User testUser2 = new User(-1, " ", "login", "Name", LocalDate.EPOCH);

        assertThrows(ValidationException.class, () -> controller.validate(testUser));
        assertThrows(ValidationException.class, () -> controller.validate(testUser2));
    }

    @DisplayName("Shouldn't throw an exception if an user's email is present")
    @Test
    void shouldNoThrowExceptionIfUserEmailIsPresent() {
        User testUser = new User(-1, "Email", "Login", "Name", LocalDate.EPOCH);

        assertDoesNotThrow(() -> controller.validate(testUser));
    }

    @DisplayName("Should throw an exception if an user login is absent")
    @Test
    void shouldThrowExceptionIfUserLoginIsAbsent() {
        User testUser = new User(-1, "Email", "", "Name", LocalDate.EPOCH);
        User testUser2 = new User(-1, "Email", " ", "Name", LocalDate.EPOCH);

        assertThrows(ValidationException.class, () -> controller.validate(testUser));
        assertThrows(ValidationException.class, () -> controller.validate(testUser2));
    }

    @DisplayName("Shouldn't throw an exception if an user login is present")
    @Test
    void shouldNotThrowExceptionIfUserLoginIsPresent() {
        User testUser = new User(-1, "Email", "Login", "Name", LocalDate.EPOCH);

        assertDoesNotThrow(() -> controller.validate(testUser));
    }

    @DisplayName("Should throw an exception if an user's birthday is in the future")
    @Test
    void shouldNotThrowExceptionIfUserBirthdayIsInFuture() {
        User testUser = new User(-1, "Email", "Login", "Name", LocalDate.MAX);

        assertThrows(ValidationException.class, () -> controller.validate(testUser));
    }

    @DisplayName("Shouldn't throw an exception if an user's birthday is correct")
    @Test
    void shouldNotThrowExceptionIfUserBirthdayIsCorrect() {
        User testUser = new User(-1, "Email", "Login", "Name", LocalDate.EPOCH);

        assertDoesNotThrow(() -> controller.validate(testUser));
    }
}
