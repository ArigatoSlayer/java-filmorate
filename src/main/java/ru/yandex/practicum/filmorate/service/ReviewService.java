package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Review getReviewById(Integer reviewId) {
        return reviewStorage.getReviewById(reviewId);
    }

    public List<Review> getAllReviews(Integer filmId, Integer count) {
        if (filmId == 0) {
            return reviewStorage.getAllReviewsForAllFilms(count);
        } else {
            filmStorage.getFilmById(filmId);
            return reviewStorage.getAllReviewsForOneFilm(filmId, count);
        }
    }

    public Review postReview(Review review) {
        filmStorage.getFilmById(review.getFilmId());
        userStorage.getUserById(review.getUserId());
        return reviewStorage.postReview(review);
    }

    public Review putReview(Review review) {
        return reviewStorage.putReview(review);
    }

    public void putLikeToReview(Integer reviewId, Integer userId) {
        reviewStorage.putLikeToReview(reviewId, userId);
    }

    public void putDislikeToReview(Integer reviewId, Integer userId) {
        reviewStorage.putDisikeToReview(reviewId, userId);
    }

    public void deleteLikeFromReview(Integer reviewId, Integer userId) {
        reviewStorage.deleteLikeFromReview(reviewId, userId);
    }

    public void deleteDislikeFromReview(Integer reviewId, Integer userId) {
        reviewStorage.deleteDisikeFromReview(reviewId, userId);
    }

    public void deleteReviewById(Integer reviewId) {
        reviewStorage.deleteReviewById(reviewId);
    }

}
