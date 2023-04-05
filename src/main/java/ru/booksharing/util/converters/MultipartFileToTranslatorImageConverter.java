package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.TranslatorImage;

@Component
public class MultipartFileToTranslatorImageConverter implements Converter<MultipartFile, TranslatorImage> {

    @Override
    public TranslatorImage convert(MultipartFile source) {
        TranslatorImage image = new TranslatorImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}