package ru.yandex.practicum.filmorate.storage.feed;

public interface FeedStorage {

    void addFeed(int userId, int type, int operationId, int entityId);
}
