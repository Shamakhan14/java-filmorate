package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
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
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        final String genreSqlQuery = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES (?, ?)";
        if (film.getGenres()!=null && film.getGenres().size()!=0) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            for (Genre genre: genres) {
                jdbcTemplate.update(genreSqlQuery, film.getId(), genre.getId());
            }
        }
        film.setMpa(getFilmRating(film.getId()));
        film.setGenres(getFilmGenres(film.getId()));
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA " +
                "FROM FILMS";
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
        String deleteGenresQuery = "DELETE FROM FILM_GENRE " +
            "WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteGenresQuery, film.getId());
        final String genreSqlQuery = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES (?, ?)";
        if (film.getGenres()!=null && film.getGenres().size()!=0) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            for (Genre genre: genres) {
                jdbcTemplate.update(genreSqlQuery, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public Film findFilm(Integer filmID) {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION " +
                "FROM FILMS " +
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
        String sqlQuery = "SELECT FILMS.FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA " +
                "FROM FILMS " +
                "LEFT JOIN FILM_LIKES FL ON FILMS.FILM_ID = FL.FILM_ID " +
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
        Rating filmRating = getFilmRating(filmID);
        List<Genre> genres = getFilmGenres(filmID);
        return new Film(filmID, filmName, filmDescription, filmDate, filmDuration, filmRating, genres);
    }

    private List<Genre> getFilmGenres(Integer filmID) {
        final String sqlQuery = "SELECT GENRES.GENRE_ID, GENRE_NAME " +
                "FROM GENRES " +
                "LEFT JOIN FILM_GENRE AS FG ON GENRES.GENRE_ID = FG.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, filmID);
    }

    private Rating getFilmRating(Integer filmID) {
        final String sqlQuery = "SELECT RATING_ID, RATING_NAME " +
                "FROM RATINGS " +
                "JOIN FILMS ON RATING_ID = FILMS.MPA " +
                "WHERE FILMS.FILM_ID = ?";
        final List<Rating> ratings = jdbcTemplate.query(sqlQuery, RatingDbStorage::makeRating, filmID);
        if (ratings.size()!=1) return null;
        return ratings.get(0);
    }

    private Integer getUserIDs(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("USER_ID");
    }
}
