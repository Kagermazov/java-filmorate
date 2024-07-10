package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FilmsRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        Set<String> genre = new HashSet<>();
        Set<Long> usersLikes = new HashSet<>();

        while(resultSet.next()) {
            genre.add(resultSet.getString("genre_name"));
            usersLikes.add((resultSet.getLong("likes")));
        }

        film.setGenre(genre);
        film.setUsersLikes(usersLikes);
        film.setId(resultSet.getLong("id"));
        film.setFilmName(resultSet.getString("filmName"));
        film.setRating(FilmRating.valueOf(resultSet.getString("rating")));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(LocalDate.from
                (Instant.ofEpochMilli(resultSet.getDate("release_date").getTime())));
        film.setDuration(resultSet.getInt("duration"));
        return film;
    }
}