package ru.rakhmanov.spring.services;

import ru.rakhmanov.spring.models.Book;
import ru.rakhmanov.spring.models.Person;
import ru.rakhmanov.spring.repositories.BooksRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
@Transactional(readOnly = true)
public class BooksService {
    BooksRepository booksRepository;
    PeopleService peopleService;

    public List<Book> findAll(int pageNum, int booksPerPage, boolean sortByYear) {
        Page<Book> page = sortByYear
                ? booksRepository.findAll(PageRequest.of(pageNum, booksPerPage, Sort.by("year")))
                : booksRepository.findAll(PageRequest.of(pageNum, booksPerPage));
        return page.getContent();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public List<Book> findByNameStartingWith(String bookName, int page, int size) {
        return booksRepository.findByNameStartingWith(bookName, PageRequest.of(
                        page, size,
                        Sort.by("name", "year")
                )
        );
    }

    @Transactional
    public void save(Book book) {
        whoAndWhenCreated(book);
        booksRepository.save(book);
    }

/*    @Transactional
    public void removePersonFromBook(int id) {
        booksRepository.removePersonFromBook(id);
    }*/

    @Transactional
    public void release (int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setPerson(null);
                    book.setIssued(null);
                });

    }

    @Transactional
    public void update(Book updatedBook) {
        Book originBook = findOne(updatedBook.getId());
        originBook.setTitle(updatedBook.getTitle());
        originBook.setAuthor(updatedBook.getAuthor());
        originBook.setYear(updatedBook.getYear());

        theLastUpdate(originBook);
        booksRepository.save(originBook);
    }

    @Transactional
    public void updateBookPersonRelationship(int bookId, int personId) {
        Book book = findOne(bookId);
        Person person = peopleService.findOne(personId);

        if (book != null && person != null) {
            book.setIssued(LocalDateTime.now());
            booksRepository.updateBookPersonRelationship(bookId, personId);
        }
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    private void whoAndWhenCreated(Book book) {
        book.setCreatedWho("ADMIN");
        book.setCreatedAt(LocalDateTime.now());
        theLastUpdate(book);
    }

    private void theLastUpdate(Book book) {
        book.setUpdatedAt(LocalDateTime.now());
    }
}
