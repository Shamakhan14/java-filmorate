package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private HashSet<Integer> likedIDs;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likedIDs = new HashSet<>();
    }

    public void addLike(Integer userID) {
        likedIDs.add(userID);
    }

    public void removeLike(Integer userID) {
        likedIDs.remove(userID);
    }
}
