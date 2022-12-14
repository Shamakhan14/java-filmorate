package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RatingDbStorage implements RatingStorage {

     private final JdbcTemplate jdbcTemplate;

     @Autowired
     public RatingDbStorage(JdbcTemplate jdbcTemplate) {
         this.jdbcTemplate = jdbcTemplate;
     }

     @Override
     public Rating getRating(Integer id) {
         final String sqlQuery = "SELECT RATING_ID, RATING_NAME " +
                 "FROM RATINGS " +
                 "WHERE RATING_ID = ?";
         final List<Rating> ratings = jdbcTemplate.query(sqlQuery, RatingDbStorage::makeRating, id);
         if (ratings.size()!=1) return null;
         return ratings.get(0);
     }

     @Override
     public List<Rating> getRatings() {
         final String sqlQuery = "SELECT RATING_ID, RATING_NAME " +
                 "FROM RATINGS";
         final List<Rating> ratings = jdbcTemplate.query(sqlQuery, RatingDbStorage::makeRating);
         return ratings;
     }

     static Rating makeRating(ResultSet rs, int rowNum) throws SQLException {
         return new Rating(rs.getInt("RATING_ID"),
                 rs.getString("RATING_NAME"));
     }
}
