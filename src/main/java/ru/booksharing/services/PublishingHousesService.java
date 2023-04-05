package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.PublishingHouse;
import ru.booksharing.models.images.PublishingHouseImage;
import ru.booksharing.repositories.PublishingHousesRepository;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PublishingHousesService {

    private final PublishingHousesRepository publishingHousesRepository;

    @Autowired
    public PublishingHousesService(PublishingHousesRepository publishingHousesRepository) {
        this.publishingHousesRepository = publishingHousesRepository;
    }

    public Set<PublishingHouse> findAll() {
        return new HashSet<>(publishingHousesRepository.findAll());
    }

    public Set<PublishingHouse> findAllByNameContaining(String text) {
        Set<String> names = new HashSet<>();
        Collections.addAll(names, text.toLowerCase().split("\\s+"));

        Set<PublishingHouse> publishingHouses = new HashSet<>();
        for (String name : names)
            publishingHouses.addAll(publishingHousesRepository.findAllByNameContainingIgnoreCase(name));

        return publishingHouses;
    }

    public Optional<PublishingHouse> findOne(long id) {
        return publishingHousesRepository.findById(id);
    }

    public Optional<PublishingHouse> findOne(String name) {
        return publishingHousesRepository.findByName(name);
    }

    @Transactional
    public PublishingHouse save(PublishingHouse publishingHouse, PublishingHouseImage publishingHouseImage) {
        publishingHouse.setImage(publishingHouseImage);
        publishingHouse.setCreatedAt(LocalDateTime.now());
        return publishingHousesRepository.save(publishingHouse);
    }

    public String getPathToImage(PublishingHouse publishingHouse) {
        try {
            String pathToURL = Paths.get(publishingHouse.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}