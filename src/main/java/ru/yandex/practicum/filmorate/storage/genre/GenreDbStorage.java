package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM genre";
        log.info("Отправлены все жанры.");
        return jdbcTemplate.query(sqlQuery, genreMapper);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM genre " +
                "WHERE genre_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRows.next()) {
            log.warn("Жанр {} не найден.", id);
            throw new NotFoundException("Рейтинг не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, genreMapper, id);
    }

    @Override
    public Set<Genre> getFilmGenresFromDb(Integer filmId) {
        String query = "SELECT * FROM genre " +
                "INNER JOIN film_genre USING (genre_id) " +
                "WHERE film_genre.film_id = ? " +
                "ORDER BY genre_id;";
        TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        genres.addAll(jdbcTemplate.query(query, genreMapper, filmId));
        return genres;
    }

    @Override
    public void setFilmGenresToDb(Integer filmId, Set<Genre> genreSet) {
        jdbcTemplate.update("DELETE FROM film_genre " +
                "WHERE film_id = ?;", filmId);
        if (genreSet == null || genreSet.isEmpty()) {
            return;
        }
        genreSet.forEach(genre -> getGenreById(genre.getId()));
        List<Genre> genreList = new ArrayList<>(genreSet);
        jdbcTemplate.batchUpdate("INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES (?, ?);", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, filmId);
                preparedStatement.setInt(2, genreList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genreList.size();
            }
        });
    }

    @Override
    public List<Film> setGenresToFilmList(List<Film> filmList) {
        Map<Integer, Film> filmMap = new LinkedHashMap<>();
        for (Film film : filmList) {
            filmMap.put(film.getId(), film);
        }
        String questionMarks = String.join(", ", (Collections.nCopies(filmMap.size(), "?")));
        String query = String.format("SELECT * " +
                "FROM film_genre " +
                "INNER JOIN genre USING (genre_id) " +
                "WHERE film_id IN (%s);", questionMarks);
        jdbcTemplate.query(query, (resultSet, rowNum) -> {
            Film film = filmMap.get(resultSet.getInt("film_id"));
            film.addGenreToGenresSet(new Genre(resultSet.getInt("genre_id"), resultSet.getString("name")));
            return null;
        }, filmMap.keySet().toArray());
        return new LinkedList<>(filmMap.values());
    }
}
