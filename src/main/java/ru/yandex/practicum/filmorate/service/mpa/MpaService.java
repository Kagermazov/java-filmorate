package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.mapper.MpaDtoMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

@Slf4j
@Service
public class MpaService {
    private final MpaDbStorage storage;

    @Autowired
    public MpaService(MpaDbStorage storage) {
        this.storage = storage;
    }

    public MpaDto findMpaName(long id) {
        return MpaDtoMapper.mapToMpaDto(this.storage.findMpaName(id)
                .orElseThrow(() -> new InternalServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Null is returned")));
    }
}
