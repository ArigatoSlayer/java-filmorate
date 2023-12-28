package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@PathVariable Integer id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews(@RequestParam(required = false, defaultValue = "0") Integer filmId,
                                      @RequestParam(required = false, defaultValue = "10") Integer count) {
        return reviewService.getAllReviews(filmId, count);
    }

    @PostMapping("/reviews")
    public Review postReview(@Valid @RequestBody Review review) {
        return reviewService.postReview(review);
    }

    @PutMapping("/reviews")
    public Review putReview(@Valid @RequestBody Review review) {
        return reviewService.putReview(review);
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public Review putLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.putLikeToReview(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public Review putDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.putDislikeToReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public Review deleteLikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.deleteLikeFromReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public Review deleteDislikeFromReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.deleteDislikeFromReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReviewById(@PathVariable Integer id) {
        reviewService.deleteReviewById(id);
    }

}
