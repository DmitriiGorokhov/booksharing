package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.Author;
import ru.booksharing.models.works.AuthorWork;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.AuthorImage;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.services.AuthorsService;
import ru.booksharing.services.WorksService;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.util.validators.AuthorValidator;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorsService authorsService;
    private final AuthorValidator authorValidator;
    private final WorksService worksService;
    private final FileLocationService fileLocationService;

    @Autowired
    public AuthorsController(AuthorsService authorsService, AuthorValidator authorValidator, WorksService worksService, FileLocationService fileLocationService) {
        this.authorsService = authorsService;
        this.authorValidator = authorValidator;
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("authors", authorsService.findAll());
        return "authors/authors";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Optional<Author> authorOpt = authorsService.findOne(id);
        if (authorOpt.isPresent()) {
            Author author = authorOpt.get();
            String pathToImage = authorsService.getPathToImage(author);
            model.addAttribute("author", author);
            model.addAttribute("pathToImage", pathToImage);
            return "authors/author";
        } else
            return "redirect:/authors";
    }

    @GetMapping("/new")
    public String add(@ModelAttribute("author") Author author) {
        return "authors/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam("image") MultipartFile image,
                         @ModelAttribute("author") @Valid Author author,
                         BindingResult bindingResult) throws IOException {
        authorValidator.validate(author, bindingResult);
        if (bindingResult.hasErrors())
            return "authors/new";

        String imageObject = "author";
        AuthorImage authorImage;
        if (!image.isEmpty())
            authorImage = (AuthorImage) fileLocationService.save(image, imageObject, false);
        else
            authorImage = (AuthorImage) fileLocationService.findDefaultImage(imageObject);

        author = authorsService.save(author, authorImage);

        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("adding_author");
        worksService.save(new AuthorWork(), workImage, author, WorkKind.ADDING_AUTHOR);
        return "redirect:/authors";
    }
}