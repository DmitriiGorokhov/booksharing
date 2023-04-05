package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.BookWork;

@Repository
public interface BookWorksRepository extends JpaRepository<BookWork, Long> {}