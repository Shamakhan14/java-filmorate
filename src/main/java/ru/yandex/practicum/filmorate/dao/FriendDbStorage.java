package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer userID, Integer friendID) {
        String sqlQuery = "INSERT INTO FRIENDSHIPS(FRIEND_1_ID, FRIEND_2_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userID, friendID);
    }

    @Override
    public List<User> getFriends(Integer userID) {
        String sqlQuery = "SELECT USERS.USER_ID, USERS.EMAIL, USERS.LOGIN, USERS.USER_NAME, USERS.BIRTHDAY " +
                "FROM FRIENDSHIPS " +
                "LEFT JOIN USERS ON FRIENDSHIPS.FRIEND_2_ID = USERS.USER_ID " +
                "WHERE FRIEND_1_ID = ?";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userID));
    }

    @Override
    public void removeFriend(Integer userID, Integer friendID) {
        String sqlQuery = "DELETE FROM FRIENDSHIPS " +
                "WHERE FRIEND_1_ID = ? AND FRIEND_2_ID = ?";
        jdbcTemplate.update(sqlQuery, userID, friendID);
    }

    @Override
    public List<User> getMutualFriends(Integer userID, Integer friendID) {
        String sqlQuery = "SELECT USERS.USER_ID, USERS.EMAIL, USERS.LOGIN, USERS.USER_NAME, USERS.BIRTHDAY " +
                "FROM FRIENDSHIPS " +
                "LEFT JOIN USERS ON FRIENDSHIPS.FRIEND_2_ID = USERS.USER_ID " +
                "WHERE FRIEND_1_ID = ? AND " +
                "FRIEND_2_ID IN (" +
                "SELECT FRIEND_2_ID FROM FRIENDSHIPS WHERE FRIEND_1_ID = ?)";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userID, friendID));
    }
}
