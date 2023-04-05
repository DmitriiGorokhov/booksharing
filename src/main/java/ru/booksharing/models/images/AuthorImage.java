package ru.booksharing.models.images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.Author;

import java.util.Set;

@Entity
@DiscriminatorValue("author")
public class AuthorImage extends Image {

    @OneToMany(mappedBy = "image")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Author> authors;

    public AuthorImage() {}

    public AuthorImage(long id) {
        super(id);
    }

    public AuthorImage(String imageName, String location) {
        super(imageName, location);
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}