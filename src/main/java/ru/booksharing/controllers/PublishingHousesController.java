package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.PublishingHouse;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.PublishingHouseImage;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.models.works.PublishingHouseWork;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.services.PublishingHousesService;
import ru.booksharing.services.WorksService;
import ru.booksharing.util.validators.PublishingHouseValidator;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/publishing_houses")
public class PublishingHousesController {

    private final PublishingHousesService publishingHousesService;
    private final PublishingHouseValidator publishingHouseValidator;
    private final WorksService worksService;
    private final FileLocationService fileLocationService;

    @Autowired
    public PublishingHousesController(PublishingHousesService publishingHousesService, PublishingHouseValidator publishingHouseValidator, WorksService worksService, FileLocationService fileLocationService) {
        this.publishingHousesService = publishingHousesService;
        this.publishingHouseValidator = publishingHouseValidator;
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("publishingHouses", publishingHousesService.findAll());
        return "publishing_houses/publishing_houses";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Optional<PublishingHouse> publishingHouseOpt = publishingHousesService.findOne(id);
        if (publishingHouseOpt.isPresent()) {
            PublishingHouse publishingHouse = publishingHouseOpt.get();
            String pathToImage = publishingHousesService.getPathToImage(publishingHouse);
            model.addAttribute("publishingHouse", publishingHouse);
            model.addAttribute("pathToImage", pathToImage);
            return "publishing_houses/publishing_house";
        } else
            return "redirect:/publishing_houses";
    }

    @GetMapping("/new")
    public String add(@ModelAttribute("publishingHouse") PublishingHouse publishingHouse) {
        return "publishing_houses/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam("image") MultipartFile image,
                         @ModelAttribute("publishingHouse") @Valid PublishingHouse publishingHouse,
                         BindingResult bindingResult) throws IOException {
        publishingHouseValidator.validate(publishingHouse, bindingResult);
        if (bindingResult.hasErrors())
            return "publishing_houses/new";

        String imageObject = "publishing_house";
        PublishingHouseImage publishingHouseImage;
        if (!image.isEmpty())
            publishingHouseImage = (PublishingHouseImage) fileLocationService.save(image, imageObject, false);
        else
            publishingHouseImage = (PublishingHouseImage) fileLocationService.findDefaultImage(imageObject);

        publishingHouse = publishingHousesService.save(publishingHouse, publishingHouseImage);

        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("adding_publishing_house");
        worksService.save(new PublishingHouseWork(), workImage, publishingHouse, WorkKind.ADDING_PUBLISHING_HOUSE);
        return "redirect:/publishing_houses";
    }
}