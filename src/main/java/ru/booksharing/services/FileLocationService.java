package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.booksharing.models.images.*;
import ru.booksharing.repositories.FileSystemRepository;
import ru.booksharing.repositories.ImagesRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class FileLocationService {

    private final FileSystemRepository fileSystemRepository;
    private final ImagesRepository imagesRepository;

    @Autowired
    public FileLocationService(FileSystemRepository fileSystemRepository, ImagesRepository imagesRepository) {
        this.fileSystemRepository = fileSystemRepository;
        this.imagesRepository = imagesRepository;
    }

    @Transactional
    public Image save(MultipartFile image, String imageObject, boolean isDefault) throws IOException {
        String imageName;
        if (isDefault)
            imageName = imageObject + ".png";
        else
            imageName = new Date().getTime() + "-" + image.getOriginalFilename();

        byte[] imageContent = image.getBytes();
        String location = fileSystemRepository.save(imageContent, imageName, imageObject);

        Image newImage;
        switch (imageObject) {
            case "author" -> newImage = new AuthorImage(imageName, location);
            case "book" -> newImage = new BookImage(imageName, location);
            case "genre" -> newImage = new GenreImage(imageName, location);
            case "language" -> newImage = new LanguageImage(imageName, location);
            case "person" -> newImage = new PersonImage(imageName, location);
            case "publishing_house" -> newImage = new PublishingHouseImage(imageName, location);
            case "storage" -> newImage = new StorageImage(imageName, location);
            case "translator" -> newImage = new TranslatorImage(imageName, location);
            default -> newImage = new WorkImage(imageName, location);
        }
        newImage.setCreatedAt(LocalDateTime.now());
        return imagesRepository.save(newImage);
    }

    public Image findDefaultImage(String imageObject) {
        return imagesRepository.findByName(imageObject + ".png")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}