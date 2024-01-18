package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    public Director getDirectorById(int directorId) {
        return directorStorage.getDirectorById(directorId);
    }

    public Director postDirector(Director director) {
        return directorStorage.postDirector(director);
    }

    public Director putDirector(Director director) {
        return directorStorage.putDirector(director);
    }

    public void deleteDirectorById(int directorId) {
        directorStorage.deleteDirectorById(directorId);
    }

}