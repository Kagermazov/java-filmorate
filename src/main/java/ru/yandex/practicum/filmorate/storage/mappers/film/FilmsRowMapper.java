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

        FilmRowDto filmRowDto = FilmRowDto.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date"))
                .duration(resultSet.getInt("duration"))
                .build();

        long userId = resultSet.getLong("user_id");

        if (userId != 0) {
            filmRowDto.setUserId(userId);
        }

        long rating = resultSet.getLong("rating");
        String mpaName = resultSet.getString("mpa_name");

        if (rating != 0 && mpaName != null) {
            Mpa filmMpa = Mpa.builder()
                    .id(rating)
                    .name(mpaName)
                    .build();

            filmRowDto.setMpa(filmMpa);
        }

        long genreId = resultSet.getLong("genre_id");
        String genreName = resultSet.getString("genre_name");

        if (genreId != 0 && genreName != null) {
            Genre filmGenre = Genre.builder()
                    .id(genreId)
                    .name(genreName)
                    .build();

            filmRowDto.setGenre(filmGenre);
        }

        return filmRowDto;
    }
}