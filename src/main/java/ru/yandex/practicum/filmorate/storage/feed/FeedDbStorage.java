package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFeed(int userId, int type, int operationId, int entityId) {
        String sql = "INSERT INTO feed (user_id, event_type_id, type_operation_id, entity_id) " +
                "VALUES (?, ?, ?, ?)";
        int updatedRowCount = jdbcTemplate.update(sql, userId, type, operationId, entityId);
        if (updatedRowCount == 0) {
            throw new NotFoundException("Произошла ошибка при добавлении действия в ленту событий");
        }
    }
}