package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.Translator;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.TranslatorImage;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.models.works.TranslatorWork;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.services.TranslatorsService;
import ru.booksharing.services.WorksService;
import ru.booksharing.util.validators.TranslatorValidator;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/translators")
public class TranslatorsController {

    private final TranslatorsService translatorsService;
    private final TranslatorValidator translatorValidator;
    private final WorksService worksService;
    private final FileLocationService fileLocationService;

    @Autowired
    public TranslatorsController(TranslatorsService translatorsService, TranslatorValidator translatorValidator, WorksService worksService, FileLocationService fileLocationService) {
        this.translatorsService = translatorsService;
        this.translatorValidator = translatorValidator;
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("translators", translatorsService.findAll());
        return "translators/translators";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Optional<Translator> translatorOpt = translatorsService.findOne(id);
        if (translatorOpt.isPresent()) {
            Translator translator = translatorOpt.get();
            String pathToImage = translatorsService.getPathToImage(translator);
            model.addAttribute("translator", translator);
            model.addAttribute("pathToImage", pathToImage);
            return "translators/translator";
        } else
            return "redirect:/translators";
    }

    @GetMapping("/new")
    public String add(@ModelAttribute("translator") Translator translator) {
        return "translators/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam("image") MultipartFile image,
                         @ModelAttribute("translator") @Valid Translator translator,
                         BindingResult bindingResult) throws IOException {
        translatorValidator.validate(translator, bindingResult);
        if (bindingResult.hasErrors())
            return "translators/new";

        String imageObject = "translator";
        TranslatorImage translatorImage;
        if (!image.isEmpty())
            translatorImage = (TranslatorImage) fileLocationService.save(image, imageObject, false);
        else
            translatorImage = (TranslatorImage) fileLocationService.findDefaultImage(imageObject);

        translator = translatorsService.save(translator, translatorImage);

        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("adding_translator");
        worksService.save(new TranslatorWork(), workImage, translator, WorkKind.ADDING_TRANSLATOR);
        return "redirect:/translators";
    }
}