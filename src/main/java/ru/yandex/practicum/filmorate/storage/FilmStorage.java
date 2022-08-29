package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void addFilm(Film film);
    List<Film> getFilms();
    void updateFilm(Film film);
}
