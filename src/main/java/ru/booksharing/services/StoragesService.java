package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.Storage;
import ru.booksharing.models.images.StorageImage;
import ru.booksharing.repositories.StoragesRepository;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class StoragesService {

    private final StoragesRepository storagesRepository;

    @Autowired
    public StoragesService(StoragesRepository storagesRepository) {
        this.storagesRepository = storagesRepository;
    }

    public Set<Storage> findAll() {
        return new HashSet<>(storagesRepository.findAll());
    }

    public Optional<Storage> findOne(long id) {
        return storagesRepository.findById(id);
    }

    public Optional<Storage> findOne(String location) {
        return storagesRepository.findByLocation(location);
    }

    @Transactional
    public Storage save(Storage storage, StorageImage storageImage) {
        storage.setImage(storageImage);
        storage.setCreatedAt(LocalDateTime.now());
        return storagesRepository.save(storage);
    }

    public String getPathToImage(Storage storage) {
        try {
            String pathToURL = Paths.get(storage.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}