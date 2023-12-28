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
        validateReview(review);
        filmStorage.getFilmById(review.getFilmId());
        userStorage.getUserById(review.getUserId());
        return reviewStorage.postReview(review);
    }

    public Review putReview(Review review) {
        return reviewStorage.putReview(review);
    }

    public void incrementLikeToReview(Integer reviewId, Integer userId) {
        userStorage.getUserById(userId);
        reviewStorage.incrementLikeToReview(reviewId, userId);
    }

    public void decrementLikeToReview(Integer reviewId, Integer userId) {
        userStorage.getUserById(userId);
        reviewStorage.decrementLikeToReview(reviewId, userId);
    }

    public void deleteReviewById(Integer reviewId) {
        reviewStorage.deleteReviewById(reviewId);
    }

    private void validateReview(Review review) {
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new ValidationException("Отзыв должен содержать контент.");
        }
        if (review.getIsPositive() == null) {
            throw new ValidationException("Отзыв должен содержать тип.");
        }
        if (review.getUserId() == null) {
            throw new ValidationException("Отзыв должен содержать идентификатор пользователя.");
        }
        if (review.getFilmId() == null) {
            throw new ValidationException("Отзыв должен содержать идентификатор фильма.");
        }
    }

}
