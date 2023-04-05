package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.PersonImage;

@Component
public class MultipartFileToPersonImageConverter implements Converter<MultipartFile, PersonImage> {

    @Override
    public PersonImage convert(MultipartFile source) {
        PersonImage image = new PersonImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}