package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
public class ControllerValidator<T> {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final String CINEMA_INVENTION_DATE = "1895-12-28";

    public void validate(T newObject) {

        Class<?> newObjectClass = newObject.getClass();

        if (newObjectClass == Film.class) {

            Film newFilm = (Film) newObject;
            Optional<String> newFilmName = Optional.ofNullable(newFilm.getName());

            if (newFilmName.isEmpty() || newFilmName.get().isBlank()) {
                log.warn("A film name absents");
                throw new ValidationException("A film name absents");
            }

            if (newFilm.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
                log.warn("A description length is more than 200 symbols");
                throw new ValidationException("A description length is more than 200 symbols");
            }

            if (newFilm.getReleaseDate().isBefore(LocalDate.parse(CINEMA_INVENTION_DATE))) {
                log.warn("The release date is wrong");
                throw new ValidationException("Cinema didn`t exist that time");
            }

            if (Integer.signum(newFilm.getDuration()) == -1) {
                log.warn("The duration is negative");
                throw new ValidationException("Duration can`t be negative");
            }
        } else if (newObjectClass == User.class) {

            User newUser = (User) newObject;
            Optional<String> newUserLogin = Optional.ofNullable(newUser.getLogin());
            Optional<String> newUserEmail = Optional.ofNullable(newUser.getEmail());

            if (newUserEmail.isEmpty() || newUserEmail.get().isBlank()) {
                log.warn("The new user doesn`t have an email");
                throw new ValidationException("An user email doesn`t exist");
            }

            if (newUserLogin.isEmpty() || newUserLogin.get().isBlank()) {
                log.warn("The new user doesn`t have a login");
                throw new ValidationException("The new user doesn`t have a login");
            }

            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                log.warn("The new user`s birthday is in the future");
                throw new ValidationException("Birthday is wrong");
            }
        } else {
            log.warn("The class is wrong");
            throw new IllegalArgumentException("The class is wrong");
        }
    }
}
