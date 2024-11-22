package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.filmorate.service.film.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity(name = "Film")
//@Builder
@Getter
@Setter
@ToString
//@EqualsAndHashCode
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue
    @Nullable
    @Positive
    private Long id;

    @NotBlank(message = "A film name in mandatory")
    private String name;

    @OneToOne
    @Nullable
    private Mpa mpa;

    @Size(max = 200, message = "A description length is more than 200 symbols")
    @NotBlank(message = "A film description in mandatory")
    private String description;

    @NonNull
    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @NonNull
    @Positive
    private Integer duration;

    @OneToMany
    @Nullable
    private List<Genre> genres;

    @Nullable
    private Set<Long> usersLikes;
}
