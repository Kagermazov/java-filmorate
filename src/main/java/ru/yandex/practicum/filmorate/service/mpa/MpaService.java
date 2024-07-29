package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
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
        return MpaMapper.mapToMpaDto(this.storage.findMpaName(id)
                .orElseThrow(() -> new EntityNotFoundException(HttpStatus.NOT_FOUND,
                        "There's no genre with id " + id)));
    }

    public List<MpaDto> findAllMpa() {
        return this.storage.findAllMpa()
                .stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }
}
