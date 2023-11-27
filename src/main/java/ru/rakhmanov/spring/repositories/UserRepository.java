package ru.rakhmanov.spring.repositories;

import ru.rakhmanov.spring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Person, Integer> {
    @Modifying
    @Query(value = "CALL remove_person_from_book(?1)", nativeQuery = true)
    void removeUserFromBook(int bookId);
}
