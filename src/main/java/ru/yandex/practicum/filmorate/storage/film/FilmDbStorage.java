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
        String query = "UPDATE film SET " +
                "name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ? " +
                "WHERE film_id = ?;";
        int updatedRowCount = jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (updatedRowCount == 0) {
            throw new NotFoundException("Фильм с идентификатором " + film.getId() + " не найден.");
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_id", film.getMpa().getId());
        film.setId((int) simpleJdbcInsert.executeAndReturnKey(parameters));
        return film;
    }

    @Override
    public List<Film> getFilms() {
        final String sql = "SELECT film.*, mpa_ratings.mpa_name " +
                "FROM film " +
                "INNER JOIN mpa_ratings USING (mpa_id);";
        log.info("Отправлены все фильмы");
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public Film getFilmById(int filmId) {
        final String sql = "SELECT film.*, mpa_ratings.mpa_name " +
                "FROM film " +
                "INNER JOIN mpa_ratings USING (mpa_id)" +
                "WHERE film_id = ?;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, filmId);
        if (!filmRows.next()) {
            log.warn("Фильм с идентификатором {} не найден.", filmId);
            throw new NotFoundException("Фильм с идентификатором " + filmId + " не найден.");
        } else {
            log.info("Отправлен фильм с идентификатором {} ", filmId);
            return jdbcTemplate.queryForObject(sql, filmMapper, filmId);
        }
    }

    @Override
    public Film addLike(int filmId, int userId) {
        validateFilm(filmId);
        validateUser(userId);
        final String sqlQuery = "INSERT INTO LIKES (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);

        log.info("Пользователь {} поставил лайк к фильму {}", userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        validateFilm(filmId);
        validateUser(userId);
        final String sqlQuery = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);

        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public void deleteFilm(int id) {
        final String query = "DELETE FROM film WHERE film_id = ?";
        if (jdbcTemplate.update(query, id) == 0) {
            throw new NotFoundException("Фильм с идентификатором " + id + " не найден.");
        } else {
            log.info("Удален фильм с id: {}", id);
        }
    }

    @Override
    public List<Film> getListTopFilmsByCount(Integer count) {
        String sqlQuery = "SELECT film.*, mpa_ratings.mpa_name, COUNT(l.film_id) as count " +
                "FROM film " +
                "LEFT JOIN LIKES AS l ON film.film_id=l.film_id " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "GROUP BY film.film_id " +
                "ORDER BY count DESC " +
                "LIMIT ?";
        log.info("Отправлен топ {} фильмов", count);
        return jdbcTemplate.query(sqlQuery, filmMapper, count);
    }

    @Override
    public List<Film> getListTopFilmsByYear(Integer year) {
        String sql = "SELECT film.*, mpa_ratings.mpa_name, COUNT(l.film_id) as count " +
                "FROM film " +
                "LEFT JOIN LIKES AS l ON film.film_id=l.film_id " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "WHERE EXTRACT(YEAR FROM release_date) = ? " +
                "GROUP BY film.film_id";
        log.info("Отправлен топ фильмов {} года", year);
        return jdbcTemplate.query(sql, filmMapper, year);
    }

    @Override
    public List<Film> getListOfTopFilmsByGenre(Integer genreId) {
        String sql = "SELECT film.*, mpa_ratings.mpa_name, COUNT(l.film_id) AS count " +
                "FROM film " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "LEFT JOIN LIKES AS l ON film.film_id = l.film_id " +
                "RIGHT JOIN film_genre AS fg ON film.film_id = fg.film_id " +
                "WHERE fg.genre_id = ? " +
                "GROUP BY film.film_id";
        log.info("Отправлен топ фильмов с идентификатором жанра {}", genreId);
        return jdbcTemplate.query(sql, filmMapper, genreId);
    }

    @Override
    public List<Film> getListTopFilmsByGenreAndYear(Integer year, Integer genreId) {
        String sql = "SELECT film.*, mpa_ratings.mpa_name, " +
                "COUNT(l.film_id) as count_likes " +
                "FROM film " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "LEFT JOIN LIKES AS l ON film.film_id = l.film_id " +
                "RIGHT JOIN film_genre AS fg ON film.film_id = fg.film_id " +
                "WHERE EXTRACT(YEAR FROM film.release_date) = ? " +
                "AND fg.genre_id = ? " +
                "GROUP BY film.film_id";
        log.info("Отправлен топ фильмов {} года с идентификатором жанра {}", year, genreId);
        return jdbcTemplate.query(sql, filmMapper, year, genreId);
    }

    @Override
    public List<Film> getAllDirectorFilmsOrderByLikes(int directorId) {
        String sqlQuery = "SELECT film.*, mpa_ratings.mpa_name, COUNT(LIKES.film_id) " +
                "FROM film " +
                "INNER JOIN film_directors USING (film_id) " +
                "LEFT JOIN LIKES USING (film_id) " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "WHERE film_directors.director_id = ? " +
                "GROUP BY film.film_id " +
                "ORDER BY COUNT(LIKES.film_id) DESC;";
        return jdbcTemplate.query(sqlQuery, filmMapper, directorId);
    }

    @Override
    public List<Film> getAllDirectorFilmsOrderByYear(int directorId) {
        String sqlQuery = "SELECT film.*, mpa_ratings.mpa_name " +
                "FROM film " +
                "INNER JOIN film_directors USING (film_id) " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "WHERE director_id = ? " +
                "ORDER BY release_date;";
        return jdbcTemplate.query(sqlQuery, filmMapper, directorId);
    }

    @Override
    public List<Film> searchBySubstring(String str) {
        String sql = "SELECT film.*, mpa_ratings.mpa_name, " +
                "(SELECT COUNT(l.film_id) " +
                "FROM LIKES AS l " +
                "WHERE film.film_id = l.film_id) as count " +
                "FROM film " +
                "LEFT JOIN film_directors AS fd ON fd.film_id = film.film_id " +
                "LEFT JOIN directors AS d ON d.director_id = fd.director_id " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "WHERE LOWER(film.name) LIKE (?) OR LOWER(d.name) LIKE (?) " +
                "ORDER BY count DESC";
        String searchStr = "%" + str.toLowerCase() + "%";
        log.info("Отправлен список фильмов содержащий в названии или в имени режиссёра подстроку {}", str);
        return jdbcTemplate.query(sql, filmMapper, searchStr, searchStr);
    }


    @Override
    public List<Film> searchBySubstringByDirectors(String str) {
        String sql = "SELECT film.*, mpa_ratings.mpa_name, " +
                "(SELECT COUNT(l.film_id) " +
                "FROM LIKES AS l " +
                "WHERE film.film_id = l.film_id) as count " +
                "FROM film " +
                "JOIN film_directors AS fd ON fd.film_id = film.film_id " +
                "JOIN directors AS d ON d.director_id = fd.director_id " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "WHERE LOWER(d.name) LIKE (?) " +
                "ORDER BY count DESC";
        String searchStr = "%" + str.toLowerCase() + "%";
        log.info("Отправлен список фильмов содержащий в имени режиссёра подстроку {}", str);
        return jdbcTemplate.query(sql, filmMapper, searchStr);
    }

    @Override
    public List<Film> searchBySubstringByFilms(String str) {
        String sql = "SELECT f.*, mpa_ratings.mpa_name, " +
                "(SELECT COUNT(l.film_id) " +
                "FROM LIKES AS l " +
                "WHERE f.film_id = l.film_id) as count " +
                "FROM film as f " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "WHERE LOWER(f.name) LIKE (?) " +
                "ORDER BY count DESC";
        String searchStr = "%" + str.toLowerCase() + "%";
        log.info("Отправлен список фильмов содержащий в названии подстроку {}", str);
        return jdbcTemplate.query(sql, filmMapper, searchStr);
    }

    public List<Film> getListCommonFilms(Integer userId, Integer friendId) {
        validateUser(userId);
        validateUser(friendId);

        String sqlQuery = "SELECT film.*, mpa_ratings.mpa_name, COUNT(LIKES.film_id) " +
                "FROM film " +
                "LEFT JOIN LIKES USING (film_id) " +
                "INNER JOIN mpa_ratings USING (mpa_id) " +
                "WHERE film.film_id IN ( " +
                "SELECT LIKES.film_id " +
                "FROM LIKES " +
                "WHERE LIKES.user_id = ? " +
                "INTERSECT " +
                "SELECT LIKES.film_id " +
                "FROM LIKES " +
                "WHERE LIKES.user_id = ?) " +
                "GROUP BY film.film_id " +
                "ORDER BY COUNT(LIKES.film_id) DESC;";

        log.info("Отправлен список общих фильмов.");

        return jdbcTemplate.query(sqlQuery, filmMapper, userId, friendId);
    }

    public Set<Integer> getLikesForCurrentFilm(int filmId) {
        final String sqlQuery = "SELECT user_id FROM LIKES WHERE film_id = ?";
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        Set<Integer> likes = new HashSet<>();
        while (likesRows.next()) {
            likes.add(likesRows.getInt("user_id"));
        }
        log.info("Количество лайков фильму {}", likes.size());
        return likes;
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
            fields.put("mpa_id", film.getMpa().getId());
        }
        return fields;
    }

}
