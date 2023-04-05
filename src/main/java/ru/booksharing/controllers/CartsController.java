package ru.booksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.booksharing.models.Book;
import ru.booksharing.models.Cart;
import ru.booksharing.models.Person;
import ru.booksharing.security.PersonDetails;
import ru.booksharing.services.BooksService;
import ru.booksharing.services.CartsService;
import ru.booksharing.services.PeopleService;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartsController {

    private final CartsService cartsService;
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public CartsController(CartsService cartsService, PeopleService peopleService, BooksService booksService) {
        this.cartsService = cartsService;
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping("")
    public String show(Model model) {
        Map<Boolean, Object> principal = peopleService.checkForAuthentication();
        if (principal.containsKey(true)) {
            Person person = ((PersonDetails) principal.get(true)).getPerson();
            Optional<Cart> cartOpt = cartsService.findOne(person);
            Set<Book> books;
            if (cartOpt.isPresent()) {
                Cart cart = cartOpt.get();
                books = booksService.findAll(Collections.singleton(cart));
            } else
                books = Collections.emptySet();
            model.addAttribute("books", books);
            return "cart/cart";
        } else {
            return "profile/auth";
        }
    }
}