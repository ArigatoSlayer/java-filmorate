package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Repository
public class DirectorDbStorage implements DirectorStorage {

    @Override
    public List<Director> getAllDirectors() {
        return null;
    }

    @Override
    public Director getDirectorById(int directorId) {
        return null;
    }

    @Override
    public Director postDirector(Director director) {
        return null;
    }

    @Override
    public Director putDirector(Director director) {
        return null;
    }

    @Override
    public void deleteDirectorById(int directorId) {
    }

}
