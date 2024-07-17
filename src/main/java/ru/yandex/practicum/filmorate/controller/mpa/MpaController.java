package ru.yandex.practicum.filmorate.controller.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @Autowired
    public MpaController(MpaService serviceForMpa) {
        service = serviceForMpa;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MpaDto findMpaName(@PathVariable Long id) {
        return service.findMpaName(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MpaDto> findAllMpa() {
        return service.findAllMpa();
    }
}
