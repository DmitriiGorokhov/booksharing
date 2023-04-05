package ru.booksharing.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.booksharing.models.Language;
import ru.booksharing.services.LanguagesService;

@Component
public class LanguageValidator implements Validator {

    private final LanguagesService languagesService;

    @Autowired
    public LanguageValidator(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Language.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Language language = (Language) target;
        if (languagesService.findOne(language.getName()).isPresent())
            errors.rejectValue("name", "", "Это наименование уже существует");
    }
}