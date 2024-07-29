package ru.yandex.practicum.filmorate.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
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
