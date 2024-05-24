package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User>{

    private final Map<Integer, User> users = new HashMap<>();
    
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

        this.users.put(newUserId, newUser);
        log.info("The user with id {} was created", newUserId);
        return newUser;
    }

    @Override
    @PutMapping
    public User update(@Valid @RequestBody User updatedUser) {
        int updatedUserId = updatedUser.getId();

        if (this.users.containsKey(updatedUserId)) {
            super.validator.validate(updatedUser);

            String newUserName = updatedUser.getName();
            String newUserLogin = updatedUser.getLogin();

            if (newUserName.isEmpty() || newUserName.isBlank()) {
                updatedUser.setName(newUserLogin);
            }

            this.users.put(updatedUserId, updatedUser);
            log.info("The user with id {} was updated", updatedUserId);
        } else {
            throw new ValidationException("Such user doesn`t exist");
        }

        return updatedUser;
    }

    @Override
    @GetMapping
    public Map<Integer, User> findAll() {
        return this.users;
    }
}
