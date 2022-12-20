package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

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

    public List<Film> giveFilmsGenres(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final String sqlQuery = String.format("SELECT FILM_ID, FILM_GENRE.GENRE_ID, GENRES.GENRE_NAME " +
                "FROM FILM_GENRE " +
                "LEFT JOIN GENRES ON FILM_GENRE.GENRE_ID = GENRES.GENRE_ID " +
                "WHERE FILM_ID IN (%s)", inSql);
        Map<Integer, Film> filmByID = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmByID.get(rs.getInt("FILM_ID"));
            film.getGenres().add(makeGenre(rs, 0));
        }, films.stream().map(Film::getId).toArray());
        return new ArrayList<>(filmByID.values());
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
