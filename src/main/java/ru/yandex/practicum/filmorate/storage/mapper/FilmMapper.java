package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

@Component
public class FilmMapper implements RowMapper<Film> {
    final JdbcTemplate jdbcTemplate;
    final MpaMapper mpaMapper;

    @Autowired
    public FilmMapper(JdbcTemplate jdbcTemplate, MpaMapper mpaMapper, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaMapper = mpaMapper;

    }


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(findMpa(rs.getInt("mpa_id")))
                .genres(new TreeSet<>(Comparator.comparing(Genre::getId)))
                .directors(new HashSet<>())
                .build();
    }

    public Mpa findMpa(int ratingId) {
        final String mpaSql = "SELECT * " +
                "FROM mpa_ratings " +
                "WHERE mpa_id = ?;";
        return jdbcTemplate.queryForObject(mpaSql, mpaMapper, ratingId);
    }

}
