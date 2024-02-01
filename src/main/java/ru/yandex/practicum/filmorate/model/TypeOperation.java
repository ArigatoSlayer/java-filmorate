package ru.yandex.practicum.filmorate.model;

public enum TypeOperation {
    REMOVE(1),
    ADD(2),
    UPDATE(3);

    public final Integer numInDb;

    TypeOperation(Integer numInDb) {
        this.numInDb = numInDb;
    }
}
