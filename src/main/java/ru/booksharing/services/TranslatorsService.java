package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Translator;
import ru.booksharing.models.images.TranslatorImage;
import ru.booksharing.repositories.TranslatorsRepository;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class TranslatorsService {

    private final TranslatorsRepository translatorsRepository;

    @Autowired
    public TranslatorsService(TranslatorsRepository translatorsRepository) {
        this.translatorsRepository = translatorsRepository;
    }

    public Set<Translator> findAll() {
        return new HashSet<>(translatorsRepository.findAll());
    }

    public Set<Translator> findAllByFullNameContaining(String text) {
        Set<String> fullNames = new HashSet<>();
        Collections.addAll(fullNames, text.toLowerCase().split("\\s+"));

        Set<Translator> translators = new HashSet<>();
        for (String fullName : fullNames)
            translators.addAll(translatorsRepository.findAllByFullNameContainingIgnoreCase(fullName));

        return translators;
    }

    public Optional<Translator> findOne(long id) {
        return translatorsRepository.findById(id);
    }

    public Optional<Translator> findOne(String fullName) {
        return translatorsRepository.findByFullName(fullName);
    }

    @Transactional
    public Translator save(Translator translator, TranslatorImage translatorImage) {
        translator.setImage(translatorImage);
        translator.setCreatedAt(LocalDateTime.now());
        return translatorsRepository.save(translator);
    }

    public String getPathToImage(Translator translator) {
        try {
            String pathToURL = Paths.get(translator.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}