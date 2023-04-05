package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.Language;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.LanguageImage;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.models.works.LanguageWork;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.services.LanguagesService;
import ru.booksharing.services.WorksService;
import ru.booksharing.util.validators.LanguageValidator;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/languages")
public class LanguagesController {

    private final LanguagesService languagesService;
    private final LanguageValidator languageValidator;
    private final WorksService worksService;
    private final FileLocationService fileLocationService;

    @Autowired
    public LanguagesController(LanguagesService languagesService, LanguageValidator languageValidator, WorksService worksService, FileLocationService fileLocationService) {
        this.languagesService = languagesService;
        this.languageValidator = languageValidator;
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("languages", languagesService.findAll());
        return "languages/languages";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Optional<Language> languageOpt = languagesService.findOne(id);
        if (languageOpt.isPresent()) {
            Language language = languageOpt.get();
            String pathToImage = languagesService.getPathToImage(language);
            model.addAttribute("language", language);
            model.addAttribute("pathToImage", pathToImage);
            return "languages/language";
        } else
            return "redirect:/languages";
    }

    @GetMapping("/new")
    public String add(@ModelAttribute("language") Language language) {
        return "languages/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam("image") MultipartFile image,
                         @ModelAttribute("language") @Valid Language language,
                         BindingResult bindingResult) throws IOException {
        languageValidator.validate(language, bindingResult);
        if (bindingResult.hasErrors())
            return "languages/new";

        String imageObject = "language";
        LanguageImage languageImage;
        if (!image.isEmpty())
            languageImage = (LanguageImage) fileLocationService.save(image, imageObject, false);
        else
            languageImage = (LanguageImage) fileLocationService.findDefaultImage(imageObject);

        language = languagesService.save(language, languageImage);

        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("adding_language");
        worksService.save(new LanguageWork(), workImage, language, WorkKind.ADDING_LANGUAGE);
        return "redirect:/languages";
    }
}