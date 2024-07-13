package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class FilmsRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        LinkedHashSet<Genre> genre = new LinkedHashSet<>();
        Set<Long> usersLikes = new HashSet<>();
        FilmRating rating = new FilmRating();

        rating.setId(resultSet.getInt("mpa_id"));
        rating.setName(resultSet.getString("mpa_name"));

        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("film_name"));
        film.setMpa(rating);
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));

        while (resultSet.next()) {
            Genre newGenre = new Genre();

            newGenre.setId(resultSet.getInt("genre_id"));
            newGenre.setName(resultSet.getString("genre_name"));
            genre.add(newGenre);
            usersLikes.add((resultSet.getLong("user_id")));
        }

        film.setGenres(genre);
        film.setUsersLikes(usersLikes);
        return film;
    }
}