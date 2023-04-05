package ru.booksharing.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.booksharing.models.PublishingHouse;
import ru.booksharing.services.PublishingHousesService;

@Component
public class PublishingHouseValidator implements Validator {

    private final PublishingHousesService publishingHousesService;

    @Autowired
    public PublishingHouseValidator(PublishingHousesService publishingHousesService) {
        this.publishingHousesService = publishingHousesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PublishingHouse.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PublishingHouse publishingHouse = (PublishingHouse) target;
        if (publishingHousesService.findOne(publishingHouse.getName()).isPresent())
            errors.rejectValue("name", "", "Это наименование уже существует");
    }
}