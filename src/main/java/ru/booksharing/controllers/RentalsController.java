package ru.booksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.booksharing.models.Book;
import ru.booksharing.models.Cart;
import ru.booksharing.models.Person;
import ru.booksharing.models.Rental;
import ru.booksharing.security.PersonDetails;
import ru.booksharing.services.BooksService;
import ru.booksharing.services.CartsService;
import ru.booksharing.services.PeopleService;
import ru.booksharing.services.RentalsService;

import java.util.*;

@Controller
@RequestMapping("/rentals")
public class RentalsController {

    private final RentalsService rentalsService;
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final CartsService cartsService;

    @Autowired
    public RentalsController(RentalsService rentalsService, PeopleService peopleService, BooksService booksService, CartsService cartsService) {
        this.rentalsService = rentalsService;
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.cartsService = cartsService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();
            model.addAttribute("rentals", rentalsService.findAll(person));
            return "rentals/rentals";
        } else {
            return "profile/auth";
        }
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();
            Optional<Rental> rentalOpt = rentalsService.findOne(id);
            if (rentalOpt.isPresent()) {
                Rental rental = rentalOpt.get();
                if (rentalsService.isEmployeeOrRentalOwner(person, rental)) {
                    model.addAttribute("rental", rental);
                    return "rentals/rental";
                }
            }
            return "redirect:/rentals";
        } else
            return "profile/auth";
    }

    @GetMapping("/{id}/return_to_storage")
    public String returnToStorage(@PathVariable("id") long id) {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();
            Optional<Rental> rentalOpt = rentalsService.findOne(id);
            if (rentalOpt.isPresent()) {
                Rental rental = rentalOpt.get();
                if (rentalsService.isAdminOrRentalOwner(person, rental)) {
                    rentalsService.raiseStatus(rentalOpt.get());
                }
            }
            return "redirect:/rentals";
        } else
            return "profile/auth";
    }

    @PostMapping("/checkout")
    public String checkout() {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();
            Optional<Cart> cartOpt = cartsService.findOne(person);
            if (cartOpt.isPresent()) {
                Cart cart = cartOpt.get();
                Set<Book> books = booksService.findAll(Collections.singleton(cart));
                for (Book book : books) {
                    rentalsService.save(new Rental(), person, book);
                    Set<Cart> carts = cartsService.findAll(Collections.singleton(book));
                    for (Cart c : carts) {
                        cartsService.removeBook(c, book);
                    }
                }
            }
            return "redirect:/rentals";
        } else {
            return "redirect:/profile";
        }
    }
}