package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {
    void addLike(Integer filmID, Integer userID);
    List<Integer> getLikedIDs(Integer filmID);
    void removeLike(Integer filmID, Integer userID);
}
