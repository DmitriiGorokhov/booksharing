package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Author;
import ru.booksharing.models.images.AuthorImage;
import ru.booksharing.repositories.AuthorsRepository;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AuthorsService {

    private final AuthorsRepository authorsRepository;

    @Autowired
    public AuthorsService(AuthorsRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    public Set<Author> findAll() {
        return new HashSet<>(authorsRepository.findAll());
    }

    public Set<Author> findAllByFullNameContaining(String text) {
        Set<String> fullNames = new HashSet<>();
        Collections.addAll(fullNames, text.toLowerCase().split("\\s+"));

        Set<Author> authors = new HashSet<>();
        for (String fullName : fullNames)
            authors.addAll(authorsRepository.findAllByFullNameContainingIgnoreCase(fullName));

        return authors;
    }

    public Optional<Author> findOne(long id) {
        return authorsRepository.findById(id);
    }

    public Optional<Author> findOne(String fullName) {
        return authorsRepository.findByFullName(fullName);
    }

    @Transactional
    public Author save(Author author, AuthorImage authorImage) {
        author.setImage(authorImage);
        author.setCreatedAt(LocalDateTime.now());
        return authorsRepository.save(author);
    }

    public String getPathToImage(Author author) {
        try {
            String pathToURL = Paths.get(author.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}