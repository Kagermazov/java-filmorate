package ru.yandex.practicum.filmorate.controller;

import java.util.Map;

public interface Controller<T> {
    T create(T newObject);

    T update(T updatedObject);

    Map<Integer, T> findAll();

    int getNextId(Map<Integer, T> storage);
}
