package ru.booksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.booksharing.models.*;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.WorkImage;
import ru.booksharing.models.works.*;
import ru.booksharing.repositories.works.*;
import ru.booksharing.security.PersonDetails;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class WorksService {

    private final WorksRepository worksRepository;
    private final RentalWorksRepository rentalWorksRepository;
    private final AuthorWorksRepository authorWorksRepository;
    private final BookWorksRepository bookWorksRepository;
    private final GenreWorksRepository genreWorksRepository;
    private final LanguageWorksRepository languageWorksRepository;
    private final PublishingHouseWorksRepository publishingHouseWorksRepository;
    private final StorageWorksRepository storageWorksRepository;
    private final TranslatorWorksRepository translatorWorksRepository;
    private final PersonWorksRepository personWorksRepository;

    @Autowired
    public WorksService(WorksRepository worksRepository, RentalWorksRepository rentalWorksRepository, AuthorWorksRepository authorWorksRepository, BookWorksRepository bookWorksRepository, GenreWorksRepository genreWorksRepository, LanguageWorksRepository languageWorksRepository, PublishingHouseWorksRepository publishingHouseWorksRepository, StorageWorksRepository storageWorksRepository, TranslatorWorksRepository translatorWorksRepository, PersonWorksRepository personWorksRepository) {
        this.worksRepository = worksRepository;
        this.rentalWorksRepository = rentalWorksRepository;
        this.authorWorksRepository = authorWorksRepository;
        this.bookWorksRepository = bookWorksRepository;
        this.genreWorksRepository = genreWorksRepository;
        this.languageWorksRepository = languageWorksRepository;
        this.publishingHouseWorksRepository = publishingHouseWorksRepository;
        this.storageWorksRepository = storageWorksRepository;
        this.translatorWorksRepository = translatorWorksRepository;
        this.personWorksRepository = personWorksRepository;
    }

    public Set<Work> findAll() {
        return new HashSet<>(worksRepository.findAll());
    }

    public Optional<Work> findOne(long id) {
        return worksRepository.findById(id);
    }

    public Optional<RentalWork> findOne(Rental rental, WorkKind workKind) {
        return rentalWorksRepository.findByRentalAndWorkKind(rental, workKind);
    }

    @Transactional
    public void save(AuthorWork authorWork, WorkImage workImage, Author author, WorkKind workKind) {
        authorWork.setAuthor(author);
        setCommonFields(authorWork, workImage, workKind);
        setEndTime(authorWork);
        authorWorksRepository.save(authorWork);
    }

    @Transactional
    public void save(BookWork bookWork, WorkImage workImage, Book book, WorkKind workKind) {
        bookWork.setBook(book);
        setCommonFields(bookWork, workImage, workKind);
        setEndTime(bookWork);
        bookWorksRepository.save(bookWork);
    }

    @Transactional
    public void save(GenreWork genreWork, WorkImage workImage, Genre genre, WorkKind workKind) {
        genreWork.setGenre(genre);
        setCommonFields(genreWork, workImage, workKind);
        setEndTime(genreWork);
        genreWorksRepository.save(genreWork);
    }

    @Transactional
    public void save(LanguageWork languageWork, WorkImage workImage, Language language, WorkKind workKind) {
        languageWork.setLanguage(language);
        setCommonFields(languageWork, workImage, workKind);
        setEndTime(languageWork);
        languageWorksRepository.save(languageWork);
    }

    @Transactional
    public void save(PublishingHouseWork publishingHouseWork, WorkImage workImage, PublishingHouse publishingHouse, WorkKind workKind) {
        publishingHouseWork.setPublishingHouse(publishingHouse);
        setCommonFields(publishingHouseWork, workImage, workKind);
        setEndTime(publishingHouseWork);
        publishingHouseWorksRepository.save(publishingHouseWork);
    }

    @Transactional
    public void save(RentalWork rentalWork, WorkImage workImage, Rental rental, WorkKind workKind) {
        rentalWork.setRental(rental);
        setCommonFields(rentalWork, workImage, workKind);
        rentalWork.setTakenToWork(LocalDateTime.now());
        rentalWorksRepository.save(rentalWork);
    }

    @Transactional
    public void save(StorageWork storageWork, WorkImage workImage, Storage storage, WorkKind workKind) {
        storageWork.setStorage(storage);
        setCommonFields(storageWork, workImage, workKind);
        setEndTime(storageWork);
        storageWorksRepository.save(storageWork);
    }

    @Transactional
    public void save(TranslatorWork translatorWork, WorkImage workImage, Translator translator, WorkKind workKind) {
        translatorWork.setTranslator(translator);
        setCommonFields(translatorWork, workImage, workKind);
        setEndTime(translatorWork);
        translatorWorksRepository.save(translatorWork);
    }

    @Transactional
    public void save(PersonWork personWork, WorkImage workImage, Person person, WorkKind workKind) {
        personWork.setPersonUpdated(person);
        setCommonFields(personWork, workImage, workKind);
        setEndTime(personWork);
        personWorksRepository.save(personWork);
    }

    @Transactional
    public void update(Work work) {
       setEndTime(work);
       worksRepository.save(work);
    }

    public String getPathToImage(Work work) {
        try {
            String pathToURL = Paths.get(work.getImage().getLocation()).toUri().toURL().toString();
            String[] pathParts = pathToURL.split("static");
            return pathParts[pathParts.length - 1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public SortedMap<String, String> getEntities() {
        SortedMap<String, String> entities = new TreeMap<>();
        entities.put("author", "Автор");
        entities.put("book", "Книга");
        entities.put("genre", "Жанр");
        entities.put("language", "Язык");
        entities.put("person", "Пользователь");
        entities.put("publishing_house", "Издательство");
        entities.put("storage", "Хранилище");
        entities.put("translator", "Переводчик");
        return entities;
    }

    public SortedMap<String, String> getWorkKinds() {
        SortedMap<String, String> entities = new TreeMap<>();
        entities.put("adding_author", "Добавление автора");
        entities.put("adding_book", "Добавление книги");
        entities.put("adding_genre", "Добавление жанра");
        entities.put("adding_language", "Добавление языка");
        entities.put("adding_publishing_house", "Добавление издательства");
        entities.put("adding_storage", "Добавление хранилища");
        entities.put("adding_translator", "Добавление переводчика");
        entities.put("appoint_user", "Назначение пользователем");
        entities.put("appoint_db_manager", "Назначение менеджером БД");
        entities.put("appoint_packer", "Назначение специалистом по упаковке");
        entities.put("appoint_deliveryman", "Назначение специалистом по доставке");
        entities.put("appoint_admin", "Назначение администратором");
        entities.put("packing_in_storage", "Упаковка в хранилище");
        entities.put("sorting_in_storage", "Сортировка в хранилище");
        entities.put("delivery_to_the_client", "Доставка к клиенту");
        entities.put("delivery_to_storage", "Доставка в хранилище");
        return entities;
    }

    private Person getCurrentEmployee() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((PersonDetails) principal).getPerson();
    }

    private void setCommonFields(Work work, WorkImage workImage, WorkKind workKind) {
        work.setPerson(getCurrentEmployee());
        work.setWorkKind(workKind);
        work.setImage(workImage);
    }

    private void setEndTime(Work work) {
        work.setDone(LocalDateTime.now());
    }

    public boolean isEmployee(Person person) {
        return !person.getRole().equals("ROLE_USER");
    }
}