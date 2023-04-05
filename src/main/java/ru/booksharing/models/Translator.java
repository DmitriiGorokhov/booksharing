package ru.booksharing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.images.TranslatorImage;
import ru.booksharing.models.works.TranslatorWork;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Translator")
public class Translator {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String fullName;

    @Column(name = "description")
    @NotNull(message = "Необходимо указать краткое описание")
    private String description;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "translator", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Book> books;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private TranslatorImage image;

    @OneToMany(mappedBy = "translator")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<TranslatorWork> translatorWorks;

    public Translator() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public TranslatorImage getImage() {
        return image;
    }

    public void setImage(TranslatorImage image) {
        this.image = image;
    }

    public Set<TranslatorWork> getTranslatorWorks() {
        return translatorWorks;
    }

    public void setTranslatorWorks(Set<TranslatorWork> translatorWorks) {
        this.translatorWorks = translatorWorks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translator that = (Translator) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}