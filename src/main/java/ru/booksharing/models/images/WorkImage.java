package ru.booksharing.models.images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.works.Work;

import java.util.Set;

@Entity
@DiscriminatorValue("work")
public class WorkImage extends Image {

    @OneToMany(mappedBy = "image")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Work> works;

    public WorkImage() {}

    public WorkImage(long id) {
        super(id);
    }

    public WorkImage(String imageName, String location) {
        super(imageName, location);
    }

    public Set<Work> getWorks() {
        return works;
    }

    public void setWorks(Set<Work> works) {
        this.works = works;
    }
}