package ru.yandex.practicum.filmorate.storage.mappers.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GetAllUsersRowMapper implements ResultSetExtractor<List<User>> {

//    @Override
//    public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//        List<User> users = new ArrayList<>();
//        Map<Long, Set<Long>> userIdToFriendId = new HashMap<>();
//
//        while(resultSet.next()) {
////                    userIdToFriendId.put(resultSet.getLong("id"), resultSet.getLong("friend_id"));
//        }
//
//        while(resultSet.next()) {
//            User specificUser = new User();
//            specificUser.setId(resultSet.getLong("id"));
//            specificUser.setLogin(resultSet.getString("login"));
//            specificUser.setName(resultSet.getString("user_name"));
//            specificUser.setEmail(resultSet.getString("email"));
//            specificUser.setBirthday(resultSet.getDate("birthday").toLocalDate());
//
//            Long specificUserId = specificUser.getId();
//
//            if (userIdToFriendId.containsKey(specificUserId)) {
//                specificUser.setFriends(userIdToFriendId.get(specificUserId));
//            }
//
//            users.add(specificUser);
//        }
//
//        return users;
//    }

    @Override
    public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();
        Set<Long> processedUserIds = new HashSet<>();
        Map<Long, Set<Long>> userIdToFriendId = new HashMap<>();

        while (resultSet.next()) {
            long currentUserId = resultSet.getLong("id");
            long friendId = resultSet.getLong("friend_id");
            Set<Long> friends = userIdToFriendId.get(friendId);

            if (friends == null)
                friends = new HashSet<>();

            friends.add(friendId);
            userIdToFriendId.put(currentUserId, friends);

            if (processedUserIds.contains(currentUserId)) {
                continue;
            }

            processedUserIds.add(currentUserId);

            User specificUser = new User();

            specificUser.setId(currentUserId);
            specificUser.setLogin(resultSet.getString("login"));
            specificUser.setName(resultSet.getString("user_name"));
            specificUser.setEmail(resultSet.getString("email"));
            specificUser.setBirthday(resultSet.getDate("birthday").toLocalDate());

            users.add(specificUser);
        }

        users.forEach(user -> {
            Set<Long> allTheirFriends = userIdToFriendId.get(user.getId());

            if (allTheirFriends == null)
                allTheirFriends = new HashSet<>();

            user.setFriends(allTheirFriends);
        });

        return users;
    }
}
