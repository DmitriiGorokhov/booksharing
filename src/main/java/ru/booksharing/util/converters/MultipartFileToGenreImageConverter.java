package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.GenreImage;

@Component
public class MultipartFileToGenreImageConverter implements Converter<MultipartFile, GenreImage> {

    @Override
    public GenreImage convert(MultipartFile source) {
        GenreImage image = new GenreImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}