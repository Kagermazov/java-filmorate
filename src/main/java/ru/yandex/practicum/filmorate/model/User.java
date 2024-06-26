package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    @Nullable
    private Long id;
    @NotBlank(message = "An email is mandatory")
    @Email
    private String email;
    @NotBlank(message = "A login is mandatory")
    private String login;
    @Nullable
    private String name;
    @NonNull
    private LocalDate birthday;
    @Nullable
    private Set<Long> friends;
}
