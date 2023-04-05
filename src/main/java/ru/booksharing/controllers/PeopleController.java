package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.Cart;
import ru.booksharing.models.Person;
import ru.booksharing.models.images.PersonImage;
import ru.booksharing.security.PersonDetails;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.services.PeopleService;
import ru.booksharing.util.validators.PersonValidator;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class PeopleController {

    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final FileLocationService fileLocationService;

    @Autowired
    public PeopleController(PersonValidator personValidator, PeopleService peopleService, FileLocationService fileLocationService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.fileLocationService = fileLocationService;
    }

    @GetMapping("")
    public String show(Model model) {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();
            String pathToImage = peopleService.getPathToImage(person);
            model.addAttribute("person", person);
            model.addAttribute("pathToImage", pathToImage);
            return "profile/profile";
        } else {
            return "profile/auth";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "profile/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person) {
        return "profile/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@RequestParam("image") MultipartFile image,
                                      @ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult) throws IOException {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "profile/registration";

        String imageObject = "person";
        PersonImage personImage;
        if (!image.isEmpty())
            personImage = (PersonImage) fileLocationService.save(image, imageObject, false);
        else
            personImage = (PersonImage) fileLocationService.findDefaultImage(imageObject);

        person = peopleService.save(person, personImage);
        peopleService.update(person, new Cart());
        return "redirect:/profile/login";
    }
}