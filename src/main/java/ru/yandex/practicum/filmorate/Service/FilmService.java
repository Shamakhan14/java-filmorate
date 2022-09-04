package ru.yandex.practicum.filmorate.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void updateFilm(Film film) {
        Film newFilm = findFilm(film.getId());
        filmStorage.updateFilm(film);
    }

    public Film findFilm(Integer filmID) {
        Film film = filmStorage.findFilm(filmID);
        if (film == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        return film;
    }

    public void addLike(Integer filmID, Integer userID) {
        if (userStorage.findUser(userID) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        filmStorage.findFilm(filmID).addLike(userID);
    }

    public void removeLike(Integer filmID, Integer userID) {
        if (userStorage.findUser(userID) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        if (!findFilm(filmID).getLikedIDs().contains(userID))
            throw new ObjectNotFoundException("Пользователю уже не нравится данный фильм.");
        findFilm(filmID).removeLike(userID);
    }

    public List<Film> getTopFilms(int count) {
        if (count <= 0) throw new ValidationException("Неверно указано количество фильмов.");
        if (count > getFilms().size()) count = getFilms().size();
        List<Film> sortedFilms = filmStorage.getFilms();
        sortedFilms.sort((Film o1, Film o2) -> o1.getLikedIDs().size() - o2.getLikedIDs().size());
        return new ArrayList<>(sortedFilms.subList(sortedFilms.size()-count, sortedFilms.size()));
    }
}
