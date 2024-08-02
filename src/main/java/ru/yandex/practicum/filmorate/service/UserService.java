package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserFriendDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storageDb) {
        storage = storageDb;
    }

    public UserDto addUser(User newUser) {
        Long newUserId = newUser.getId();

        checkIfUserIdIsNull(newUserId);
        validateUserBirthday(newUser);

        Long id = storage.addUser(newUser);

        newUser.setId(id);
        log.info("The user with an id {} was created", id);
        return UserMapper.mapToDto(newUser);
    }

    public UserDto updateUser(User updatedUser) {
        Long updatedUserId = updatedUser.getId();

        checkIfIdExists(updatedUserId);
        storage.updateUser(updatedUser);

        log.info("The user with an id {} was updated", updatedUserId);

        return getUserDto(updatedUserId);
    }

    public List<UserDto> findAllUsers() {
        return storage.findAllUsers().stream()
                .map(UserMapper::mapToDto)
                .toList();
    }

    public List<UserFriendDto> findAllUserFriends(Long userId) {
        checkIfIdExists(userId);

        Set<Long> friendIds = storage.getUserById(userId).getFriends();
        List<UserFriendDto> friends = getFriends(friendIds);

        if (friends != null) {
            return friends;
        }

        return List.of();
    }

    public void addFriend(Long userId, Long friendId) {
        checkIfIdExists(friendId);
        storage.addFriend(userId, friendId);
    }

    public List<UserFriendDto> showCommonFriends(Long userId, Long userIdToCompare) {
        User firstUser = storage.getUserById(userId);
        User userToCompare = storage.getUserById(userIdToCompare);

        Set<Long> intersection = getUsersFriends(userId, firstUser);
        Set<Long> secondUserFriends = getUsersFriends(userIdToCompare, userToCompare);

        intersection.retainAll(secondUserFriends);
        log.info("The list of common fiends user with id {} and user with id {} was created",
                userId, userIdToCompare);

        return getIntersection(intersection);
    }

    public void removeFriend(Long userId, Long userToUnfriendId) {
        checkIfIdExists(userId);
        checkIfIdExists(userToUnfriendId);
        checkIfUserHasThatFriend(userId, userToUnfriendId);
        storage.removeFriend(userId, userToUnfriendId);

    }

    private void checkIfUserHasThatFriend(Long userId, Long userToUnfriendId) {
        Set<Long> userFriends = storage.getUserById(userId).getFriends();

        if (userFriends == null || !userFriends.contains(userToUnfriendId)) {
            throw new ValidationException(HttpStatus.OK,
                    "The user with an id " + userId + " doesn't have a friend with an id " + userToUnfriendId);
        }
    }

    private static List<UserFriendDto> getIntersection(Set<Long> intersection) {
        return intersection.stream()
                .map(commonFriendId -> UserFriendDto.builder()
                        .id(commonFriendId)
                        .build())
                .toList();
    }

    private static List<UserFriendDto> getFriends(Set<Long> friendIds) {
        if (friendIds != null) {
            return friendIds.stream()
                    .map(friendId -> UserFriendDto.builder()
                            .id(friendId)
                            .build())
                    .toList();
        }
        return List.of();
    }

    private static Set<Long> getUsersFriends(Long userId, User specificUser) {
        return Optional.ofNullable(specificUser.getFriends())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " doesn't have friends"));
    }

    private void validateUserBirthday(User userToCheck) {
        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The new user's birthday is in the future");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The birthday is wrong");
        }
    }

    private void checkIfIdExists(Long friendId) {
        if (friendId == null || storage.getUserById(friendId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no user with an id " + friendId);
        }
    }

    private static void checkIfUserIdIsNull(Long newUserId) {
        if (newUserId != null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The user has the id " + newUserId);
        }
    }

    private UserDto getUserDto(Long updatedUserId) {
        return UserMapper.mapToDto(storage.getUserById(updatedUserId));
    }
}
