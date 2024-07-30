package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    @Nullable
    @Positive
    private Long id;
    @NotBlank(message = "A login is mandatory")
    private String login;
    @Nullable
    private String name;
    @NotBlank(message = "An email is mandatory")
    @Email
    private String email;
    @NonNull
    private LocalDate birthday;
    @Nullable
    private Set<Long> friends;
}
