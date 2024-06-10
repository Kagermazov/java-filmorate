package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class User implements BaseEntity {
    private int id;
    @NotBlank(message = "An email is mandatory")
    @Email
    private final String email;
    @NotBlank(message = "A login is mandatory")
    private final String login;
    private String name;
    @NotNull
    private LocalDate birthday;
}
