package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @NotNull
    private Rating mpa;
    private LinkedHashSet<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration, Rating mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        genres = new LinkedHashSet<>();
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Rating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        genres = new LinkedHashSet<>();
    }
}
