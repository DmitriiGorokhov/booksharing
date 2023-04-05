package ru.booksharing.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.booksharing.models.Storage;
import ru.booksharing.services.StoragesService;

@Component
public class StorageValidator implements Validator {

    private final StoragesService storagesService;

    @Autowired
    public StorageValidator(StoragesService storagesService) {
        this.storagesService = storagesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Storage.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Storage storage = (Storage) target;
        if (storagesService.findOne(storage.getLocation()).isPresent())
            errors.rejectValue("location", "", "Это местоположение уже существует");
    }
}