package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public class FilmMapper {

    private FilmMapper() {
    }

    public static FilmCreateDto mapToFilmDto(Film specificFilm) {
        FilmCreateDto dto = new FilmCreateDto();
        List<Genre> filmGenres = specificFilm.getGenres();
        List<GenreDto> genreDtos = null;

        if (filmGenres != null) {
            genreDtos = specificFilm.getGenres().stream()
                    .map(GenreDtoMapper::mapToGenreDto)
                    .toList();
        }

        dto.setId(specificFilm.getId());
        dto.setName(specificFilm.getName());
        dto.setMpa(new MpaDto(specificFilm.getMpa().getId()));
        dto.setDescription(specificFilm.getDescription());
        dto.setReleaseDate(specificFilm.getReleaseDate());
        dto.setDuration(specificFilm.getDuration());
        dto.setGenres(genreDtos);
        dto.setUsersLikes(specificFilm.getUsersLikes());
        return dto;
    }
}
