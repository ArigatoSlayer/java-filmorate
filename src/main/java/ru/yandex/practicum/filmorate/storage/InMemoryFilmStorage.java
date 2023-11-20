package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate MAGIC_DATE = LocalDate.of(1895, 12, 28);
    private final int MAX_CHAR = 200;

    private int id = 1;


    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            if (film.getLikes() == null) {
                film.setLikes(films.get(film.getId()).getLikes());
            }
            films.put(film.getId(), film);
            log.info("FilmId: " + film.getId() + " update");
        } else {
            log.error("Не удалось обновить " + film.getId() + "  id не найден");
            throw new RuntimeException("Id " + film.getId() + " не существует");
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        if (isValidFilm(film)) {
            film.setId(id);
            film.setLikes(new HashSet<>());
            films.put(id, film);
            id++;
            log.info("Created film" + film.getName() + "    " + film.getId());
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм c id: " + id + " не найден");
        }
    }

    private boolean isValidFilm(Film film) {
        film.setName(film.getName().trim());
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > MAX_CHAR) {
            throw new ValidationException("максимальная длина описания — " + MAX_CHAR + " символов");
        } else if (film.getReleaseDate().isBefore(MAGIC_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
