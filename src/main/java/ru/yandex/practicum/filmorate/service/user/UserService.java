package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserFriendDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    UserDto addUser(User newUser);

    UserDto updateUser(User updatedUser);

    List<UserDto> findAllUsers();

    List<UserFriendDto> findAllUserFriends(Long userId);

    void addFriend(Long userId, Long newFriendId);

    List<UserFriendDto> showCommonFriends(Long userId, Long userIdToCompare);

    void removeFriend(Long userId, Long friendId);
}
