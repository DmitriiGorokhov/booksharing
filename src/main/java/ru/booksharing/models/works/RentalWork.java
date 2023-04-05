package ru.booksharing.models.works;

import jakarta.persistence.*;
import ru.booksharing.models.Rental;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("rental")
public class RentalWork extends Work {

    @ManyToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
    private Rental rental;

    @Column(name = "taken_to_work")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime takenToWork;

    public RentalWork() {}

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public LocalDateTime getTakenToWork() {
        return takenToWork;
    }

    public void setTakenToWork(LocalDateTime takenToWork) {
        this.takenToWork = takenToWork;
    }
}