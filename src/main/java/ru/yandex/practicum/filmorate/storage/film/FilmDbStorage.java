package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.util.*;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    @Override
    public Film updateFilm(Film film) {
        final String sql = "SELECT * FROM film WHERE film_id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, film.getId());

        if (!filmRows.next()) {
            log.warn("Фильм с идентификатором {} не найден.", film.getId());
            throw new NotFoundException("Фильм с идентификатором " + film.getId() + " не найден.");
        }

        final String sqlQuery = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ? " +
                "WHERE film_id = ?";

        if (film.getMpa() != null) {
            final String updateMpa = "UPDATE film SET rating_id = ? WHERE film_id = ?";
            jdbcTemplate.update(updateMpa, film.getMpa().getId(), film.getId());
        }

        if (film.getGenres() != null) {
            final String deleteGenresQuery = "DELETE FROM film_genre WHERE film_id = ?";
            final String updateGenresQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            final String sqlCheck = "SELECT * FROM film_genre WHERE film_id = ? AND genre_id = ?";

            jdbcTemplate.update(deleteGenresQuery, film.getId());
            for (Genre g : film.getGenres()) {
                SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlCheck, film.getId(), g.getId());
                if (!genreRows.next()) {
                    jdbcTemplate.update(updateGenresQuery, film.getId(), g.getId());
                }
            }
        }

        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getId());
        log.info("Обновлен фильм с индентификатором {} ", film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        Number key = jdbcInsert.withTableName("film")
                .usingGeneratedKeyColumns("film_id")
                .executeAndReturnKey(getFilmFields(film));
        film.setId(key.intValue());

        if (film.getGenres() != null) {
            final String updateGenresQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            final String sqlCheck = "SELECT * FROM film_genre WHERE film_id = ? AND genre_id = ?";
            for (Genre g : film.getGenres()) {
                SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlCheck, film.getId(), g.getId());
                if (!genreRows.next()) {
                    jdbcTemplate.update(updateGenresQuery, film.getId(), g.getId());
                }
            }
        }
        log.info("Создан фильм с индентификатором {} ", film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getFilms() {
        final String sql = "SELECT * FROM film";
        log.info("Отправлены все фильмы");
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public Film getFilmById(int filmId) {
        final String sql = "SELECT * FROM film WHERE film_id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, filmId);

        if (!filmRows.next()) {
            log.warn("Фильм с идентификатором {} не найден.", filmId);
            throw new NotFoundException("Фильм с идентификатором " + filmId + " не найден.");
        } else {
            log.info("Отправлен фильм с индентификатором {} ", filmId);
            return jdbcTemplate.queryForObject(sql, filmMapper, filmId);
        }
    }

    @Override
    public Film addLike(int filmId, int userId) {
        validateFilm(filmId);
        validateUser(userId);
        final String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);

        log.info("Пользователь {} поставил лайк к фильму {}", userId, filmId);
        addFeed(userId, 2, filmId);
        return getFilmById(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        validateFilm(filmId);
        validateUser(userId);
        final String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);

        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);
        addFeed(userId, 1, filmId);
        return getFilmById(filmId);
    }

    @Override
    public List<Film> getListOfTopFilms(int count) {
        String sqlQuery = "SELECT film.*, COUNT(l.film_id) as count FROM film " +
                "LEFT JOIN likes AS l ON film.film_id=l.film_id " +
                "GROUP BY film.film_id " +
                "ORDER BY count DESC " +
                "LIMIT ?";
        log.info("Отправлен топ {} фильмов", count);
        return jdbcTemplate.query(sqlQuery, filmMapper, count);
    }

    @Override
    public List<Film> getAllDirectorFilmsOrderByLikes(int directorId) {
        String sqlQuery = "SELECT film.*, COUNT(likes.film_id) " +
                "FROM film " +
                "INNER JOIN film_directors USING (film_id) " +
                "LEFT JOIN likes USING (film_id) " +
                "WHERE film_directors.director_id = ? " +
                "GROUP BY film.film_id " +
                "ORDER BY COUNT(likes.film_id) DESC;";
        return jdbcTemplate.query(sqlQuery, filmMapper, directorId);
    }

    @Override
    public List<Film> getAllDirectorFilmsOrderByYear(int directorId) {
        String sqlQuery = "SELECT * " +
                "FROM film " +
                "INNER JOIN film_directors USING (film_id) " +
                "WHERE director_id = ? " +
                "ORDER BY release_date;";
        return jdbcTemplate.query(sqlQuery, filmMapper, directorId);
    }

    @Override
    public List<Film> getListCommonFilms(Integer userId, Integer friendId) {
        validateUser(userId);
        validateUser(friendId);

        String sqlQuery = "SELECT film.*, COUNT(likes.film_id) " +
                "FROM film " +
                "LEFT JOIN likes USING (film_id)" +
                "WHERE film.film_id IN ( " +
                    "SELECT likes.film_id " +
                    "FROM likes " +
                    "WHERE likes.user_id = ? " +
                    "INTERSECT " +
                    "SELECT likes.film_id " +
                    "FROM likes " +
                    "WHERE likes.user_id = ?) " +
                "GROUP BY film.film_id " +
                "ORDER BY COUNT(likes.film_id) DESC;";

        log.info("Отправлен список общих фильмов.");

        return jdbcTemplate.query(sqlQuery, filmMapper, userId, friendId);
    }

    public Set<Integer> getLikesForCurrentFilm(int filmId) {
        final String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        Set<Integer> likes = new HashSet<>();
        while (likesRows.next()) {
            likes.add(likesRows.getInt("user_id"));
        }
        log.info("Количество лайков фильму {}", likes.size());
        return likes;
    }

    private void isExist(int id) {
        final String checkUserQuery = "SELECT * FROM film WHERE film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkUserQuery, id);
        if (!userRows.next()) {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    private void validateUser(int userId) {
        final String checkUserQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkUserQuery, userId);

        if (!userRows.next()) {
            log.warn("Пользователь {} не найден.", userId);
            throw new NotFoundException("Фильм или пользователь не найдены");
        }
    }

    private void validateFilm(int filmId) {
        final String checkFilmQuery = "SELECT * FROM film WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkFilmQuery, filmId);

        if (!filmRows.next()) {
            log.warn("Фильм {} не найден.", filmId);
            throw new NotFoundException("Фильм или пользователь не найдены");
        }
    }

    private Map<String, Object> getFilmFields(Film film) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("NAME", film.getName());
        fields.put("DESCRIPTION", film.getDescription());
        fields.put("DURATION", film.getDuration());
        fields.put("RELEASE_DATE", film.getReleaseDate());
        if (film.getMpa() != null) {
            fields.put("RATING_ID", film.getMpa().getId());
        }
        return fields;
    }

    private void addFeed(int userId, int operationId, int entityId) {
        String sql = "INSERT INTO feed (user_id, event_type_id, type_operation_id, entity_id) " +
                "VALUES (?, 1, ?, ?)";
        jdbcTemplate.update(sql, userId, operationId, entityId);
    }
}
