package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Genre;
import ru.booksharing.models.images.GenreImage;
import ru.booksharing.repositories.GenresRepository;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class GenresService {

    private final GenresRepository genresRepository;

    @Autowired
    public GenresService(GenresRepository genresRepository) {
        this.genresRepository = genresRepository;
    }

    public Set<Genre> findAll() {
        return new HashSet<>(genresRepository.findAll());
    }

    public Set<Genre> findAllByNameContaining(String text) {
        Set<String> names = new HashSet<>();
        Collections.addAll(names, text.toLowerCase().split("\\s+"));

        Set<Genre> genres = new HashSet<>();
        for (String name : names)
            genres.addAll(genresRepository.findAllByNameContainingIgnoreCase(name));

        return genres;
    }

    public Optional<Genre> findOne(long id) {
        return genresRepository.findById(id);
    }

    public Optional<Genre> findOne(String name) {
        return genresRepository.findByName(name);
    }

    @Transactional
    public Genre save(Genre genre, GenreImage genreImage) {
        genre.setImage(genreImage);
        genre.setCreatedAt(LocalDateTime.now());
        return genresRepository.save(genre);
    }

    public String getPathToImage(Genre genre) {
        try {
            String pathToURL = Paths.get(genre.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}