package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    private final Map<Integer, User> users;

    public UserController() {
        this.users = new HashMap<>();
    }

    @Override
    @PostMapping
    public final User create(@Valid @RequestBody final User newUser) {

        this.validate(newUser);

        Optional<String> newUserName = Optional.ofNullable(newUser.getName());
        String newUserLogin = newUser.getLogin();

        if (newUserName.isEmpty() || newUserName.get().isBlank()) {
            newUser.setName(newUserLogin);
        }

        newUser.setId(getNextId(this.users));

        int newUserId = newUser.getId();

        Object puttingResult = this.users.putIfAbsent(newUserId, newUser);

        if (puttingResult != null) {
            throw new ValidationException("Such user exists already");
        }

        log.info("The user with id {} was created", newUserId);
        return newUser;
    }

    @Override
    @PutMapping
    public final User update(@Valid @RequestBody final User updatedUser) {
        int updatedUserId = updatedUser.getId();
        String newUserName = updatedUser.getName();
        String newUserLogin = updatedUser.getLogin();

        this.validate(updatedUser);

        if (newUserName.isEmpty() || newUserName.isBlank()) {
            updatedUser.setName(newUserLogin);
        }

        Object puttingResult = this.users.putIfAbsent(updatedUserId, updatedUser);

        if (puttingResult == null) {
            throw new ValidationException("Such user doesn't exist");
        }

        log.info("The user with id {} was updated", updatedUserId);
        return updatedUser;
    }

    @GetMapping
    public final List<User> getUsers() {
        return findAll(this.users);
    }

    @Override
    public final void validate(final User userToCheck) {

        Optional<String> newUserLogin = Optional.ofNullable(userToCheck.getLogin());
        Optional<String> newUserEmail = Optional.ofNullable(userToCheck.getEmail());

        if (newUserEmail.isEmpty() || newUserEmail.get().isBlank()) {
            log.warn("The new user doesn't have an email");
            throw new ValidationException("An user email doesn't exist");
        }

        if (newUserLogin.isEmpty() || newUserLogin.get().isBlank()) {
            log.warn("The new user doesn't have a login");
            throw new ValidationException("The new user doesn't have a login");
        }

        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The new user`s birthday is in the future");
            throw new ValidationException("Birthday is wrong");
        }
    }
}
