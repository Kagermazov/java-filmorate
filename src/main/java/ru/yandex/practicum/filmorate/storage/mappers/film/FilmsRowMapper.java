package ru.yandex.practicum.filmorate.storage.mappers.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.film.FilmRowDto;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class FilmsRowMapper implements RowMapper<FilmRowDto>, Serializable {

    @Override
    public FilmRowDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        FilmRowDto filmRowDto = new FilmRowDto();
        long userId = resultSet.getLong("user_id");

        if (userId != 0) {
            filmRowDto.setUserId(userId);
        }

        filmRowDto.setId(resultSet.getLong("id"));
        filmRowDto.setName(resultSet.getString("film_name"));
        filmRowDto.setDescription(resultSet.getString("description"));
        filmRowDto.setReleaseDate(resultSet.getDate("release_date"));
        filmRowDto.setDuration(resultSet.getInt("duration"));

        Mpa filmMpa = new Mpa();
        long rating = resultSet.getLong("rating");
        String mpaName = resultSet.getString("mpa_name");

        if (rating != 0 && mpaName != null) {
            filmMpa.setId(rating);
            filmMpa.setName(mpaName);
            filmRowDto.setMpa(filmMpa);
        }

        Genre filmGenre = new Genre();
        long genreId = resultSet.getLong("genre_id");
        String genreName = resultSet.getString("genre_name");

        if (genreId != 0 && genreName != null) {
            filmGenre.setId(genreId);
            filmGenre.setName(genreName);
            filmRowDto.setGenre(filmGenre);
        }

        log.info("a filmRowDto is created");
        return filmRowDto;
    }
}