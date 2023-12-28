package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.mapper.ReviewMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewMapper reviewMapper;

    @Override
    public Review getReviewById(Integer reviewId) {
        final String sqlQuery = "SELECT * FROM reviews " +
                "WHERE review_id = ?;";
        try {
            log.info("Отправлен отзыв с индентификатором {} ", reviewId);
            return jdbcTemplate.queryForObject(sqlQuery, reviewMapper, reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Отзыв с идентификатором " + reviewId + " не найден.");
        }
    }

    @Override
    public List<Review> getAllReviewsForAllFilms(Integer count) {
        final String sqlQuery = "SELECT * " +
                "FROM reviews " +
                "ORDER BY useful DESC, review_id " +
                "LIMIT ?;";
        log.info("Отправлены все отзывы.");
        return jdbcTemplate.query(sqlQuery, reviewMapper, count);
    }

    @Override
    public List<Review> getAllReviewsForOneFilm(Integer filmId, Integer count) {
        final String sqlQuery = "SELECT * " +
                "FROM reviews " +
                "WHERE film_id = ? " +
                "ORDER BY useful DESC, review_id " +
                "LIMIT ?;";
        log.info("Отправлены все отзывы на фильм с индентификатором {}.", filmId);
        return jdbcTemplate.query(sqlQuery, reviewMapper, filmId, count);
    }

    @Override
    public Review postReview(Review review) {
        Number returnedKey = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id")
                .executeAndReturnKey(reviewToMap(review));
        review.setReviewId((int) returnedKey);
        if (null == review.getUseful()) {
            review.setUseful(0);
        }
        log.info("Создан отзыв с индентификатором {}.", review.getReviewId());
        return review;
    }

    @Override
    public Review putReview(Review review) {
        final String sqlQuery = "UPDATE reviews SET " +
                "content = ?, " +
                "is_positive = ? " +
                "WHERE review_id = ?;";
        int updatedRowCount = jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        if (updatedRowCount == 0) {
            throw new NotFoundException("Введены неверные данные при обновлении отзыва.");
        }
        log.info("Обновлен отзыв с индентификатором {}.", review.getReviewId());
        return getReviewById(review.getReviewId());
    }

    @Override
    public void incrementLikeToReview(Integer reviewId, Integer userId) {
        final String sqlQuery = "UPDATE reviews SET " +
                "useful = useful + 1 " +
                "WHERE review_id = ?;";
        int updatedRowCount = jdbcTemplate.update(sqlQuery, reviewId);
        if (updatedRowCount == 0) {
            throw new NotFoundException("Введены неверные данные при обновлении отзыва.");
        }
        log.info("Пользователь {} поставил лайк к фильму {}", userId, reviewId);
    }

    @Override
    public void decrementLikeToReview(Integer reviewId, Integer userId) {
        final String sqlQuery = "UPDATE reviews SET " +
                "useful = useful - 1 " +
                "WHERE review_id = ?;";
        int updatedRowCount = jdbcTemplate.update(sqlQuery, reviewId);
        if (updatedRowCount == 0) {
            throw new NotFoundException("Введены неверные данные при обновлении отзыва.");
        }
        log.info("Пользователь {} поставил лайк к фильму {}", userId, reviewId);
    }

    @Override
    public void deleteReviewById(Integer reviewId) {
        final String sqlQuery = "DELETE reviews " +
                "WHERE review_id = ?;";
        int updatedRowCount = jdbcTemplate.update(sqlQuery, reviewId);
        if (updatedRowCount == 0) {
            throw new NotFoundException("Отзыв с идентификатором " + reviewId + " не найден.");
        }
        log.info("Удален отзыв с индентификатором {}.", reviewId);
    }

    private Map<String, Object> reviewToMap(Review review) {
        Map<String, Object> map = new HashMap<>();
        map.put("content", review.getContent());
        map.put("is_positive", review.getIsPositive());
        map.put("user_id", review.getUserId());
        map.put("film_id", review.getFilmId());
        if (null == review.getUseful()) {
            map.put("useful", 0);
        } else {
            map.put("useful", review.getUseful());
        }
        return map;
    }
}
