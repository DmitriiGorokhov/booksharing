package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.LanguageWork;

@Repository
public interface LanguageWorksRepository extends JpaRepository<LanguageWork, Long> {}