package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film updateFilm(Film film);

    Film createFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(int id);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    void deleteFilm(int id);

    List<Film> getListTopFilmsByCount(Integer count);

    List<Film> getListTopFilmsByYear(Integer year);

    List<Film> getListOfTopFilmsByGenre(Integer genreId);

    List<Film> getListTopFilmsByGenreAndYear(Integer year, Integer genreId);

    List<Film> getAllDirectorFilmsOrderByLikes(int directorId);

    List<Film> getAllDirectorFilmsOrderByYear(int directorId);

    List<Film> searchBySubstring(String str);

    List<Film> searchBySubstringByFilms(String str);

    List<Film> searchBySubstringByDirectors(String str);

    List<Film> getListCommonFilms(Integer userId, Integer friendId);

}
