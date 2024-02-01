package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.TypeOperation;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;
    private final JdbcTemplate jdbcTemplate;

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

    public Review postReview(@Valid Review review) {
        Integer userId = review.getUserId();
        filmStorage.getFilmById(review.getFilmId());
        userStorage.getUserById(userId);
        Review reviewUpdate = reviewStorage.postReview(review);
        feedStorage.addFeed(userId, EventType.REVIEW.numInDb, TypeOperation.ADD.numInDb, reviewUpdate.getReviewId());
        return reviewUpdate;
    }

    public Review putReview(Review review) {
        Review reviewUpdate = reviewStorage.putReview(review);
        int reviewId = reviewUpdate.getReviewId();
        Integer userId = reviewUpdate.getUserId();
        feedStorage.addFeed(userId, EventType.REVIEW.numInDb, TypeOperation.UPDATE.numInDb, reviewId);
        return reviewUpdate;
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
        Integer userId = getUserId(reviewId);
        reviewStorage.deleteReviewById(reviewId);
        feedStorage.addFeed(userId, EventType.REVIEW.numInDb, TypeOperation.REMOVE.numInDb, reviewId);
    }

    private Integer getUserId(int reviewId) {
        String sql = "SELECT user_id FROM reviews " +
                "WHERE review_id = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
    }

}
