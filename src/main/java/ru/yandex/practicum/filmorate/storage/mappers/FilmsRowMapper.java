package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class FilmsRowMapper implements RowMapper<FilmsRowMapper.FilmRowDto> {

    @Data
    @NoArgsConstructor
    public static class FilmRowDto {
        Long id;
        String name;
        Long mpaId;
        Long genreId;
        String description;
        Date releaseDate;
        Integer duration;
        Long userId;
    }

    @Override
    public FilmRowDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmRowDto dto = new FilmRowDto();

        dto.setId(resultSet.getLong("id"));
        dto.setName(resultSet.getString("film_name"));
        dto.setMpaId(resultSet.getLong("rating"));
        dto.setDescription(resultSet.getString("description"));
        dto.setReleaseDate(resultSet.getDate("release_date"));
        dto.setDuration(resultSet.getInt("duration"));
        dto.setGenreId(resultSet.getLong("genre_id"));
        dto.setUserId(resultSet.getLong("user_id"));

        return dto;
    }
}