package ru.booksharing.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.booksharing.models.Author;
import ru.booksharing.services.AuthorsService;

@Component
public class AuthorValidator implements Validator {

    private final AuthorsService authorsService;

    @Autowired
    public AuthorValidator(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Author author = (Author) target;
        if (authorsService.findOne(author.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "Это имя уже существует");
    }
}