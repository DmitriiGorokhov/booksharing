package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.images.Image;

import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}