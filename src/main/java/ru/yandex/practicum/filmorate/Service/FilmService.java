package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer filmID, Integer userID) {
        filmStorage.findFilm(filmID).addLike(userID);
    }

    public void removeLike(Integer filmID, Integer userID) {
        filmStorage.findFilm(filmID).removeLike(userID);
    }

    public List<Film> getTopFilms(int top) {
        List<Film> sortedFilms = filmStorage.getFilms();
        sortedFilms.sort((Film o1, Film o2) -> o1.getLikedIDs().size() - o2.getLikedIDs().size());
        return new ArrayList<>(sortedFilms.subList(sortedFilms.size()-top, sortedFilms.size()));
    }

    public boolean isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank() ||
                film.getDescription() == null || film.getDescription().length() > 200 ||
                film.getReleaseDate() == null || film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE) ||
                film.getDuration() <= 0) {
            throw new ValidationException("Неверно введены данные о фильме.");
        }
        return true;
    }
}
