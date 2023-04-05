package ru.booksharing.models.images;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "Image")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("null")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "name")
    @NotNull(message = "Необходимо указать наименование")
    @Size(min = 1, max = 100, message = "Наименование изображения должно содержать от 1 до 100 символов")
    protected String name;

    @Column(name = "location")
    @NotNull(message = "Необходимо указать местоположение")
    protected String location;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public Image() {}

    public Image(long id) {
        this.id = id;
    }

    public Image(String imageName, String location) {
        this.name = imageName;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}