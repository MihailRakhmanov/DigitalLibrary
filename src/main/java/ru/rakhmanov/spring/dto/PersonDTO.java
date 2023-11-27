package ru.rakhmanov.spring.dto;

import ru.rakhmanov.spring.models.Book;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonDTO {

    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    String fullName;

    LocalDate dateOfBirth;

    List<Book> books;

    String password;

    String role;

    String status;

    public boolean isLocked() {
        return status.equals("LOCKED");
    }
}
