package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {

    private MpaMapper() {
    }

    public static MpaDto mapToMpaDto(Mpa specificMpa) {
        MpaDto dto = new MpaDto();

        dto.setId(specificMpa.getId());
        dto.setName(specificMpa.getName());
        return dto;
    }
}
