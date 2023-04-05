package ru.booksharing.models.works;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ru.booksharing.models.Translator;

@Entity
@DiscriminatorValue("translator")
public class TranslatorWork extends Work {

    @ManyToOne
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    private Translator translator;

    public TranslatorWork() {}

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }
}