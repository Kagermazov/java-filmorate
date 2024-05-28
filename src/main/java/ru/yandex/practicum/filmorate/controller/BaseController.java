package ru.yandex.practicum.filmorate.controller;
import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.List;
import java.util.Map;

public abstract class BaseController<T extends BaseEntity> implements Controller<T> {

    @Override
    public List<T> findAll(final Map<Integer, T> storage) {
        return storage.values().stream().toList();
    }

    @Override
    public int getNextId(final Map<Integer, T> storage) {
        int currentMaxId = storage.values().stream()
                .mapToInt(BaseEntity::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
