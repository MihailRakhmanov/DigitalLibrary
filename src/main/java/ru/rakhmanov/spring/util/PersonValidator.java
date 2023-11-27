package ru.rakhmanov.spring.util;


import ru.rakhmanov.spring.dto.PersonDTO;
import ru.rakhmanov.spring.models.Person;
import ru.rakhmanov.spring.services.PeopleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Component
public class PersonValidator implements Validator {
    PeopleService peopleService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO person = (PersonDTO) target;

        List<Integer> ids = peopleService.getIdByFullName(person.getFullName());
        if (person.getId() != 0 && ids.contains(person.getId()) && ids.size() > 1)
            errors.rejectValue("fullName", "", "Человек с таким логином уже существует");

        if (person.getId() == 0 && peopleService.getPersonByFullName(person.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "Человек с таким логином уже существует");

        if (person.getDateOfBirth() == null) {
            errors.rejectValue("dateOfBirth", "", "Введите дату в формате (dd.mm.yyyy)");
            return;
        }

        LocalDate currentDateMinus7Years = LocalDate.now().minusYears(7);
        LocalDate minYear = LocalDate.now().minusYears(120);

        if (!person.getDateOfBirth().isBefore(currentDateMinus7Years))
            errors.rejectValue("dateOfBirth", "", "Для получения книги, человек должен быть старше 6 лет!");

        if (!person.getDateOfBirth().isAfter(minYear))
            errors.rejectValue("dateOfBirth", "", "Для получения книги, человек должен быть младше 120 лет!");
    }
}
