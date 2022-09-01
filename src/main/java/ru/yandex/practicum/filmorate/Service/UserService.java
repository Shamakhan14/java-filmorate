package ru.yandex.practicum.filmorate.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addUser(User user) {
        userStorage.addUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public User findUser(Integer userID) {
        return userStorage.findUser(userID);
    }

    public void addFriend(Integer userID, Integer friendID) {
        User user = userStorage.findUser(userID);
        User friend = userStorage.findUser(friendID);
        user.addFriend(friendID);
        friend.addFriend(userID);
    }

    public void removeFriend(Integer userID, Integer friendID) {
        User user = userStorage.findUser(userID);
        User friend = userStorage.findUser(friendID);
        user.removeFriend(friendID);
        friend.removeFriend(userID);
    }

    public List<User> getFriends(Integer userID) {
        List<User> friends = new ArrayList<>();
        for (Integer integer: userStorage.findUser(userID).getFriends()) {
            friends.add(userStorage.findUser(integer));
        }
        return friends;
    }

    public List<User> getMutualFriends(Integer userID, Integer friendID) {
        User user = userStorage.findUser(userID);
        User friend = userStorage.findUser(friendID);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> friendFriends = friend.getFriends();
        List<User> result = new ArrayList<>();
        if (userFriends == null || friendFriends == null) return result;
        for (Integer ID: userFriends) {
            if (friendFriends.contains(ID)) {
                result.add(userStorage.findUser(ID));
            }
        }
        return result;
    }

    public boolean isValid(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ") ||
                user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверно введены данные пользователя.");
        }
        return true;
    }
}
