package ru.booksharing.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.booksharing.models.Translator;
import ru.booksharing.services.TranslatorsService;

@Component
public class TranslatorValidator implements Validator {

    private final TranslatorsService translatorsService;

    @Autowired
    public TranslatorValidator(TranslatorsService translatorsService) {
        this.translatorsService = translatorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Translator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Translator translator = (Translator) target;
        if (translatorsService.findOne(translator.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "Это имя уже существует");
    }
}