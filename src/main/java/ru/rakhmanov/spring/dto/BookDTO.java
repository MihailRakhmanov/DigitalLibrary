package ru.rakhmanov.spring.dto;

import ru.rakhmanov.spring.models.Person;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDTO {

    int id;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 1, max = 100, message = "Название книги должно быть от 1 до 100 символов")
    String title;

    @NotEmpty(message = "Автор не должен быть пустым")
    @Size(min = 2, max = 100, message = "Автор должен быть от 2 до 100 символов")
    String author;

    int year;

    Person person;
}
