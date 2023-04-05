package ru.booksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.booksharing.models.Person;
import ru.booksharing.security.PersonDetails;
import ru.booksharing.services.AuthorsService;
import ru.booksharing.services.BooksService;
import ru.booksharing.services.GenresService;
import ru.booksharing.services.PeopleService;

import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final AuthorsService authorsService;
    private final GenresService genresService;

    @Autowired
    public MainController(PeopleService peopleService, BooksService booksService, AuthorsService authorsService, GenresService genresService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.authorsService = authorsService;
        this.genresService = genresService;
    }

    @GetMapping("")
    public String main(Model model) {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        Person person;
        if (principal.containsKey(true)) {
            person = ((PersonDetails) principal.get(true)).getPerson();
        } else
            person = new Person();

        model.addAttribute("person", person);
        model.addAttribute("bookNumber", booksService.findAll().size());
        model.addAttribute("authorNumber", authorsService.findAll().size());
        model.addAttribute("genreNumber", genresService.findAll().size());
        return "index";
    }
}