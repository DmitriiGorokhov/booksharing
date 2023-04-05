package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.Storage;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.StorageImage;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.models.works.StorageWork;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.services.StoragesService;
import ru.booksharing.services.WorksService;
import ru.booksharing.util.validators.StorageValidator;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/storages")
public class StoragesController {

    private final StoragesService storagesService;
    private final StorageValidator storageValidator;
    private final WorksService worksService;
    private final FileLocationService fileLocationService;

    @Autowired
    public StoragesController(StoragesService storagesService, StorageValidator storageValidator, WorksService worksService, FileLocationService fileLocationService) {
        this.storagesService = storagesService;
        this.storageValidator = storageValidator;
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("storages", storagesService.findAll());
        return "storages/storages";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Optional<Storage> storageOpt = storagesService.findOne(id);
        if (storageOpt.isPresent()) {
            Storage storage = storageOpt.get();
            String pathToImage = storagesService.getPathToImage(storage);
            model.addAttribute("storage", storage);
            model.addAttribute("pathToImage", pathToImage);
            return "storages/storage";
        } else
            return "redirect:/storages";
    }

    @GetMapping("/new")
    public String add(@ModelAttribute("storage") Storage storage) {
        return "storages/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam("image") MultipartFile image,
                         @ModelAttribute("storage") @Valid Storage storage,
                         BindingResult bindingResult) throws IOException {
        storageValidator.validate(storage, bindingResult);
        if (bindingResult.hasErrors())
            return "storages/new";

        String imageObject = "storage";
        StorageImage storageImage;
        if (!image.isEmpty())
            storageImage = (StorageImage) fileLocationService.save(image, imageObject, false);
        else
            storageImage = (StorageImage) fileLocationService.findDefaultImage(imageObject);

        storage = storagesService.save(storage, storageImage);

        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("adding_storage");
        worksService.save(new StorageWork(), workImage, storage, WorkKind.ADDING_STORAGE);
        return "redirect:/storages";
    }
}