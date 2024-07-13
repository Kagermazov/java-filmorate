package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class Genre implements Serializable {
    Integer id;
    String name;
}
