package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Storage;

import java.util.Optional;

@Repository
public interface StoragesRepository extends JpaRepository<Storage, Long> {
    Optional<Storage> findByLocation(String location);
}