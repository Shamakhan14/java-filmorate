package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);
    List<Film> getFilms();
    void updateFilm(Film film);
    Film findFilm(Integer filmID);
    void addLike(Integer filmID, Integer userID);
    List<Integer> getLikedIDs(Integer filmID);
    void removeLike(Integer filmID, Integer userID);
    List<Film> getTopFilms(Integer limit);
}
