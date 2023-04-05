package ru.booksharing.models.images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import ru.booksharing.models.Translator;

import java.util.Set;

@Entity
@DiscriminatorValue("translator")
public class TranslatorImage extends Image {

    @OneToMany(mappedBy = "image")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Translator> translator;

    public TranslatorImage() {}

    public TranslatorImage(String imageName, String location) {
        super(imageName, location);
    }

    public Set<Translator> getTranslator() {
        return translator;
    }

    public void setTranslator(Set<Translator> translator) {
        this.translator = translator;
    }
}