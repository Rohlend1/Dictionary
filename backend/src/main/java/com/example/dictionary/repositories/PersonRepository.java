package com.example.dictionary.repositories;

import com.example.dictionary.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);

    @Query(value = "SELECT p.id from Person p where p.username = :username")
    Long getUserId(@Param("username") String username);
}
