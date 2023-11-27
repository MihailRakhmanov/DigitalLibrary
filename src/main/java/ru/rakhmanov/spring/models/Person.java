package ru.rakhmanov.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    @Column(name = "fullname")
    String fullName;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "created_who")
    @NotEmpty
    String createdWho;

    @OneToMany(mappedBy = "person")
    List<Book> books;

    @Column(name = "password")
    String password;

    @Column(name = "role")
    String role;

    @Column(name = "status")
    String status;

    public Person() {
    }

    public Person(String fullName, LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }
}
