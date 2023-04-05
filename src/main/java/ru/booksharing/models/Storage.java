package ru.booksharing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.images.StorageImage;
import ru.booksharing.models.works.StorageWork;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Storage")
public class Storage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "location") // TODO: добавить regex
    @NotNull(message = "Необходимо указать адрес хранилища")
    private String location;

    @Column(name = "description")
    @NotNull(message = "Необходимо указать краткое описание")
    private String description;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "storage", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Book> books;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private StorageImage image;

    @OneToMany(mappedBy = "storage")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<StorageWork> storageWorks;

    public Storage() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public StorageImage getImage() {
        return image;
    }

    public void setImage(StorageImage image) {
        this.image = image;
    }

    public Set<StorageWork> getStorageWorks() {
        return storageWorks;
    }

    public void setStorageWorks(Set<StorageWork> storageWorks) {
        this.storageWorks = storageWorks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return id == storage.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}