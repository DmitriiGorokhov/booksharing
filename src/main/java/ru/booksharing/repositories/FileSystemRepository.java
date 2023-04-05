package ru.booksharing.repositories;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Repository
public class FileSystemRepository {

    private final URI RESOURCES_DIR = Objects.requireNonNull(FileSystemRepository.class.getResource("/static/img/")).toURI();

    public FileSystemRepository() throws URISyntaxException {
    }

    public String save(byte[] content, String imageName, String imageObject) throws IOException {
        String path = Paths.get(RESOURCES_DIR).toString();

        switch (imageObject) {
            case "adding_author",
                "adding_book",
                "adding_genre",
                "adding_language",
                "adding_publishing_house",
                "adding_storage",
                "adding_translator",
                "appoint_admin",
                "appoint_client",
                "appoint_db_manager",
                "appoint_deliveryman",
                "appoint_packer",
                "delivery_to_storage",
                "delivery_to_the_client",
                "packing_in_storage",
                "sorting_in_storage"-> imageObject = "work";
        }

        Path newFile = Paths.get(path + File.separator + imageObject + File.separator + imageName);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);

        return newFile.toAbsolutePath().toString();
    }
}