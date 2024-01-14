package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final FeedMapper feedMapper;


    @Override
    public List<User> getAllUsers() {
        final String sql = "SELECT * FROM USERS";
        log.info("Получение пользователей");
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public User createUser(User user) {
        final String sql = "INSERT INTO USERS (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            user.setId((Integer) keyHolder.getKey());
        }

        log.info("Создан пользователь с индентификатором {} ", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        isExist(user.getId());
        final String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        log.info("Обновлен пользователь с индентификатором {} ", user.getId());
        return user;
    }

    @Override
    public User getUserById(int userId) {
        isExist(userId);
        final String sql = "SELECT * FROM users WHERE user_id = ?";
        log.info("Отправлен пользователь с индентификатором {} ", userId);
        return jdbcTemplate.queryForObject(sql, userMapper, userId);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        isExist(userId);
        isExist(friendId);
        final String sqlQuery = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        final String checkQuery = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, friendId, userId);

        if (!userRows.next()) {
            jdbcTemplate.update(sqlQuery, userId, friendId, false);
            log.info("Пользователь {} отправил запрос на добавления в друзья {}", userId, friendId);
        } else {
            jdbcTemplate.update(sqlQuery, userId, friendId, true);
            jdbcTemplate.update(sqlQuery, friendId, userId, true);
            log.info("Пользователь {} добавил в друзья {}", userId, friendId);
        }
        addFeed(userId, 2, friendId);
        return getUserById(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        final String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь {} удалил из друзей {}", userId, friendId);
        addFeed(userId, 1, friendId);
        //addFeed(friendId, 1, userId);
    }

    @Override
    public List<User> getFriends(int userId) {
        isExist(userId);
        final String sqlQuery = "SELECT u.* FROM users AS u " +
                "LEFT JOIN friends AS f ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ?";
        log.info("Запрос получения списка друзей пользователя {} выполнен", userId);
        return jdbcTemplate.query(sqlQuery, userMapper, userId);
    }

    @Override
    public List<User> getMutualFriends(int userId, int secondUserId) {
        isExist(userId);
        isExist(secondUserId);
        final String sqlQuery = "SELECT u.* FROM friends AS f " +
                "LEFT JOIN users u ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ? " +
                "AND f.friend_id IN " +
                "( " +
                "SELECT f.friend_id " +
                "FROM friends AS f " +
                "LEFT JOIN users AS u ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ?" +
                ")";
        log.info("Отправлен одинаковые друзья пользователей {} и {} ", userId, secondUserId);
        return jdbcTemplate.query(sqlQuery, userMapper, userId, secondUserId);
    }

    @Override
    public List<Feed> getFeedById(int id) {
        isExist(id);
        final String sqlQuery = "SELECT f.timestamp_event, " +
                "f.user_id, " +
                "t.type_name, " +
                "o.operation_name, " +
                "f.event_id, " +
                "f.entity_id " +
                "FROM feed AS f " +
                "LEFT JOIN event_type AS t ON f.event_type_id = t.type_id " +
                "LEFT JOIN type_operation AS o ON f.type_operation_id = o.operation_id " +
                "WHERE f.user_id = ?;";
        log.info("Отправлен запрос на ленту событий пользователя {}", id);
        return jdbcTemplate.query(sqlQuery, feedMapper, id);
    }

    private void isExist(Integer userId) {
        final String checkUserQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkUserQuery, userId);

        if (!userRows.next()) {
            log.warn("Пользователь с идентификатором {} не найден.", userId);
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден.");
        }
    }

    private void addFeed(int userId, int operationId, int entityId) {
        String sql = "INSERT INTO feed (user_id, event_type_id, type_operation_id, entity_id) " +
                "VALUES (?, 3, ?, ?)";
        jdbcTemplate.update(sql, userId, operationId, entityId);
    }
}
