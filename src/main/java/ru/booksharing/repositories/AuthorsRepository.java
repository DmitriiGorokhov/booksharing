package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Author;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorsRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFullName(String fullName);
    Set<Author> findAllByFullNameContainingIgnoreCase(String fullName);
}