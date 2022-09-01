package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private Map<Integer, Film> films;
    private static int ids = 0;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    @Override
    public void addFilm(Film film) {
        film.setId(++ids);
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Film findFilm(Integer filmID) {
        return films.get(filmID);
    }
}
