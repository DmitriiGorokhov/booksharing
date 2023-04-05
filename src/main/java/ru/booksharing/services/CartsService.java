package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Book;
import ru.booksharing.models.Cart;
import ru.booksharing.models.Person;
import ru.booksharing.repositories.CartsRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class CartsService {

    private final CartsRepository cartsRepository;

    @Autowired
    public CartsService(CartsRepository cartsRepository) {
        this.cartsRepository = cartsRepository;
    }

    public Optional<Cart> findOne(long id) {
        return cartsRepository.findById(id);
    }

    public Optional<Cart> findOne(Person person) {
        return cartsRepository.findByPerson(person);
    }

    public Set<Cart> findAll() {
        return new HashSet<>(cartsRepository.findAll());
    }

    public Set<Cart> findAll(Set<Book> books) {
        return cartsRepository.findAllByBooksIn(books);
    }

    @Transactional
    public Cart save(Cart cart, Person person) {
        cart.setPerson(person);
        person.setCart(cart);
        return cartsRepository.save(cart);
    }

    @Transactional
    public void addBook(Person person, Book book) {
        Optional<Cart> cartOpt = findOne(person);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.getBooks().add(book);
        }
    }

    @Transactional
    public void removeBook(Person person, Book book) {
        Optional<Cart> cartOpt = findOne(person);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.getBooks().remove(book);
        }
    }

    @Transactional
    public void removeBook(Cart cart, Book book) {
        cart.getBooks().remove(book);
        cartsRepository.save(cart);
    }
}