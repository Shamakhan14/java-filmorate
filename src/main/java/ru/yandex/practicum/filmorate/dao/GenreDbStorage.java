package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(Integer id) {
        final String sqlQuery = "SELECT GENRE_ID, GENRE_NAME " +
                "FROM GENRES " +
                "WHERE GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id);
        if (genres.size()!=1) return null;
        return genres.get(0);
    }

    @Override
    public List<Genre> getGenres() {
        final String sqlQuery = "SELECT GENRE_ID, GENRE_NAME " +
                "FROM GENRES";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
        return genres;
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
