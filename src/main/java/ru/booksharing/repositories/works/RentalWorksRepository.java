package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Rental;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.works.RentalWork;

import java.util.Optional;

@Repository
public interface RentalWorksRepository extends JpaRepository<RentalWork, Long> {
    Optional<RentalWork> findByRentalAndWorkKind(Rental rental, WorkKind workKind);
}