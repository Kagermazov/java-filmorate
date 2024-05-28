package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final String CINEMA_INVENTION_DATE = "1895-12-28";
    private final Map<Integer, Film> films;

    public FilmController() {
        this.films = new HashMap<>();
    }

    @Override
    @PostMapping
    public final Film create(@Valid @RequestBody final Film newFilm) {

        this.validate(newFilm);
        newFilm.setId(getNextId(this.films));

        int newFilmId = newFilm.getId();

        Object puttingResult = this.films.putIfAbsent(newFilmId, newFilm);

        if (puttingResult != null) {
            throw new ValidationException("Such user exists already");
        }

        log.info("The film with an id {} was created", newFilmId);
        return newFilm;
    }

    @Override
    @PutMapping
    public final Film update(@Valid @RequestBody final Film updatedFilm) {

        this.validate(updatedFilm);

        int updatedFilmId = updatedFilm.getId();

        Object puttingResult = this.films.putIfAbsent(updatedFilmId, updatedFilm);

        if (puttingResult == null) {
            throw new ValidationException("Such user doesn't already");
        }

        log.info("The film with an id {} was updated", updatedFilmId);
        return updatedFilm;
    }

    @GetMapping
    public final List<Film> getFilms() {
        return findAll(this.films);
    }

    @Override
    public final void validate(final Film filmToCheck) {
        Optional<String> newFilmName = Optional.ofNullable(filmToCheck.getName());

        if (newFilmName.isEmpty() || newFilmName.get().isBlank()) {
            log.warn("A film name absents");
            throw new ValidationException("A film name absents");
        }

        if (filmToCheck.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("A description length is more than 200 symbols");
            throw new ValidationException("A description length is more than 200 symbols");
        }

        if (filmToCheck.getReleaseDate().isBefore(LocalDate.parse(CINEMA_INVENTION_DATE))) {
            log.warn("The release date is wrong");
            throw new ValidationException("Cinema didn't exist that time");
        }

        if (Integer.signum(filmToCheck.getDuration()) == -1) {
            log.warn("The duration is negative");
            throw new ValidationException("Duration can`t be negative");
        }
    }
}
