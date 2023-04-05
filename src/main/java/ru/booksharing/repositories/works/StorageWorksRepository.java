package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.StorageWork;

@Repository
public interface StorageWorksRepository extends JpaRepository<StorageWork, Long> {}