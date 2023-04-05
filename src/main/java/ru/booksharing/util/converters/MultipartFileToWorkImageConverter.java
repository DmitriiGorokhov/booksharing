package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.WorkImage;

@Component
public class MultipartFileToWorkImageConverter implements Converter<MultipartFile, WorkImage> {

    @Override
    public WorkImage convert(MultipartFile source) {
        WorkImage image = new WorkImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}