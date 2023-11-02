package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.trace("method: Get. Model: Film");
        return List.copyOf(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("method: Post. Model: Film. FilmName" + film.getId());
        if (isValidFilm(film)) {
            film.setId(id);
            films.put(id, film);
            id++;
            log.info("Created film" + film.getName() + "    " + film.getId());
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("FilmId: " + film.getId() + " update");
        } else {
            log.error("Не удалось обновить " + film.getId() + "  id не найден");
            throw new RuntimeException("Id " + film.getId() + " не существует");
        }
        return film;
    }

    private boolean isValidFilm(Film film) {
        film.setName(film.getName().trim());
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
