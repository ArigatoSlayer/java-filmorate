package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
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
        if (!isValidUser(user)) {
            throw new RuntimeException("Введены неверные параметры");
        }
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        if (!isValidUser(user)) {
            throw new RuntimeException("Введены неверные параметры");
        }
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

    private boolean isValidUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email пустой или не содержит: @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин пустой или содержит пробел");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения установлена в будущем");
        }
        return true;
    }
}
