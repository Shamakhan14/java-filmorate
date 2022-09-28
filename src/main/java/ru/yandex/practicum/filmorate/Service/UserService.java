package ru.yandex.practicum.filmorate.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addUser(User user) {
        userStorage.addUser(user);
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
        user.addFriend(friendID);
        friend.addFriend(userID);
    }

    public void removeFriend(Integer userID, Integer friendID) {
        if (!findUser(userID).getFriends().contains(friendID)) {
            throw new ObjectNotFoundException("Данные пользовтаели не являются друзьями.");
        }
        User user = findUser(userID);
        User friend = findUser(friendID);
        user.removeFriend(friendID);
        friend.removeFriend(userID);
    }

    public List<User> getFriends(Integer userID) {
        if (findUser(userID) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        return findUser(userID).getFriends().stream()
                .map(this::findUser)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Integer userID, Integer friendID) {
        return findUser(userID).getFriends().stream()
                .filter(findUser(friendID).getFriends()::contains)
                .map(this::findUser)
                .collect(Collectors.toList());
    }
}
