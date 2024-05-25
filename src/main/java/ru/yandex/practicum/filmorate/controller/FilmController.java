package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    private final List<Film> films = new ArrayList<>();

    @Override
    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {

        super.validator.validate(newFilm);
        newFilm.setId(getNextId(this.films));

        int newFilmId = newFilm.getId();

        this.films.add(newFilm);
        log.info("The film with an id {} was created", newFilmId);
        return newFilm;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film updatedFilm) {

        super.validator.validate(updatedFilm);

        int updatedFilmId = updatedFilm.getId();

        if (isObjectInStorage(updatedFilm, this.films)) {
            this.films.add(updatedFilm);
        } else {
            throw new ValidationException("The film doesn`t exist");
        }

        log.info("The film with an id {} was updated", updatedFilmId);
        return updatedFilm;
    }

    @Override
    @GetMapping
    public List<Film> findAll(@RequestParam(value = "storage", required = false) List<Film> storage) {
        return this.films;
    }
}
