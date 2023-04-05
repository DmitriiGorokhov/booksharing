package ru.booksharing.models.works;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ru.booksharing.models.Storage;

@Entity
@DiscriminatorValue("storage")
public class StorageWork extends Work {

    @ManyToOne
    @JoinColumn(name = "storage_id", referencedColumnName = "id")
    private Storage storage;

    public StorageWork() {}

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}