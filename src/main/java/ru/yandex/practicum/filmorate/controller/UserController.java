package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    private final List<User> users = new ArrayList<>();

    @Override
    @PostMapping
    public User create(@Valid @RequestBody User newUser) {

        super.validator.validate(newUser);

        Optional<String> newUserName = Optional.ofNullable(newUser.getName());
        String newUserLogin = newUser.getLogin();

        if (newUserName.isEmpty() || newUserName.get().isBlank()) {
            newUser.setName(newUserLogin);
        }

        newUser.setId(getNextId(this.users));

        int newUserId = newUser.getId();

        this.users.add(newUser);
        log.info("The user with id {} was created", newUserId);
        return newUser;
    }

    @Override
    @PutMapping
    public User update(@Valid @RequestBody User updatedUser) {
        int updatedUserId = updatedUser.getId();

        if (isObjectInStorage(updatedUser, this.users)) {
            super.validator.validate(updatedUser);

            String newUserName = updatedUser.getName();
            String newUserLogin = updatedUser.getLogin();

            if (newUserName.isEmpty() || newUserName.isBlank()) {
                updatedUser.setName(newUserLogin);
            }

            this.users.add(updatedUser);
            log.info("The user with id {} was updated", updatedUserId);
        } else {
            throw new ValidationException("Such user doesn`t exist");
        }

        return updatedUser;
    }

    @Override
    @GetMapping
    public List<User> findAll(@RequestParam(value = "storage", required = false) List<User> storage) {
        return this.users;
    }
}
