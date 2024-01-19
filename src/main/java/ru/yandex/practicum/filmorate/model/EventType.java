package ru.yandex.practicum.filmorate.model;

public enum EventType {
    LIKE(1),
    REVIEW(2),
    FRIEND(3);

    public final Integer numInDb;

    EventType(Integer numInDb) {
        this.numInDb = numInDb;
    }
}
