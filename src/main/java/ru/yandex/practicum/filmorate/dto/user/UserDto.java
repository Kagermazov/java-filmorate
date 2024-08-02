package ru.yandex.practicum.filmorate.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserDto {
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
