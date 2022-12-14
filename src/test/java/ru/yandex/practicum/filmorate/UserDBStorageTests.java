package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDBStorageTests {

    private final UserDbStorage userStorage;

    @Test
    public void shouldCreateUserTest() {
        User user = new User("email@email.com", "login", "name", LocalDate.now());
        User newUser = userStorage.addUser(user);
        assertEquals(1, newUser.getId());
        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getEmail(), newUser.getEmail());
        assertEquals(user.getLogin(), newUser.getLogin());
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User("email@email.com", "login", "name", LocalDate.now());
        User newUser = userStorage.addUser(user);
        newUser.setName("name2");
        userStorage.updateUser(newUser);
        user = userStorage.findUser(newUser.getId());
        assertEquals(1, newUser.getId());
        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getEmail(), newUser.getEmail());
        assertEquals(user.getLogin(), newUser.getLogin());
    }

    @Test
    public void shouldReturnEmptyUserList() {
        List<User> users = userStorage.getUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void shouldReturnUserByID() {
        User user = new User("email@email.com", "login", "name", LocalDate.now());
        User user2 = userStorage.addUser(user);
        User user3 = userStorage.findUser(user2.getId());
        assertEquals(user2.getId(), user3.getId());
        assertEquals(user2.getName(), user3.getName());
        assertEquals(user2.getEmail(), user3.getEmail());
        assertEquals(user2.getLogin(), user3.getLogin());
    }

    @Test
    public void shouldReturnUserList() {
        User user1 = new User("1email@email.com", "login1", "name1", LocalDate.now());
        User user2 = userStorage.addUser(user1);
        User user3 = new User("3email@email.com", "login3", "name3", LocalDate.now());
        User user4 = userStorage.addUser(user3);
        assertEquals(2, userStorage.getUsers().size());
    }

    @Test
    public void shouldMakeFriend() {
        User user1 = new User("1email@email.com", "login1", "name1", LocalDate.now());
        User user2 = userStorage.addUser(user1);
        User user3 = new User("3email@email.com", "login3", "name3", LocalDate.now());
        User user4 = userStorage.addUser(user3);
        userStorage.addFriend(user2.getId(), user4.getId());
        assertEquals(1, userStorage.getFriends(user2.getId()).size());
    }

    @Test
    public void shouldRemoveFriend() {
        User user1 = new User("1email@email.com", "login1", "name1", LocalDate.now());
        User user2 = userStorage.addUser(user1);
        User user3 = new User("3email@email.com", "login3", "name3", LocalDate.now());
        User user4 = userStorage.addUser(user3);
        userStorage.addFriend(user2.getId(), user4.getId());
        assertEquals(1, userStorage.getFriends(user2.getId()).size());
        userStorage.removeFriend(user2.getId(), user4.getId());
        assertEquals(0, userStorage.getFriends(user2.getId()).size());
    }
}
