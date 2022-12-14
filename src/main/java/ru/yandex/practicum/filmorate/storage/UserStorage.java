package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);
    List<User> getUsers();
    void updateUser(User user);
    User findUser(Integer userID);
    void addFriend(Integer userID, Integer friendID);
    List<Integer> getFriends(Integer userID);
    void removeFriend(Integer userID, Integer friendID);
}
