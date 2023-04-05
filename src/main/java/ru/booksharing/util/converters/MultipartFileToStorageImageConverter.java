package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.StorageImage;

@Component
public class MultipartFileToStorageImageConverter implements Converter<MultipartFile, StorageImage> {

    @Override
    public StorageImage convert(MultipartFile source) {
        StorageImage image = new StorageImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}