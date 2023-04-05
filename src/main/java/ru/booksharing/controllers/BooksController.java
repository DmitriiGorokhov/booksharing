package ru.booksharing.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.booksharing.models.*;
import ru.booksharing.models.enums.AgeLimit;
import ru.booksharing.models.enums.Cover;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.BookImage;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.models.works.BookWork;
import ru.booksharing.security.PersonDetails;
import ru.booksharing.services.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final AuthorsService authorsService;
    private final GenresService genresService;
    private final LanguagesService languagesService;
    private final PublishingHousesService publishingHousesService;
    private final TranslatorsService translatorsService;
    private final StoragesService storagesService;
    private final WorksService worksService;
    private final FileLocationService fileLocationService;
    private final PeopleService peopleService;
    private final CartsService cartsService;

    @Autowired
    public BooksController(BooksService booksService, AuthorsService authorsService, GenresService genresService, LanguagesService languagesService, PublishingHousesService publishingHousesService, TranslatorsService translatorsService, StoragesService storagesService, WorksService worksService, FileLocationService fileLocationService, PeopleService peopleService, CartsService cartsService) {
        this.booksService = booksService;
        this.authorsService = authorsService;
        this.genresService = genresService;
        this.languagesService = languagesService;
        this.publishingHousesService = publishingHousesService;
        this.translatorsService = translatorsService;
        this.storagesService = storagesService;
        this.worksService = worksService;
        this.fileLocationService = fileLocationService;
        this.peopleService = peopleService;
        this.cartsService = cartsService;
    }

    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("books", booksService.findAll());
        return "books/books";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        Optional<Book> bookOpt = booksService.findOne(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            model.addAttribute("book", book);

            String pathToImage = booksService.getPathToImage(book);
            model.addAttribute("pathToImage", pathToImage);

            boolean inCart = false;
            Map<Boolean, Object> principal = peopleService.checkForAuthentication();
            if (principal.containsKey(true)) {
                Person person = ((PersonDetails) principal.get(true)).getPerson();
                Optional<Cart> cartOpt = cartsService.findOne(person);
                if (cartOpt.isPresent()) {
                    Cart cart = cartOpt.get();
                    Set<Book> books = booksService.findAll(Collections.singleton(cart));
                    inCart = books.contains(book);
                }
            }
            model.addAttribute("inCart", inCart);

            return "books/book";
        } else
            return "redirect:/books";
    }

    @GetMapping("/{id}/add_to_cart")
    public String addToCart(@PathVariable("id") long id) {
        Optional<Book> bookOpt = booksService.findOne(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();

            Map<Boolean, Object> principal = peopleService.checkForAuthentication();
            if (principal.containsKey(true)) {
                Person person = ((PersonDetails) principal.get(true)).getPerson();
                cartsService.addBook(person, book);
            }
            return "redirect:/cart";
        } else
            return "redirect:/books";
    }

    @GetMapping("/{id}/remove_from_cart")
    public String removeFromCart(@PathVariable("id") long id) {
        Optional<Book> bookOpt = booksService.findOne(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();

            Map<Boolean, Object> principal = peopleService.checkForAuthentication();
            if (principal.containsKey(true)) {
                Person person = ((PersonDetails) principal.get(true)).getPerson();
                cartsService.removeBook(person, book);
            }
            return "redirect:/cart";
        } else
            return "redirect:/books";
    }


    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("book", new Book());
        setAttributesForModel(model);
        return "books/new";
    }

    @PostMapping("/new")
    public String create(Model model,
                         @RequestParam("image") MultipartFile image,
                         @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            setAttributesForModel(model);
            return "books/new";
        }

        String imageObject = "book";
        BookImage bookImage;
        if (!image.isEmpty())
            bookImage = (BookImage) fileLocationService.save(image, imageObject, false);
        else
            bookImage = (BookImage) fileLocationService.findDefaultImage(imageObject);

        book = booksService.save(book, bookImage);

        WorkImage workImage = (WorkImage) fileLocationService.findDefaultImage("adding_book");
        worksService.save(new BookWork(), workImage, book, WorkKind.ADDING_BOOK);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(name = "text") String text) {
        model.addAttribute("books", booksService.findAllByTitleContaining(text));
        model.addAttribute("authors", authorsService.findAllByFullNameContaining(text));
        model.addAttribute("genres", genresService.findAllByNameContaining(text));
        model.addAttribute("publishingHouses", publishingHousesService.findAllByNameContaining(text));
        model.addAttribute("translators", translatorsService.findAllByFullNameContaining(text));
        return "books/search";
    }

    private void setAttributesForModel(Model model) {
        model.addAttribute("books", booksService.findAll());
        model.addAttribute("authors", authorsService.findAll());
        model.addAttribute("genres", genresService.findAll());
        model.addAttribute("languages", languagesService.findAll());
        model.addAttribute("covers", Cover.values());
        model.addAttribute("publishingHouses", publishingHousesService.findAll());
        model.addAttribute("translators", translatorsService.findAll());
        model.addAttribute("ageLimits", AgeLimit.values());
        model.addAttribute("storages", storagesService.findAll());
    }
}