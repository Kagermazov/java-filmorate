package ru.yandex.practicum.filmorate.service.film;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

//@NoArgsConstructor(force = true)
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
//    @Autowired
//    Environment environment;

    @Value("${filmorate.CINEMA_INVENTION_DATE}")
    private String cinemaInventionDate;
//
//    public ReleaseDateValidator(@Value("${filmorate.CINEMA_INVENTION_DATE}") String cinemaInventionDate) {
//        this.cinemaInventionDate = cinemaInventionDate;
//    }

    @Override
    public boolean isValid(LocalDate dateToCheck, ConstraintValidatorContext context) {
        return dateToCheck.isAfter(LocalDate.parse(cinemaInventionDate));
    }
}
