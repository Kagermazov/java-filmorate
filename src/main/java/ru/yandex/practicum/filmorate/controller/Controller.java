package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.List;


public interface Controller<T> {
    T create(T newObject);

    T update(T updatedObject);

    List<T> findAll(List<T> storage);

    int getNextId(List<? extends BaseEntity> storage);

    boolean isObjectInStorage(T objectToCheck, List<? extends BaseEntity> storage);
}
