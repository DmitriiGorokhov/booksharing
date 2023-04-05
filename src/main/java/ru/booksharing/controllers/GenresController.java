package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.Genre;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.GenreImage;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.models.works.GenreWork;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.services.GenresService;
import ru.booksharing.services.WorksService;
import ru.booksharing.util.validators.GenreValidator;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/genres")
public class GenresController {

    private final GenresService genresService;
    private final GenreValidator genreValidator;
    private final WorksService worksService;
    private final FileLocationService fileLocationService;

    @Autowired
    public GenresController(GenresService genresService, GenreValidator genreValidator, WorksService worksService, FileLocationService fileLocationService) {
        this.genresService = genresService;
        this.genreValidator = genreValidator;
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("genres", genresService.findAll());
        return "genres/genres";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Optional<Genre> genreOpt = genresService.findOne(id);
        if (genreOpt.isPresent()) {
            Genre genre = genreOpt.get();
            String pathToImage = genresService.getPathToImage(genre);
            model.addAttribute("genre", genre);
            model.addAttribute("pathToImage", pathToImage);
            return "genres/genre";
        } else
            return "redirect:/genres";
    }

    @GetMapping("/new")
    public String add(@ModelAttribute("genre") Genre genre) {
        return "genres/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam("image")MultipartFile image,
                         @ModelAttribute("genre") @Valid Genre genre,
                         BindingResult bindingResult) throws IOException {
        genreValidator.validate(genre, bindingResult);
        if (bindingResult.hasErrors())
            return "genres/new";

        String imageObject = "genre";
        GenreImage genreImage;
        if (!image.isEmpty())
            genreImage = (GenreImage) fileLocationService.save(image, imageObject, false);
        else
            genreImage = (GenreImage) fileLocationService.findDefaultImage(imageObject);

        genre = genresService.save(genre, genreImage);

        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("adding_genre");
        worksService.save(new GenreWork(), workImage, genre, WorkKind.ADDING_GENRE);
        return "redirect:/genres";
    }
}