package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmsRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    public static final String ADD_FILM =
            "INSERT INTO films (film_name, rating, description, release_date, duration) VALUES (?, ?, ?, ?, ?);";
    private final JdbcTemplate jdbc;
    private final FilmsRowMapper mapper;

    @Override
    public void addFilm(Film newFilm) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_FILM, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, newFilm.getFilmName());
            statement.setObject(2, newFilm.getRating());
            statement.setObject(3, newFilm.getDescription());
            statement.setObject(4, newFilm.getReleaseDate());
            statement.setObject(5, newFilm.getDuration());
            return statement;
        }, keyHolder);
    }

    @Override
    public void updateFilm(Film updatedFilm) {

    }

    @Override
    public void deleteFilms() {

    }

    @Override
    public List<Film> findAllFilms() {
        String query = "SELECT * FROM films";
        return jdbc.query(query, mapper);
    }

    @Override
    public Film getFilmById(long filmId) {
        return null;
    }
}
