package ru.yandex.practicum.filmorate.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenre(Integer id) {
        Genre genre = genreStorage.getGenre(id);
        if (genre == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        return genre;
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }
}
