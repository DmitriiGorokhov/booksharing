package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Book;
import ru.booksharing.models.Cart;
import ru.booksharing.models.Person;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CartsRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByPerson(Person person);
    Set<Cart> findAllByBooksIn(Set<Book> books);
}