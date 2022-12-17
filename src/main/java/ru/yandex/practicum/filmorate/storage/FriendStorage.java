package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(Integer userID, Integer friendID);
    List<User> getFriends(Integer userID);
    void removeFriend(Integer userID, Integer friendID);
    List<User> getMutualFriends(Integer userID, Integer friendID);
}
