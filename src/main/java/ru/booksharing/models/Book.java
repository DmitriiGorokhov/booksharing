package ru.booksharing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.enums.AgeLimit;
import ru.booksharing.models.enums.Cover;
import ru.booksharing.models.images.BookImage;
import ru.booksharing.models.works.BookWork;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    @NotNull(message = "Необходимо указать наименование")
    @Size(min = 1, max = 100, message = "Наименование книги должно содержать от 1 до 100 символов")
    private String title;

    @ManyToOne
    @NotNull(message = "Необходимо указать автора")
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    @ManyToOne
    @NotNull(message = "Необходимо указать жанр")
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    private Genre genre;

    @ManyToOne
    @NotNull(message = "Необходимо указать язык")
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "cover")
    @NotNull(message = "Необходимо указать тип обложки")
    private Cover cover;

    @Column(name = "year_of_publishing")
    @NotNull(message = "Необходимо указать год издания")
    private int yearOfPublishing;

    @ManyToOne
    @NotNull(message = "Необходимо указать издательство")
    @JoinColumn(name = "publishing_house_id", referencedColumnName = "id")
    private PublishingHouse publishingHouse;           

    @ManyToOne
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    private Translator translator;

    @Column(name = "isbn")
    @NotNull(message = "Необходимо указать ISBN")
    private String isbn;

    @Column(name = "number_of_pages")
    @NotNull(message = "Необходимо указать количество страниц")
    private int numberOfPages;

    @Column(name = "length")
    @NotNull(message = "Необходимо указать длину в мм")
    @Min(value = 0)
    @Max(value = 1_000)
    private int length;

    @Column(name = "width")
    @NotNull(message = "Необходимо указать ширину в мм")
    @Min(value = 0)
    @Max(value = 1_000)
    private int width;

    @Column(name = "height")
    @NotNull(message = "Необходимо указать высоту в мм")
    @Min(value = 0)
    @Max(value = 1_000)
    private int height;

    @Column(name = "weight")
    @NotNull(message = "Необходимо указать вес в граммах")
    @Min(value = 0)
    @Max(value = 10_000)
    private int weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_limit")
    @NotNull(message = "Необходимо указать возрастное ограничение")
    private AgeLimit ageLimit;

    @Column(name = "description")
    @NotNull(message = "Необходимо указать краткое описание")
    private String description;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne
    @NotNull(message = "Необходимо указать местоположение хранилища")
    @JoinColumn(name = "storage_id", referencedColumnName = "id")
    private Storage storage;

    @OneToMany(mappedBy = "book")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Rental> rentals;

    @ManyToOne
    @NotNull(message = "Необходимо указать изображение")
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private BookImage image;

    @OneToMany(mappedBy = "book")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<BookWork> bookWorks;

    @ManyToMany(mappedBy = "books")
    private Set<Cart> carts;

    public Book() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(int yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public PublishingHouse getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(PublishingHouse publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String ISBN) {
        this.isbn = ISBN;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public AgeLimit getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(AgeLimit ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public BookImage getImage() {
        return image;
    }

    public void setImage(BookImage image) {
        this.image = image;
    }

    public Set<BookWork> getBookWorks() {
        return bookWorks;
    }

    public void setBookWorks(Set<BookWork> bookWorks) {
        this.bookWorks = bookWorks;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}