package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.booksharing.models.works.GenreWork;

public interface GenreWorksRepository extends JpaRepository<GenreWork, Long> {}