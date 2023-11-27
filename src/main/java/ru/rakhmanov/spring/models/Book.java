package ru.rakhmanov.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "title")
    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 1, max = 100, message = "Название книги должно быть от 1 до 100 символов")
    String title; // name

    @Column(name = "author")
    @NotEmpty(message = "Автор не должен быть пустым")
    @Size(min = 2, max = 100, message = "Автор должен быть от 2 до 100 символов")
    String author;

    @Column(name = "year")
    int year;

    @ManyToOne
    @JoinColumn(name = "personid", referencedColumnName = "id")
    Person person;

    // Время выдачи книги человеку
    @Column(name = "issued")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime issued;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "created_who")
    @NotEmpty
    String createdWho;

    public Book() {
    }

    public Book(String title, String author, int year, Person person) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.person = person;
    }

    public boolean isExpired() {
        LocalDateTime issuedDate = getIssued();
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(issuedDate, currentDate);
        long TEN_DAYS_IN_SECONDS = 10 * 24 * 60 * 60;

        return duration.getSeconds() > TEN_DAYS_IN_SECONDS;
    }
}
