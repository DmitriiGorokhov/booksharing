package ru.booksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.works.*;
import ru.booksharing.models.Person;
import ru.booksharing.models.Rental;
import ru.booksharing.models.enums.RentalStatus;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.security.PersonDetails;
import ru.booksharing.services.WorksService;
import ru.booksharing.services.FileLocationService;
import ru.booksharing.services.PeopleService;
import ru.booksharing.services.RentalsService;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/works")
public class WorksController {

    private final WorksService worksService;
    private final FileLocationService fileLocationService;
    private final PeopleService peopleService;
    private final RentalsService rentalsService;

    @Autowired
    public WorksController(WorksService worksService, FileLocationService fileLocationService, PeopleService peopleService, RentalsService rentalsService) {
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
        this.peopleService = peopleService;
        this.rentalsService = rentalsService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("works", worksService.findAll());
        return "works/works";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();
            if (worksService.isEmployee(person)) {
                Optional<Work> workOpt = worksService.findOne(id);
                if (workOpt.isPresent()) {
                    Work work = workOpt.get();
                    String pathToImage = worksService.getPathToImage(work);
                    model.addAttribute("work", work);
                    model.addAttribute("pathToImage", pathToImage);
                    return "works/work";
                } else
                    return "redirect:/works";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/my")
    public String redirectToMyWorks() {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();

            switch (person.getRole()) {
                case "ROLE_DB_MANAGER" -> { return "redirect:/works/db_manager"; }
                case "ROLE_PACKER" -> { return "redirect:/works/packer"; }
                case "ROLE_DELIVERYMAN" -> { return "redirect:/works/deliveryman"; }
                case "ROLE_ADMIN" -> { return "redirect:/works/admin"; }
                default -> { return "redirect:/"; }
            }
        } else
            return "redirect:/";
    }

    @GetMapping("/db_manager")
    public String showDbManagerWorks() {
        return "works/db_manager/db_manager";
    }

    @GetMapping("/db_manager/upload_default_image")
    public String uploadDefaultImage(Model model) {
        model.addAttribute("entities", worksService.getEntities());
        model.addAttribute("workKinds", worksService.getWorkKinds());
        return "works/db_manager/upload_default_image";
    }

    @PostMapping("/db_manager/upload_default_image")
    public String uploadImage(@RequestParam("image") MultipartFile image,
                              @RequestParam("imageObject") String imageObject) throws IOException {
        fileLocationService.save(image, imageObject, true);
        return "redirect:/works/db_manager";
    }

    @GetMapping("/packer")
    public String showPackerWorks(Model model) {
        model.addAttribute("rentalsForPacking", rentalsService.findAll(RentalStatus.IS_PROCESSED, RentalStatus.BEING_PREPARED));
        model.addAttribute("rentalsForSorting", rentalsService.findAll(RentalStatus.READY_FOR_STORAGE, RentalStatus.IN_STORAGE_SORT));
        model.addAttribute("rental", new Rental());
        return "works/packer";
    }

    @PostMapping("/packer/start_packing")
    public String startPacking(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("packing_in_storage");
            worksService.save(new RentalWork(), workImage, rental, WorkKind.PACKING_IN_STORAGE);
            rentalsService.raiseStatus(rental);
        }
        return "redirect:/works/packer";
    }

    @PatchMapping("/packer/finish_packing")
    public String finishPacking(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            Optional<RentalWork> rentalWorkOpt = worksService.findOne(rental, WorkKind.PACKING_IN_STORAGE);
            if (rentalWorkOpt.isPresent()) {
                worksService.update(rentalWorkOpt.get());
                rentalsService.raiseStatus(rental);
            }
        }
        return "redirect:/works/packer";
    }

    @PostMapping("/packer/start_sorting")
    public String startSorting(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("sorting_in_storage");
            worksService.save(new RentalWork(), workImage, rental, WorkKind.SORTING_IN_STORAGE);
            rentalsService.raiseStatus(rental);
        }
        return "redirect:/works/packer";
    }

    @PatchMapping("/packer/finish_sorting")
    public String finishSorting(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            Optional<RentalWork> rentalWorkOpt = worksService.findOne(rental, WorkKind.SORTING_IN_STORAGE);
            if (rentalWorkOpt.isPresent()) {
                worksService.update(rentalWorkOpt.get());
                rentalsService.raiseStatus(rental);
                rentalsService.done(rental);
            }
        }
        return "redirect:/works/packer";
    }

    @GetMapping("/deliveryman")
    public String showDeliverymanWorks(Model model) {
        model.addAttribute("rentalsForDeliveryToClient", rentalsService.findAll(RentalStatus.READY_FOR_DELIVERY_TO_THE_CLIENT, RentalStatus.DELIVERED_TO_THE_CLIENT));
        model.addAttribute("rentalsForDeliveryToStorage", rentalsService.findAll(RentalStatus.READY_FOR_RETURN, RentalStatus.DELIVERED_TO_STORAGE));
        model.addAttribute("rental", new Rental());
        return "works/deliveryman";
    }

    @PostMapping("/deliveryman/start_delivery_to_the_client")
    public String startDeliveryToClient(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("delivery_to_the_client");
            worksService.save(new RentalWork(), workImage, rental, WorkKind.DELIVERY_TO_THE_CLIENT);
            rentalsService.raiseStatus(rental);
        }
        return "redirect:/works/deliveryman";
    }

    @PatchMapping("/deliveryman/finish_delivery_to_the_client")
    public String finishDeliveryToClient(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            Optional<RentalWork> rentalWorkOpt = worksService.findOne(rental, WorkKind.DELIVERY_TO_THE_CLIENT);
            if (rentalWorkOpt.isPresent()) {
                worksService.update(rentalWorkOpt.get());
                rentalsService.raiseStatus(rental);
            }
        }
        return "redirect:/works/deliveryman";
    }

    @PostMapping("/deliveryman/start_delivery_to_storage")
    public String startDeliveryToStorage(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("delivery_to_storage");
            worksService.save(new RentalWork(), workImage, rental, WorkKind.DELIVERY_TO_STORAGE);
            rentalsService.raiseStatus(rental);
        }
        return "redirect:/works/deliveryman";
    }

    @PatchMapping("/deliveryman/finish_delivery_to_storage")
    public String finishDeliveryToStorage(@ModelAttribute("rental") Rental rental) {
        Optional<Rental> rentalOpt = rentalsService.findOne(rental.getId());
        if (rentalOpt.isPresent()) {
            rental = rentalOpt.get();
            Optional<RentalWork> rentalWorkOpt = worksService.findOne(rental, WorkKind.DELIVERY_TO_STORAGE);
            if (rentalWorkOpt.isPresent()) {
                worksService.update(rentalWorkOpt.get());
                rentalsService.raiseStatus(rental);
            }
        }
        return "redirect:/works/deliveryman";
    }

    @GetMapping("/admin")
    public String showAdminWorks() {
        return "works/admin/admin";
    }

    @GetMapping("/admin/appoint_user")
    public String appointUser(Model model) {
        model.addAttribute("people", peopleService.findAllByRoleNotContains("ROLE_USER"));
        model.addAttribute("updatedPerson", new Person());
        return "works/admin/appoint_user";
    }

    @PatchMapping("/admin/appoint_user")
    public String updateUser(@ModelAttribute("updatedPerson") Person person) {
        person = peopleService.changeRole(person, "ROLE_USER");
        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("appoint_user");
        worksService.save(new PersonWork(), workImage, person, WorkKind.APPOINT_USER);
        return "redirect:/works/admin";
    }

    @GetMapping("/admin/appoint_db_manager")
    public String appointDbManager(Model model) {
        model.addAttribute("people", peopleService.findAllByRoleNotContains("ROLE_DB_MANAGER"));
        model.addAttribute("updatedPerson", new Person());
        return "works/admin/appoint_db_manager";
    }

    @PatchMapping("/admin/appoint_db_manager")
    public String updateDbManager(@ModelAttribute("updatedPerson") Person person) {
        person = peopleService.changeRole(person, "ROLE_DB_MANAGER");
        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("appoint_db_manager");
        worksService.save(new PersonWork(), workImage, person, WorkKind.APPOINT_DB_MANAGER);
        return "redirect:/works/admin";
    }

    @GetMapping("/admin/appoint_packer")
    public String appointPacker(Model model) {
        model.addAttribute("people", peopleService.findAllByRoleNotContains("ROLE_PACKER"));
        model.addAttribute("updatedPerson", new Person());
        return "works/admin/appoint_packer";
    }

    @PatchMapping("/admin/appoint_packer")
    public String updatePacker(@ModelAttribute("updatedPerson") Person person) {
        person = peopleService.changeRole(person, "ROLE_PACKER");
        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("appoint_packer");
        worksService.save(new PersonWork(), workImage, person, WorkKind.APPOINT_PACKER);
        return "redirect:/works/admin";
    }

    @GetMapping("/admin/appoint_deliveryman")
    public String appointDeliveryman(Model model) {
        model.addAttribute("people", peopleService.findAllByRoleNotContains("ROLE_DELIVERYMAN"));
        model.addAttribute("updatedPerson", new Person());
        return "works/admin/appoint_deliveryman";
    }

    @PatchMapping("/admin/appoint_deliveryman")
    public String updateDeliveryman(@ModelAttribute("updatedPerson") Person person) {
        person = peopleService.changeRole(person, "ROLE_DELIVERYMAN");
        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("appoint_deliveryman");
        worksService.save(new PersonWork(), workImage, person, WorkKind.APPOINT_DELIVERYMAN);
        return "redirect:/works/admin";
    }

    @GetMapping("/admin/appoint_admin")
    public String appointAdmin(Model model) {
        model.addAttribute("people", peopleService.findAllByRoleNotContains("ROLE_ADMIN"));
        model.addAttribute("updatedPerson", new Person());
        return "works/admin/appoint_admin";
    }

    @PatchMapping("/admin/appoint_admin")
    public String updateAdmin(@ModelAttribute("updatedPerson") Person person) {
        person = peopleService.changeRole(person, "ROLE_ADMIN");
        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("appoint_admin");
        worksService.save(new PersonWork(), workImage, person, WorkKind.APPOINT_ADMIN);
        return "redirect:/works/admin";
    }

    @GetMapping("/admin/show_all_rentals")
    public String showAllRentals(Model model) {
        model.addAttribute("rentals", rentalsService.findAll());
        return "works/admin/show_all_rentals";
    }

    @GetMapping("/admin/show_people")
    public String showPeople(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "works/admin/show_people";
    }
}