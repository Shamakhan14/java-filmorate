package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendStorage {
    void addFriend(Integer userID, Integer friendID);
    List<Integer> getFriends(Integer userID);
    void removeFriend(Integer userID, Integer friendID);
}
