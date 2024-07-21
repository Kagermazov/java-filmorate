package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public class FilmMapper {

    private FilmMapper() {
    }

    public static FilmDto mapToFilmDto(Film specificFilm) {
        FilmDto dto = new FilmDto();
        List<GenreDto> filmGenres = specificFilm.getGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
        List<GenreDto> genreDtos = null;

        if (filmGenres != null) {
            genreDtos = specificFilm.getGenres().stream()
                    .map(GenreMapper::mapToGenreDto)
                    .toList();
        }

        dto.setId(specificFilm.getId());
        dto.setName(specificFilm.getName());
        dto.setMpa(MpaMapper.mapToMpaDto(specificFilm.getMpa()));
        dto.setDescription(specificFilm.getDescription());
        dto.setReleaseDate(specificFilm.getReleaseDate());
        dto.setDuration(specificFilm.getDuration());
        dto.setGenres(genreDtos);
        dto.setUsersLikes(specificFilm.getUsersLikes());
        return dto;
    }
}
