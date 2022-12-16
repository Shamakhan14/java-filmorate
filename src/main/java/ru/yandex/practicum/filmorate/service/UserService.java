package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void updateUser(User user) {
        User newUser = findUser(user.getId());
        userStorage.updateUser(user);
    }

    public User findUser(Integer userID) {
        if (userStorage.findUser(userID) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        return userStorage.findUser(userID);
    }

    public void addFriend(Integer userID, Integer friendID) {
        User user = findUser(userID);
        User friend = findUser(friendID);
        friendStorage.addFriend(userID, friendID);
    }

    public void removeFriend(Integer userID, Integer friendID) {
        if (!friendStorage.getFriends(userID).contains(friendID)) {
            throw new ObjectNotFoundException("Данные пользователи не являются друзьями.");
        }
        friendStorage.removeFriend(userID, friendID);
    }

    public List<User> getFriends(Integer userID) {
        if (findUser(userID) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        return friendStorage.getFriends(userID).stream()
                .map(this::findUser)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Integer userID, Integer friendID) {
        return friendStorage.getFriends(userID).stream()
                .filter(friendStorage.getFriends(friendID)::contains)
                .map(this::findUser)
                .collect(Collectors.toList());
    }
}
