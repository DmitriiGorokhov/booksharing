package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.TranslatorWork;

@Repository
public interface TranslatorWorksRepository extends JpaRepository<TranslatorWork, Long> {}