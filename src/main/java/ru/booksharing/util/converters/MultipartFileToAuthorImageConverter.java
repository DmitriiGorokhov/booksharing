package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.AuthorImage;

@Component
public class MultipartFileToAuthorImageConverter implements Converter<MultipartFile, AuthorImage> {

    @Override
    public AuthorImage convert(MultipartFile source) {
        AuthorImage image = new AuthorImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}