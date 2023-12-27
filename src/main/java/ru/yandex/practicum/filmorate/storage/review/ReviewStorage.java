package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review getReviewById(Integer reviewId);

    List<Review> getAllReviewsForAllFilms(Integer count);

    List<Review> getAllReviewsForOneFilm(Integer filmId, Integer count);

    Review postReview(Review review);

    Review putReview(Review review);

    Review putLikeToReview(Integer reviewId, Integer userId);

    Review putDislikeToReview(Integer reviewId, Integer userId);

    Review deleteLikeFromReview(Integer reviewId, Integer userId);

    Review deleteDislikeFromReview(Integer reviewId, Integer userId);

    void deleteReviewById(Integer reviewId);
}
