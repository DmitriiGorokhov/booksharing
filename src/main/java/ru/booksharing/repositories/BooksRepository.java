package ru.booksharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.Book;
import ru.booksharing.models.Cart;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    Set<Book> findAllByCartsIn(Set<Cart> carts);
    Set<Book> findAllByTitleContainingIgnoreCase(String title);
}