package ru.booksharing.models.images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.PublishingHouse;

import java.util.Set;

@Entity
@DiscriminatorValue("publishing_house")
public class PublishingHouseImage extends Image {

    @OneToMany(mappedBy = "image")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<PublishingHouse> publishingHouses;

    public PublishingHouseImage() {}

    public PublishingHouseImage(String imageName, String location) {
        super(imageName, location);
    }

    public Set<PublishingHouse> getPublishingHouses() {
        return publishingHouses;
    }

    public void setPublishingHouses(Set<PublishingHouse> publishingHouses) {
        this.publishingHouses = publishingHouses;
    }
}