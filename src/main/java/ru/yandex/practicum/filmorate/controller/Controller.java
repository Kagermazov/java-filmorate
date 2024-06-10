package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.List;
import java.util.Map;


public interface Controller<T extends BaseEntity> {
    T create(T newObject);

    T update(T updatedObject);

    List<T> findAll(Map<Integer, T> storage);

    int getNextId(Map<Integer, T> storage);

    void validate(T objectToCheck);
}
