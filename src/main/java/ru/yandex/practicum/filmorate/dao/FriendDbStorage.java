package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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
    public List<Integer> getFriends(Integer userID) {
        String sqlQuery = "SELECT FRIEND_2_ID " +
                "FROM FRIENDSHIPS " +
                "WHERE FRIEND_1_ID = ?";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, UserDbStorage::makeID, userID));
    }

    @Override
    public void removeFriend(Integer userID, Integer friendID) {
        String sqlQuery = "DELETE FROM FRIENDSHIPS " +
                "WHERE FRIEND_1_ID = ? AND FRIEND_2_ID = ?";
        jdbcTemplate.update(sqlQuery, userID, friendID);
    }
}
