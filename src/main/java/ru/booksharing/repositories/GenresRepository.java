package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Genre;

import java.util.Optional;
import java.util.Set;

@Repository
public interface GenresRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
    Set<Genre> findAllByNameContainingIgnoreCase(String name);
}