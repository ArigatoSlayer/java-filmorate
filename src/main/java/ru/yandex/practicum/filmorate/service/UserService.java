package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;

    private final GenreStorage genreStorage;

    @Autowired
    public UserService(UserStorage storage, FeedStorage feedStorage, GenreStorage genreStorage) {
        this.userStorage = storage;
        this.feedStorage = feedStorage;
        this.genreStorage = genreStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public void addToFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        feedStorage.addFeed(userId, 3, 2, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
        feedStorage.addFeed(userId, 3, 1, friendId);
    }

    public List<User> getAllFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        return userStorage.getMutualFriends(userId, friendId);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public List<Feed> getFeedById(int id) {
        return userStorage.getFeedById(id);
    }

    public List<Film> getRecommendation(int id) {
        return genreStorage.setGenresToFilmList(userStorage.getRecommendation(id));
    }

}
