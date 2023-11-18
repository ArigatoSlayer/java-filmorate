package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User createUser(User user) {
        if (isValidUser(user)) {
            user.setFriends(new HashSet<>());
            log.info(user.getName() + "FriendsList not null " + user.getFriends());
            if (StringUtils.isBlank(user.getName())) {
                user.setName(user.getLogin());
            }
            user.setId(id);
            users.put(id, user);
            id++;
            log.info("User created" + user.getLogin() + "   " + user.getId());
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            if (user.getFriends() == null) {
                user.setFriends(users.get(user.getId()).getFriends());
            }
            users.put(user.getId(), user);
            log.info("UserId: " + user.getId() + " update");
        } else {
            log.error("Не удалось обновить " + user.getId() + "  id не найден");
            throw new RuntimeException("Id " + user.getId() + " не существует");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь c id: " + id + " не найден");
        }
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
