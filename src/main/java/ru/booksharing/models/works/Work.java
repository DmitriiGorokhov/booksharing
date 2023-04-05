package ru.booksharing.models.works;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ru.booksharing.models.Person;
import ru.booksharing.models.enums.WorkKind;
import ru.booksharing.models.images.WorkImage;

import java.time.LocalDateTime;

@Entity
@Table(name = "Work")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "work_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("null")
public class Work {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @NotNull(message = "Необходимо указать работника")
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_kind")
    @NotNull(message = "Необходимо указать тип работы")
    private WorkKind workKind;

    @Column(name = "done")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime done;

    @ManyToOne
    @NotNull(message = "Необходимо указать изображение")
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private WorkImage image;

    public Work() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public WorkKind getWorkKind() {
        return workKind;
    }

    public void setWorkKind(WorkKind workKind) {
        this.workKind = workKind;
    }

    public LocalDateTime getDone() {
        return done;
    }

    public void setDone(LocalDateTime done) {
        this.done = done;
    }

    public WorkImage getImage() {
        return image;
    }

    public void setImage(WorkImage image) {
        this.image = image;
    }
}