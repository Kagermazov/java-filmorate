package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User addUser(User newUser);

    User updateUser(User updatedUser);

    List<User> findAllUsers();

    List<User> findAllUserFriends(long userId);

    User addFriend(long userId, long newFriendId);

    List<User> showCommonFriends(long userId, long userIdToCompare);

    User unfriend(long userId, long userToUnfriendId);
}
