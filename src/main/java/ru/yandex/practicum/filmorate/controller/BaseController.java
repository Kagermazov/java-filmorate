package ru.yandex.practicum.filmorate.controller;
import java.util.Map;

public abstract class BaseController<T> implements Controller<T> {

    ControllerValidator<T> validator = new ControllerValidator<>();

    @Override
    public int getNextId(Map<Integer, T> storage) {
        int currentMaxId = storage.keySet()
                .stream()
                .mapToInt(v -> v)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
