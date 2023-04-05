package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.*;
import ru.booksharing.models.enums.AgeLimit;
import ru.booksharing.models.enums.Cover;
import ru.booksharing.models.images.BookImage;
import ru.booksharing.repositories.BooksRepository;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public Set<Book> findAll() {
        return new HashSet<>(booksRepository.findAll());
    }

    public Set<Book> findAll(Set<Cart> carts) {
        return booksRepository.findAllByCartsIn(carts);
    }

    public Set<Book> findAllByTitleContaining(String text) {
        Set<String> titles = new HashSet<>();
        Collections.addAll(titles, text.toLowerCase().split("\\s+"));

        Set<Book> books = new HashSet<>();
        for (String title : titles)
            books.addAll(booksRepository.findAllByTitleContainingIgnoreCase(title));

        return books;
    }

    public Optional<Book> findOne(long id) {
        return booksRepository.findById(id);
    }

    public Optional<Book> findOne(String title) {
        return booksRepository.findByTitle(title);
    }

    @Transactional
    public Book save(Book book) {
        return booksRepository.save(book);
    }

    @Transactional
    public Book save(Book book, BookImage bookImage, Author author, Genre genre, Language language,
                     PublishingHouse publishingHouse, Translator translator, Storage storage,
                     Cover cover, AgeLimit ageLimit) {
        book.setImage(bookImage);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setLanguage(language);
        book.setPublishingHouse(publishingHouse);
        book.setTranslator(translator);
        book.setStorage(storage);
        book.setCover(cover);
        book.setAgeLimit(ageLimit);
        book.setCreatedAt(LocalDateTime.now());
        return booksRepository.save(book);
    }

    @Transactional
    public Book save(Book book, BookImage bookImage) {
        book.setImage(bookImage);
        book.setCreatedAt(LocalDateTime.now());
        return booksRepository.save(book);
    }

    public String getPathToImage(Book book) {
        try {
            String pathToURL = Paths.get(book.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}