package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Book;
import ru.booksharing.models.Person;
import ru.booksharing.models.Rental;
import ru.booksharing.models.enums.RentalStatus;
import ru.booksharing.repositories.RentalsRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Service
@Transactional(readOnly = true)
public class RentalsService {

    private final RentalsRepository rentalsRepository;

    @Autowired
    public RentalsService(RentalsRepository rentalsRepository) {
        this.rentalsRepository = rentalsRepository;
    }

    public Set<Rental> findAll() {
        return new TreeSet<>(rentalsRepository.findAll());
    }

    public Set<Rental>findAll(Person person) {
        return new HashSet<>(rentalsRepository.findAllByPerson(person));
    }

    public Set<Rental>findAll(Book book) {
        return new HashSet<>(rentalsRepository.findAllByBook(book));
    }

    public Set<Rental>findAll(RentalStatus... rentalStatuses) {
        Set<Rental> rentals = new HashSet<>();
        for (RentalStatus rentalStatus : rentalStatuses) {
            rentals.addAll(new HashSet<>(rentalsRepository.findAllByRentalStatus(rentalStatus)));
        }
        return rentals;
    }

    public Optional<Rental> findOne(long id) {
        return rentalsRepository.findById(id);
    }

    @Transactional
    public Rental save(Rental rental, Person person, Book book) {
        rental.setPerson(person);
        rental.setBook(book);
        rental.setRentalStatus(RentalStatus.IS_PROCESSED);
        rental.setCreatedAt(LocalDateTime.now());
        return rentalsRepository.save(rental);
    }

    @Transactional
    public void raiseStatus(Rental rental) {
        RentalStatus oldStatus = rental.getRentalStatus();
        RentalStatus newStatus;
        switch (oldStatus) {
            case IS_PROCESSED -> newStatus = RentalStatus.BEING_PREPARED;
            case BEING_PREPARED -> newStatus = RentalStatus.READY_FOR_DELIVERY_TO_THE_CLIENT;
            case READY_FOR_DELIVERY_TO_THE_CLIENT -> newStatus = RentalStatus.DELIVERED_TO_THE_CLIENT;
            case DELIVERED_TO_THE_CLIENT -> newStatus = RentalStatus.AT_THE_CLIENT;
            case AT_THE_CLIENT -> newStatus = RentalStatus.READY_FOR_RETURN;
            case READY_FOR_RETURN -> newStatus = RentalStatus.DELIVERED_TO_STORAGE;
            case DELIVERED_TO_STORAGE -> newStatus = RentalStatus.READY_FOR_STORAGE;
            case READY_FOR_STORAGE -> newStatus = RentalStatus.IN_STORAGE_SORT;
            case IN_STORAGE_SORT -> newStatus = RentalStatus.COMPLETED;
            default -> newStatus = null;
        }
        rental.setRentalStatus(newStatus);
        rentalsRepository.save(rental);
    }

    @Transactional
    public void done(Rental rental) {
        rental.setDone(LocalDateTime.now());
        rentalsRepository.save(rental);
    }

    public boolean isEmployeeOrRentalOwner(Person person, Rental rental) {
        if (person.getRole().equals("ROLE_USER")) {
            return rental.getPerson().equals(person);
        }
        return true;
    }

    public boolean isAdminOrRentalOwner(Person person, Rental rental) {
        if (person.getRole().equals("ROLE_ADMIN"))
            return true;
        else if (person.getRole().equals("ROLE_USER")) {
            return rental.getPerson().equals(person);
        }
        return false;
    }
}