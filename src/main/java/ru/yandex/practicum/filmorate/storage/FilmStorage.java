package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);
    List<Film> getFilms();
    void updateFilm(Film film);
    Film findFilm(Integer filmID);
    List<Film> getTopFilms(Integer limit);
}
