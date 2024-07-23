package ru.yandex.practicum.filmorate.service.user;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Getter
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Autowired
    public UserServiceImpl(@Qualifier("inMemoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserDto addUser(@NonNull User newUser) {
        validate(newUser);
        setLoginAsNameIfNameIsAbsent(newUser);
        this.storage.addUser(newUser);

        Long newUserId = newUser.getId();

        log.info("The user with id {} was created", newUserId);
        return UserMapper.mapToUserCreateDto(getUserById(newUserId));
    }

    private User getUserById(Long newUserId) {
        return this.storage.findAllUsers().stream()
                .filter(user -> user.getId() == newUserId)
                .findFirst()
                .get();
    }

    @Override
    public UserDto updateUser(@NonNull User updatedUser) {
        validate(updatedUser);
        setLoginAsNameIfNameIsAbsent(updatedUser);
        this.storage.updateUser(updatedUser);

        Long updatedUserId = updatedUser.getId();

        log.trace("The user with id {} was updated", updatedUserId);
        return UserMapper.mapToUserCreateDto(getUserById(updatedUserId));
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("The user list was created");
        return this.storage.findAllUsers().stream()
                .map(UserMapper::mapToUserCreateDto)
                .toList();
    }

    @Override
    public List<UserFriendDto> findAllUserFriends(Long userId) {
        Set<Long> userFriends = getUserById(userId).getFriends();

        if (userFriends == null) {
            return List.of();
        }

        log.info("All users list was created");
        return List.of();
    }

    @Override
    public @Nullable void addFriend(Long userId, Long newFriendId) {
        User firstUser = getUserById(userId);
        User newFriend = getUserById(newFriendId);

        if (firstUser.getFriends() == null) {
            firstUser.setFriends(new HashSet<>());
        }
//fixme вынести проверку на сет друзей из-под ифа
        if (firstUser.getFriends().add(newFriendId) && newFriend.getFriends() == null) {
            newFriend.setFriends(new HashSet<>());
            newFriend.getFriends().add(userId);
            this.storage.updateUser(firstUser);
            this.storage.updateUser(newFriend);
            log.info("The user with id {} was added to the user with id {} friends", newFriendId, userId);
        }
    }

    @Override
    public List<UserFriendDto> showCommonFriends(Long userId, Long userIdToCompare) {
        Set<Long> intersection = Optional.ofNullable(getUserById(userId).getFriends())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " doesn't have friends"));
        Set<Long> secondUserFriends = Optional.ofNullable(getUserById(userIdToCompare).getFriends())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userIdToCompare + " doesn't have friends"));

        if (intersection.retainAll(secondUserFriends)) {
            log.info("The list of common fiends user with id {} and user with id {} was created", userId, userIdToCompare);
//            return intersection.stream()
//                    .map(this::getUserById)
//                    .map(UserMapper::mapToUserCreateDto)
//                    .toList();
            return List.of();
        }

        log.info("The user with id {} and user with id {} don't have common friends", userId, userIdToCompare);
        return List.of();
    }

    @Override
    public void removeFriend(Long userId, Long userToUnfriendId) {
        User userToReturn = getUserById(userId);
        User userToUnfriend = getUserById(userToUnfriendId);
        Set<Long> userToReturnFriends = userToReturn.getFriends();
        Set<Long> userToUnfriendFriends = userToUnfriend.getFriends();

        if (userToReturnFriends != null
                && userToUnfriendFriends != null
                && userToReturnFriends.contains(userToUnfriendId)
                && userToUnfriendFriends.contains(userId)) {

            userToReturnFriends.remove(userToUnfriendId);
            userToUnfriendFriends.remove(userId);
            log.info("The user with id {} was unfriend from user with id {}", userToUnfriendId, userId);
        }

        log.info("The user with id {} isn't user with id {} friend", userToUnfriendId, userId);
    }

    private void validate(@NonNull User userToCheck) {
        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The new user's birthday is in the future");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The birthday is wrong");
        }
    }

    private void setLoginAsNameIfNameIsAbsent(@NonNull User newUser) {
        String newUserName = Optional.ofNullable(newUser.getName()).orElseThrow();
        String newUserLogin = newUser.getLogin();

        if (newUserName.isEmpty() || newUserName.isBlank()) {
            newUser.setName(newUserLogin);
        }
    }
}
