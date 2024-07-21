package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserGetFriendsDto {
    List<Long> friends;
}
