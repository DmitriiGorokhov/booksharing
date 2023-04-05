package ru.booksharing.models.images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.Person;

import java.util.Set;

@Entity
@DiscriminatorValue("person")
public class PersonImage extends Image {

    @OneToMany(mappedBy = "image")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Person> people;

    public PersonImage() {}

    public PersonImage(String imageName, String location) {
        super(imageName, location);
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }
}