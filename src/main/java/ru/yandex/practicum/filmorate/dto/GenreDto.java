package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GenreDto {
    private Long id;
    private String name;
}
