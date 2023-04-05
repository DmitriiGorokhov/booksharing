package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.AuthorWork;

@Repository
public interface AuthorWorksRepository extends JpaRepository<AuthorWork, Long> {}