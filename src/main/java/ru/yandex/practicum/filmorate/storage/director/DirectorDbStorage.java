package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.DirectorMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    @Override
    public List<Director> getAllDirectors() {
        final String sqlQuery = "SELECT * FROM directors;";
        log.info("Отправлены все режиссеры.");
        return jdbcTemplate.query(sqlQuery, directorMapper);
    }

    @Override
    public Director getDirectorById(int directorId) {
        final String sqlQuery = "SELECT * FROM directors " +
                "WHERE director_id = ?;";
        try {
            log.info("Отправлен режиссер с индентификатором {}.", directorId);
            return jdbcTemplate.queryForObject(sqlQuery, directorMapper, directorId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режиссер с идентификатором " + directorId + " не найден.");
        }
    }

    @Override
    public Director postDirector(Director director) {
        Number returnedKey = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id")
                .executeAndReturnKey(Map.of("name", director.getName()));
        director.setId((int) returnedKey);
        log.info("Создан режиссер с индентификатором {}.", director.getId());
        return director;
    }

    @Override
    public Director putDirector(Director director) {
        final String sqlQuery = "UPDATE directors SET name = ? " +
                "WHERE director_id = ?;";
        int updatedRowCount = jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        if (updatedRowCount == 0) {
            throw new NotFoundException("Режиссер с идентификатором " + director.getId() + " не найден.");
        }
        log.info("Обновлен режиссер с индентификатором {}.", director.getId());
        return director;
    }

    @Override
    public void deleteDirectorById(int directorId) {
        final String sqlQuery = "DELETE directors " +
                "WHERE director_id = ?;";
        int updatedRowCount = jdbcTemplate.update(sqlQuery, directorId);
        if (updatedRowCount == 0) {
            throw new NotFoundException("Режиссер с идентификатором " + directorId + " не найден.");
        }
        log.info("Удален режиссер с индентификатором {}.", directorId);
    }

    @Override
    public void setFilmDirectorsToDb(Integer filmId, Set<Director> directorSet) {
        jdbcTemplate.update("DELETE FROM film_directors " +
                "WHERE film_id = ?;", filmId);
        if (directorSet == null || directorSet.isEmpty()) {
            return;
        }
        List<Director> directorList = new ArrayList<>(directorSet);
        jdbcTemplate.batchUpdate("INSERT INTO film_directors (film_id, director_id) " +
                "VALUES (?, ?);", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, filmId);
                preparedStatement.setInt(2, directorList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return directorSet.size();
            }
        });
    }

    @Override
    public Set<Director> getFilmDirectorsFromDb(Integer filmId) {
        String sqlQuery = "SELECT * FROM directors " +
                "INNER JOIN film_directors USING (director_id) " +
                "WHERE film_directors.film_id = ?;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, directorMapper, filmId));
    }

    @Override
    public List<Film> setDirectorsToFilmList(List<Film> filmList) {
        Map<Integer, Film> filmMap = new LinkedHashMap<>();
        for (Film film : filmList) {
            filmMap.put(film.getId(), film);
        }
        String questionMarks = String.join(", ", (Collections.nCopies(filmMap.size(), "?")));
        String query = String.format("SELECT * " +
                "FROM film_directors " +
                "INNER JOIN directors USING (director_id) " +
                "WHERE film_id IN (%s);", questionMarks);
        jdbcTemplate.query(query, (resultSet, rowNum) -> {
            Film film = filmMap.get(resultSet.getInt("film_id"));
            film.addDirectorToDirectorsSet(new Director(resultSet.getInt("director_id"),
                    resultSet.getString("name")));
            return null;
        }, filmMap.keySet().toArray());
        return new LinkedList<>(filmMap.values());
    }

}