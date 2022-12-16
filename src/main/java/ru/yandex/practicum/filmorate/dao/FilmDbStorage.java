package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        final String sqlQuery = "INSERT INTO FILMS(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((con) -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate release = film.getReleaseDate();
            if (release == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(release));
            }
            stmt.setInt(4, film.getDuration());
            final Rating rating = film.getMpa();
            stmt.setInt(5, rating.getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        if (film.getGenres()!=null && film.getGenres().size()!=0) {
            batchUpdateGenres(film);
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATINGS.RATING_NAME " +
                "FROM FILMS " +
                "LEFT JOIN RATINGS ON FILMS.MPA = RATINGS.RATING_ID";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        return films;
    }

    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteGenres(film.getId());
        if (film.getGenres()!=null && film.getGenres().size()!=0) {
            batchUpdateGenres(film);
        }
    }

    @Override
    public Film findFilm(Integer filmID) {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATINGS.RATING_NAME " +
                "FROM FILMS " +
                "LEFT JOIN RATINGS ON FILMS.MPA = RATINGS.RATING_ID " +
                "WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, filmID);
        if (films.size()!=1) return null;
        return films.get(0);
    }

    @Override
    public void addLike(Integer filmID, Integer userID) {
        removeLike(filmID, userID);
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

    @Override
    public List<Film> getTopFilms(Integer limit) {
        String sqlQuery = "SELECT FILMS.FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATINGS.RATING_NAME " +
                "FROM FILMS " +
                "LEFT JOIN FILM_LIKES FL ON FILMS.FILM_ID = FL.FILM_ID " +
                "LEFT JOIN RATINGS ON FILMS.MPA = RATINGS.RATING_ID " +
                "GROUP BY FILMS.FILM_ID " +
                "ORDER BY COUNT(USER_ID) DESC " +
                "LIMIT ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, limit);
        return films;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int filmID = rs.getInt("FILM_ID");
        String filmName = rs.getString("FILM_NAME");
        String filmDescription = rs.getString("DESCRIPTION");
        LocalDate filmDate = rs.getDate("RELEASE_DATE").toLocalDate();
        Integer filmDuration = rs.getInt("DURATION");
        Rating filmRating = new Rating(rs.getInt("MPA"), rs.getString("RATING_NAME"));
        //List<Genre> genres = getFilmGenres(filmID);
        return new Film(filmID, filmName, filmDescription, filmDate, filmDuration, filmRating/*, genres*/);
    }

    private List<Genre> getFilmGenres(Integer filmID) {
        final String sqlQuery = "SELECT GENRES.GENRE_ID, GENRE_NAME " +
                "FROM GENRES " +
                "LEFT JOIN FILM_GENRE AS FG ON GENRES.GENRE_ID = FG.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, filmID);
    }

    private Integer getUserIDs(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("USER_ID");
    }

    private void deleteGenres(Integer id) {
        String deleteGenresQuery = "DELETE FROM FILM_GENRE " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteGenresQuery, id);
    }

    private int[] batchUpdateGenres(Film film) {
        Set<Genre> genres = new HashSet<>(film.getGenres());
        List<Genre> genreList = new ArrayList<>(genres);
        return this.jdbcTemplate.batchUpdate(
                "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genreList.get(i).getId());
                    }
                    @Override
                    public int getBatchSize() {
                        return genreList.size();
                    }
                });
    }
}
