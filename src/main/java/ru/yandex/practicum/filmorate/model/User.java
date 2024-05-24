package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotBlank
    @NotNull
    @Email
    private final String email;
    @NotBlank
    @NotNull
    private final String login;
    @NotBlank
    @NotNull
    private String name;
    @NotNull
    private LocalDate birthday;
}
