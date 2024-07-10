package ru.yandex.practicum.filmorate.service.user;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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
    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public User addUser(@NonNull User newUser) {
        validate(newUser);
        setLoginAsNameIfNameIsAbsent(newUser);
        this.storage.addUser(newUser);

        Long newUserId = newUser.getId();

        log.info("The user with id {} was created", newUserId);
        return this.storage.getUserById(Optional.ofNullable(newUserId)
                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "The user with login " + newUser.getLogin() + " doesn't have an ID")));
    }

    @Override
    public User updateUser(@NonNull User updatedUser) {
        validate(updatedUser);
        setLoginAsNameIfNameIsAbsent(updatedUser);
        this.storage.updateUser(updatedUser);

        Long updatedUserId = updatedUser.getId();

        log.trace("The user with id {} was updated", updatedUserId);
        return this.storage.getUserById(Optional.ofNullable(updatedUserId)
                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "The user with login " + updatedUser.getLogin() + " doesn't have an ID")));
    }

    @Override
    public List<User> findAllUsers() {
        log.info("The user list was created");
        return this.storage.findAllUsers();
    }

    @Override
    public List<User> findAllUserFriends(long userId) {
        Set<Long> userFriends = Optional.ofNullable(this.storage.getUserById(userId))
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " doesn't exist")).getFriends();

        if (userFriends == null) {
            return List.of();
        }

        log.info("All users list was created");
        return userFriends.stream()
                .map(this.storage::getUserById)
                .toList();
    }

    @Override
    public @Nullable User addFriend(long userId, long newFriendId) {
        User firstUser = Optional.ofNullable(this.storage.getUserById(userId))
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " doesn't exist"));
        User newFriend = Optional.ofNullable(this.storage.getUserById(newFriendId))
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " doesn't exist"));

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
            return this.storage.getUserById(userId);
        }

        return null;
    }

    @Override
    public List<User> showCommonFriends(long userId, long userIdToCompare) {
        Set<Long> intersection = Optional.ofNullable(this.storage.getUserById(userId).getFriends())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " doesn't have friends"));
        Set<Long> secondUserFriends = Optional.ofNullable(this.storage.getUserById(userIdToCompare).getFriends())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userIdToCompare + " doesn't have friends"));

        if (intersection.retainAll(secondUserFriends)) {
            log.info("The list of common fiends user with id {} and user with id {} was created", userId, userIdToCompare);
            return intersection.stream()
                    .map(this.storage::getUserById)
                    .toList();
        }

        log.info("The user with id {} and user with id {} don't have common friends", userId, userIdToCompare);
        return List.of();
    }

    @Override
    public User unfriend(long userId, long userToUnfriendId) {
        User userToReturn = Optional.ofNullable(this.storage.getUserById(userId))
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "There's no user with id " + userId));
        User userToUnfriend = Optional.ofNullable(this.storage.getUserById(userToUnfriendId))
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "There's no user with id " + userToUnfriendId));
        Set<Long> userToReturnFriends = userToReturn.getFriends();
        Set<Long> userToUnfriendFriends = userToUnfriend.getFriends();

        if (userToReturnFriends != null
                && userToUnfriendFriends != null
                && userToReturnFriends.contains(userToUnfriendId)
                && userToUnfriendFriends.contains(userId)) {

            userToReturnFriends.remove(userToUnfriendId);
            userToUnfriendFriends.remove(userId);
            log.info("The user with id {} was unfriend from user with id {}", userToUnfriendId, userId);
            return userToReturn;
        }

        log.info("The user with id {} isn't user with id {} friend", userToUnfriendId, userId);
        return userToReturn;
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
