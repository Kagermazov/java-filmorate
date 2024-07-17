package ru.yandex.practicum.filmorate.storage.mappers.film;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Component
public class FilmsRowExtractor implements ResultSetExtractor<List<Film>> {

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
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<FilmRowDto> results = (new ArrayList<>());
        while (rs.next()) {
            results.add(
                    mapRow(rs)
            );
        }

        Map<Long, List<FilmRowDto>> filmIdToFilmRowDto = results.stream()
                .collect(groupingBy(FilmRowDto::getId));

        return filmIdToFilmRowDto.values().stream()
                .map(this::combineRows)
                .toList();
    }

    private FilmRowDto mapRow(ResultSet resultSet) throws SQLException {
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

    private Film combineRows(List<FilmRowDto> dtos) {
        Film filmToReturn = new Film();
        FilmRowDto firstDto = dtos.getFirst();

        filmToReturn.setId(firstDto.getId());
        filmToReturn.setName(firstDto.getName());

        FilmRating mpa = new FilmRating();

        mpa.setId(firstDto.getMpaId());

        filmToReturn.setMpa(mpa);
        filmToReturn.setDescription(firstDto.getDescription());
        filmToReturn.setDuration(firstDto.getDuration());
        filmToReturn.setReleaseDate(firstDto.getReleaseDate().toLocalDate());

        filmToReturn.setGenres(
                dtos.stream()
                        .map(FilmRowDto::getGenreId)
                        .distinct()
                        .sorted()
                        .map(id -> {
                            Genre filmGenre = new Genre();
                            filmGenre.setId(id);
                            return filmGenre;
                        })
                        .toList()
        );

        filmToReturn.setUsersLikes(
                new LinkedHashSet<>(
                        dtos.stream()
                                .map(FilmRowDto::getUserId)
                                .distinct()
                                .sorted()
                                .toList()
                )
        );

        return filmToReturn;
    }
}