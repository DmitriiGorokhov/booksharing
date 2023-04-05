package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.BookImage;

@Component
public class MultipartFileToBookImageConverter implements Converter<MultipartFile, BookImage> {

    @Override
    public BookImage convert(MultipartFile source) {
        BookImage image = new BookImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}