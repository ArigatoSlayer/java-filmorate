package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.DirectorSortBy;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final DirectorStorage directorStorage;

    public Film updateFilm(Film film) {
        directorStorage.setFilmDirectorsToDb(film.getId(), film.getDirectors());
        Film resultFilm = filmStorage.updateFilm(film);
        resultFilm.setDirectors(directorStorage.getFilmDirectorsFromDb(film.getId()));
        return resultFilm;
    }

    public Film createFilm(Film film) {
        Film resultFilm = filmStorage.createFilm(film);
        directorStorage.setFilmDirectorsToDb(resultFilm.getId(), film.getDirectors());
        resultFilm.setDirectors(directorStorage.getFilmDirectorsFromDb(film.getId()));
        return resultFilm;
    }

    public List<Film> getFilms() {
        return directorStorage.setDirectorsToFilmList(filmStorage.getFilms());
    }

    public Film getFilmById(int filmId) {
        Film film = filmStorage.getFilmById(filmId);
        film.setDirectors(directorStorage.getFilmDirectorsFromDb(filmId));
        return film;
    }

    public List<Film> getAllDirectorFilms(int directorId, DirectorSortBy directorSortBy) {
        directorStorage.getDirectorById(directorId);
        if (directorSortBy == DirectorSortBy.likes) {
            return directorStorage.setDirectorsToFilmList(filmStorage.getAllDirectorFilmsOrderByLikes(directorId));
        }
        if (directorSortBy == DirectorSortBy.year) {
            return directorStorage.setDirectorsToFilmList(filmStorage.getAllDirectorFilmsOrderByYear(directorId));
        }
        throw new RuntimeException("Введены неверные данные.");
    }

    public Film putLike(int filmId, int userId) {
        Film film = filmStorage.addLike(filmId, userId);
        film.setDirectors(directorStorage.getFilmDirectorsFromDb(filmId));
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.deleteLike(filmId, userId);
        film.setDirectors(directorStorage.getFilmDirectorsFromDb(filmId));
        return film;
    }

    public List<Film> getMostPopularsFilms(Integer count, Integer genreId, Integer year) {
        List<Film> popularsFilms;
        if (genreId != null && year != null) {
            popularsFilms = filmStorage.getListTopFilmsByGenreAndYear(year, genreId);
        } else if (count != null) {
            popularsFilms = filmStorage.getListTopFilmsByCount(count);
        } else if (year != null) {
            popularsFilms = filmStorage.getListTopFilmsByYear(year);
        } else if (genreId != null) {
            popularsFilms = filmStorage.getListOfTopFilmsByGenre(genreId);
        } else {
            popularsFilms = filmStorage.getListOfTopFilms();
        }
        return directorStorage.setDirectorsToFilmList(popularsFilms);
    }

    public List<Film> searchBySubstring(String str, List<String> by) {
        List<Film> films;
        if (by.size() == 2) {
            films = filmStorage.searchBySubstring(str);
        } else if (by.size() == 1 && by.contains("title")) {
            films = filmStorage.searchBySubstringByFilms(str);
        } else if (by.size() == 1 && by.contains("director")) {
            films = filmStorage.searchBySubstringByDirectors(str);
        } else {
            throw new NotFoundException("Фильмы с подстрокой " + str + " не найдены");
        }
        return directorStorage.setDirectorsToFilmList(films);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public List<Film> getListCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getListCommonFilms(userId, friendId);

    }

}
