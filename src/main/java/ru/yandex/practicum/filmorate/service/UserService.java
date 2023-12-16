package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User createUser(User user) {
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }


    public User getUserById(Integer id) {
        return storage.getUserById(id);
    }

    public void addToFriend(int userId, int friendId) {
        storage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        storage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends(int userId) {
        return storage.getFriends(userId);
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        return storage.getMutualFriends(userId, friendId);
    }
}
