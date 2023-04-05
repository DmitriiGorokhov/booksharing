package ru.booksharing.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.images.PublishingHouseImage;

@Component
public class MultipartFileToPublishingHouseImageConverter implements Converter<MultipartFile, PublishingHouseImage> {

    @Override
    public PublishingHouseImage convert(MultipartFile source) {
        PublishingHouseImage image = new PublishingHouseImage();
        image.setName(source.getOriginalFilename());
        return image;
    }
}