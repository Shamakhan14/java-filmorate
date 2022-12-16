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

        @AllArgsConstructor
        @Data
        @Getter
        class RowResult {
            private Integer filmID;
            private Integer genreID;
            private String genreName;
        }

        List<Integer> filmIDs = new ArrayList<>();
        for (Film film: films) {
            filmIDs.add(film.getId());
        }
        String inSql = String.join(",", Collections.nCopies(filmIDs.size(), "?"));
        List<RowResult> results = jdbcTemplate.query(
                String.format("SELECT FILM_ID, FILM_GENRE.GENRE_ID, GENRES.GENRE_NAME " +
                        "FROM FILM_GENRE " +
                        "LEFT JOIN GENRES ON FILM_GENRE.GENRE_ID = GENRES.GENRE_ID " +
                        "WHERE FILM_ID IN (%s)", inSql),
                filmIDs.toArray(),
                (rs, rowNum) -> new RowResult(rs.getInt("FILM_ID"),
                        rs.getInt("GENRE_ID"),
                        rs.getString("GENRE_NAME")));
        for (Film film: films) {
            for (int i = results.size()-1; i >=0; i--) {
                if (film.getId() == results.get(i).getFilmID()) {
                    film.getGenres().add(new Genre(results.get(i).getGenreID(), results.get(i).getGenreName()));
                }
            }
        }
        return films;
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
