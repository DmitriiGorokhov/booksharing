package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.PersonWork;

@Repository
public interface PersonWorksRepository extends JpaRepository<PersonWork, Long> {}