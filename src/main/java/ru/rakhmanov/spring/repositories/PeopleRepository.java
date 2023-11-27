package ru.rakhmanov.spring.repositories;

import ru.rakhmanov.spring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {
    Optional<Person> findByFullName(String fullName);

    @Query(value = "CALL get_person_by_fullName(?1,1)",nativeQuery = true)
    List<Integer> getIdByFullName(String fullName);
}
