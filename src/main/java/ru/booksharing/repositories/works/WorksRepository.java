package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.Work;

@Repository
public interface WorksRepository extends JpaRepository<Work, Long> {}