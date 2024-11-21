package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Genre")
//@Builder
@Getter
@Setter
@ToString
//@EqualsAndHashCode
public class Genre {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
