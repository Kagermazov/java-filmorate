package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaDbStorage storage;

    @Autowired
    public MpaService(MpaDbStorage storage) {
        this.storage = storage;
    }

    public MpaDto findMpaName(long id) {
        if (this.storage.findAllMpa().size() < id) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no rating with an id " + id);
        }

        return MpaMapper.mapToMpaDto(this.storage.findMpaName(id)
                .orElseThrow(() -> new InternalServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Null is returned")));
    }

    public List<MpaDto> findAllMpa() {
        return this.storage.findAllMpa()
                .stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }
}
