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
        Mpa filmMpa = new Mpa();
        Genre filmGenre = new Genre();

        filmMpa.setId(resultSet.getLong("rating"));
        filmMpa.setName(resultSet.getString("mpa_name"));

        filmGenre.setId(resultSet.getLong("genre_id"));
        filmGenre.setName(resultSet.getString("genre_name"));

        filmRowDto.setId(resultSet.getLong("id"));
        filmRowDto.setName(resultSet.getString("film_name"));
        filmRowDto.setMpa(filmMpa);
        filmRowDto.setDescription(resultSet.getString("description"));
        filmRowDto.setReleaseDate(resultSet.getDate("release_date"));
        filmRowDto.setDuration(resultSet.getInt("duration"));
        filmRowDto.setGenre(filmGenre);
        filmRowDto.setUserId(resultSet.getLong("user_id"));

        log.info("a filmRowDto is created");
        return filmRowDto;
    }
}