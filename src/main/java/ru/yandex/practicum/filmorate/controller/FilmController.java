package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
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
    public List<Film> getMostPopularFilms(@RequestParam(required = false) Integer count,
                                          @RequestParam(required = false) Integer genreId,
                                          @RequestParam(required = false) Integer year) {
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
    public List<Film> getAllDirectorFilms(@PathVariable int directorId, @RequestParam String sortBy) {
        return service.getAllDirectorFilms(directorId, sortBy);
    }
}