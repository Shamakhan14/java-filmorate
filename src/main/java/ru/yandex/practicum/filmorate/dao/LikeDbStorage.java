package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer filmID, Integer userID) {
        final String sqlQuery = "INSERT INTO FILM_LIKES(FILM_ID, USER_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    @Override
    public List<Integer> getLikedIDs(Integer filmID) {
        final String sqlQuery = "SELECT USER_ID " +
                "FROM FILM_LIKES " +
                "WHERE FILM_ID = ?";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::getUserIDs, filmID));
    }

    @Override
    public void removeLike(Integer filmID, Integer userID) {
        final String sqlQuery = "DELETE FROM FILM_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    private Integer getUserIDs(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("USER_ID");
    }
}
