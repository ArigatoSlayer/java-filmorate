package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;

    public Review getReviewById(Integer reviewId) {
        return reviewStorage.getReviewById(reviewId);
    }

    public List<Review> getAllReviews(Integer filmId, Integer count) {
        if (filmId == 0) {
            return reviewStorage.getAllReviewsForAllFilms(count);
        } else {
            return reviewStorage.getAllReviewsForOneFilm(filmId, count);
        }
    }

    public Review postReview(Review review) {
        return reviewStorage.postReview(review);
    }

    public Review putReview(Review review) {
        return reviewStorage.putReview(review);
    }

    public Review putLikeToReview(Integer reviewId, Integer userId) {
        return reviewStorage.putLikeToReview(reviewId, userId);
    }

    public Review putDislikeToReview(Integer reviewId, Integer userId) {
        return reviewStorage.putDislikeToReview(reviewId, userId);
    }

    public Review deleteLikeFromReview(Integer reviewId, Integer userId) {
        return reviewStorage.deleteLikeFromReview(reviewId, userId);
    }

    public Review deleteDislikeFromReview(Integer reviewId, Integer userId) {
        return reviewStorage.deleteDislikeFromReview(reviewId, userId);
    }

    public void deleteReviewById(Integer reviewId) {
        reviewStorage.deleteReviewById(reviewId);
    }

}
