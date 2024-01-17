package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    User addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getFriends(int userId);

    List<User> getMutualFriends(int userId, int secondUserId);

    List<Film> getRecommendation(int id);

    void deleteUser(int id);

    List<Feed> getFeedById(int id);

}
