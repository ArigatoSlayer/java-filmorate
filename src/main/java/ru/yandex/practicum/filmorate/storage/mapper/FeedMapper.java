package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedMapper implements RowMapper<Feed> {

    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date timestamp = rs.getDate("timestamp_event");

        return Feed.builder()
                .timestamp(timestamp.getTime())
                .userId(rs.getInt("user_id"))
                .eventType(rs.getString("type_name"))
                .operation(rs.getString("operation_name"))
                .eventId(rs.getInt("event_id"))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
