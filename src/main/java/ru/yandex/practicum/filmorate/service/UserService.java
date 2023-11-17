package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        return storage.getAllUsers().get(id - 1);
    }

    public User addToFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User user2 = getUserById(friendId);
        user.getFriends().add(user2.getId());
        user2.getFriends().add(user.getId());
        return getUserById(userId);
    }

    public User deleteFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User user2 = getUserById(friendId);
        user.getFriends().remove(friendId);
        user2.getFriends().remove(userId);
        return getUserById(userId);
    }

    public List<User> getAllFriends(int userId) {
        List<User> userList = new ArrayList<>();
        for (Integer friend : getUserById(userId).getFriends()) {
            userList.add(getUserById(friend));
        }
        return userList;
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId){
        List<User> mutualFriends = new ArrayList<>();
        for (Integer id :getUserById(userId).getFriends()) {
            if (getUserById(friendId).getFriends().contains(id)) {
                mutualFriends.add(getUserById(id));
            }
        }
        return mutualFriends;
    }
}