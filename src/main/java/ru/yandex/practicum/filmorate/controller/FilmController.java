package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm){

        super.validator.validate(newFilm);
        newFilm.setId(getNextId(this.films));

        int newFilmId = newFilm.getId();

        this.films.put(newFilmId, newFilm);
        log.info("The film with an id {} was created", newFilmId);
        return newFilm;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film updatedFilm) {

        super.validator.validate(updatedFilm);

        int updatedFilmId = updatedFilm.getId();

        if (this.films.containsKey(updatedFilmId)) {
            this.films.put(updatedFilmId, updatedFilm);
        } else {
            throw new ValidationException("The film doesn`t exist");
        }

        log.info("The film with an id {} was updated", updatedFilmId );
        return updatedFilm;
    }

    @Override
    @GetMapping
    public Map<Integer, Film> findAll() {
        return this.films;
    }
}
