package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.DirectorSortBy;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService service;

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        return service.getFilmById(filmId);
    }

    @GetMapping("/search")
    public List<Film> searchBySubstring(@RequestParam String query,
                                        @RequestParam List<String> by) {
        return service.searchBySubstring(query, by);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count,
                                          @RequestParam(required = false) Integer genreId,
                                          @RequestParam(required = false) @Min(value = 1895) Integer year) {
        return service.getMostPopularsFilms(count, genreId, year);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return service.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return service.deleteLike(id, userId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return service.createFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        service.deleteFilm(id);
    }

    @GetMapping("/common")
    public List<Film> getListCommonFilms(@RequestParam Integer userId, @RequestParam Integer friendId) {
        return service.getListCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getAllDirectorFilms(@PathVariable int directorId, @RequestParam DirectorSortBy sortBy) {
        return service.getAllDirectorFilms(directorId, sortBy);
    }
}
