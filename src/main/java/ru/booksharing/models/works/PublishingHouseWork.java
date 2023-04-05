package ru.booksharing.models.works;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ru.booksharing.models.PublishingHouse;

@Entity
@DiscriminatorValue("publishing_house")
public class PublishingHouseWork extends Work {

    @ManyToOne
    @JoinColumn(name = "publishing_house_id", referencedColumnName = "id")
    private PublishingHouse publishingHouse;

    public PublishingHouseWork() {}

    public PublishingHouse getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(PublishingHouse publishingHouse) {
        this.publishingHouse = publishingHouse;
    }
}