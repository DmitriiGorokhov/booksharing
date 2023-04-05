package ru.booksharing.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.booksharing.models.Genre;
import ru.booksharing.services.GenresService;

@Component
public class GenreValidator implements Validator {

    private final GenresService genresService;

    @Autowired
    public GenreValidator(GenresService genresService) {
        this.genresService = genresService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Genre.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Genre genre = (Genre) target;
        if (genresService.findOne(genre.getName()).isPresent())
            errors.rejectValue("name", "", "Это наименование уже существует");
    }
}