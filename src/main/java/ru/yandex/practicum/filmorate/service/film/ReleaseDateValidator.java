package ru.yandex.practicum.filmorate.service.film;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    private final String cinemaInventionDate;

    //почему внедряю значение через конструктор, а не через поле, написал в комментарии в pull request
    public ReleaseDateValidator(@Value("${filmorate.CINEMA_INVENTION_DATE}") String cinemaInventionDate) {
        this.cinemaInventionDate = cinemaInventionDate;
    }

    @Override
    public boolean isValid(LocalDate dateToCheck, ConstraintValidatorContext context) {
        return dateToCheck.isAfter(LocalDate.parse(this.cinemaInventionDate));
    }
}
