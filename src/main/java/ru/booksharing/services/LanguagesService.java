package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Language;
import ru.booksharing.models.images.LanguageImage;
import ru.booksharing.repositories.LanguagesRepository;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class LanguagesService {

    private final LanguagesRepository languagesRepository;

    @Autowired
    public LanguagesService(LanguagesRepository languagesRepository) {
        this.languagesRepository = languagesRepository;
    }

    public Set<Language> findAll() {
        return new HashSet<>(languagesRepository.findAll());
    }

    public Optional<Language> findOne(long id) {
        return languagesRepository.findById(id);
    }

    public Optional<Language> findOne(String name) {
        return languagesRepository.findByName(name);
    }

    @Transactional
    public Language save(Language language, LanguageImage languageImage) {
        language.setImage(languageImage);
        language.setCreatedAt(LocalDateTime.now());
        return languagesRepository.save(language);
    }

    public String getPathToImage(Language language) {
        try {
            String pathToURL = Paths.get(language.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}