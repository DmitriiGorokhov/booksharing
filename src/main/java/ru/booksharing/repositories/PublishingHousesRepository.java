package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.PublishingHouse;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PublishingHousesRepository extends JpaRepository<PublishingHouse, Long> {
    Optional<PublishingHouse> findByName(String name);
    Set<PublishingHouse> findAllByNameContainingIgnoreCase(String name);
}