package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {

    private MpaMapper() {
    }

    public static MpaDto mapToMpaDto(Mpa specificMpa) {
        return MpaDto.builder()
                .id(specificMpa.getId())
                .name(specificMpa.getName())
                .build();
    }
}
