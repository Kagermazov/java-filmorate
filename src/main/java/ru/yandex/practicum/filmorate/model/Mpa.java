package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "MPA")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Mpa {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    private String name;
}