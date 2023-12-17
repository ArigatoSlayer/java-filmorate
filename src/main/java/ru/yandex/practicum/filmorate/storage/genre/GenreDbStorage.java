package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper mapper;

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM genre";
        log.info("Отправлены все жанры");
        return jdbcTemplate.query(sqlQuery, mapper);
    }

    @Override
    public Genre getById(int id) {
        String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!mpaRows.next()) {
            log.warn("Жанр {} не найден.", id);
            throw new NotFoundException("Рейтинг не найден");
        }

        return jdbcTemplate.queryForObject(sqlQuery, mapper, id);
    }
}
