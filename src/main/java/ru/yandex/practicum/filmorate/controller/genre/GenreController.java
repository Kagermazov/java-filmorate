package ru.yandex.practicum.filmorate.controller.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @Autowired
    public GenreController(GenreService serviceForGenre) {
        service = serviceForGenre;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto findGenreById(@PathVariable Long id) {
        return service.findGenreById(id);
    }
}
