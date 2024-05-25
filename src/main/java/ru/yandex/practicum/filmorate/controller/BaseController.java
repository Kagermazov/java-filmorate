package ru.yandex.practicum.filmorate.controller;
import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.List;

public abstract class BaseController<T extends BaseEntity> implements Controller<T> {

    ControllerValidator<T> validator = new ControllerValidator<>();

    @Override
    public List<T> findAll(List<T> storage) {
        return storage;
    }

    @Override
    public int getNextId(List<? extends BaseEntity> storage) {
        int currentMaxId = storage.stream()
                .mapToInt(BaseEntity::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public boolean isObjectInStorage(T objectToCheck, List<? extends BaseEntity> storage) {
        return storage.stream().anyMatch(storedEntity -> storedEntity.getId() == objectToCheck.getId());
    }
}
