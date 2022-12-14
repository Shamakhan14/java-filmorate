package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((con) -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY " +
                "FROM USERS";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
        return users;
    }

    @Override
    public void updateUser(User user) {
        String sqlQuery = "UPDATE USERS SET " +
                "EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public User findUser(Integer userID) {
        final String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY " +
                "FROM USERS " +
                "WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userID);
        if (users.size()!=1) return null;
        return users.get(0);
    }

    @Override
    public void addFriend(Integer userID, Integer friendID) {
        String sqlQuery1 = "DELETE FROM FRIENDSHIPS " +
                "WHERE FRIEND_1_ID = ? AND FRIEND_2_ID = ?";
        String sqlQuery2 = "INSERT INTO FRIENDSHIPS(FRIEND_1_ID, FRIEND_2_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery1, userID, friendID);
        jdbcTemplate.update(sqlQuery2, userID, friendID);
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

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }

    static Integer makeID(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("FRIEND_2_ID");
    }
}
