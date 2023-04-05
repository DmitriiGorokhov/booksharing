package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.LanguageImage;

@Component
public class MultipartFileToLanguageImageConverter implements Converter<MultipartFile, LanguageImage> {

    @Override
    public LanguageImage convert(MultipartFile source) {
        LanguageImage image = new LanguageImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}