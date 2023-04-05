package ru.booksharing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.enums.RentalStatus;
import ru.booksharing.models.works.RentalWork;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Rental")
public class Rental {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @NotNull(message = "Необходимо указать клиента")
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @ManyToOne
    @NotNull(message = "Необходимо указать книгу")
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "done")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime done;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NotNull(message = "Необходимо указать статус")
    private RentalStatus rentalStatus;

    @OneToMany(mappedBy = "rental")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<RentalWork> rentalWorks;

    public Rental() {}

    public Rental(long id) {
        this.id = id;
    }

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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDone() {
        return done;
    }

    public void setDone(LocalDateTime done) {
        this.done = done;
    }

    public RentalStatus getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Set<RentalWork> getRentalWorks() {
        return rentalWorks;
    }

    public void setRentalWorks(Set<RentalWork> rentalWorks) {
        this.rentalWorks = rentalWorks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return id == rental.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}