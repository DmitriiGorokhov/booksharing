package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Book;
import ru.booksharing.models.Person;
import ru.booksharing.models.Rental;
import ru.booksharing.models.enums.RentalStatus;

import java.util.Set;

@Repository
public interface RentalsRepository extends JpaRepository<Rental, Long> {
    Set<Rental> findAllByPerson(Person person);
    Set<Rental> findAllByBook(Book book);
    Set<Rental> findAllByRentalStatus(RentalStatus rentalStatus);
}