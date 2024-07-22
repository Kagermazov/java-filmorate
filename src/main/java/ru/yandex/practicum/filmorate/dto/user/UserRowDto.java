package ru.yandex.practicum.filmorate.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserRowDto {
    @Nullable
    private Long id;
    @Email
    private String email;
    private String login;
    @Nullable
    private String name;
    @NonNull
    private LocalDate birthday;
    @Nullable
    private Long friendId;
}