package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Translator;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TranslatorsRepository extends JpaRepository<Translator, Long> {
    Optional<Translator> findByFullName(String fullName);
    Set<Translator> findAllByFullNameContainingIgnoreCase(String fullName);
}