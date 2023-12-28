package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public class ReviewDbStorage implements ReviewStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public Review getReviewById(Integer reviewId) {
        return null;
    }

    @Override
    public List<Review> getAllReviewsForAllFilms(Integer count) {
        return null;
    }

    @Override
    public List<Review> getAllReviewsForOneFilm(Integer filmId, Integer count) {
        return null;
    }

    @Override
    public Review postReview(Review review) {
        return null;
    }

    @Override
    public Review putReview(Review review) {
        return null;
    }

    @Override
    public Review putLikeToReview(Integer reviewId, Integer userId) {
        return null;
    }

    @Override
    public Review putDislikeToReview(Integer reviewId, Integer userId) {
        return null;
    }

    @Override
    public Review deleteLikeFromReview(Integer reviewId, Integer userId) {
        return null;
    }

    @Override
    public Review deleteDislikeFromReview(Integer reviewId, Integer userId) {
        return null;
    }

    @Override
    public void deleteReviewById(Integer reviewId) {

    }
}
