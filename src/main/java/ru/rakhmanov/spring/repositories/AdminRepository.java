package ru.rakhmanov.spring.repositories;

import ru.rakhmanov.spring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Person, Integer> {
    @Modifying
    @Query(value = "CALL update_person_status(?1,?2)", nativeQuery = true)
    void updatePersonStatus(int personId, String status);

}
