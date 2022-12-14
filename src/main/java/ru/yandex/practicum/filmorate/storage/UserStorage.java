package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);
    List<User> getUsers();
    void updateUser(User user);
    User findUser(Integer userID);
}
