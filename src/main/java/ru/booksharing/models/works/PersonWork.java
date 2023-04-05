package ru.booksharing.models.works;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ru.booksharing.models.Person;

@Entity
@DiscriminatorValue("person")
public class PersonWork extends Work {

    @ManyToOne
    @JoinColumn(name = "person_updated_id", referencedColumnName = "id")
    private Person personUpdated;

    public PersonWork() {}

    public Person getPersonUpdated() {
        return personUpdated;
    }

    public void setPersonUpdated(Person personUpdated) {
        this.personUpdated = personUpdated;
    }
}