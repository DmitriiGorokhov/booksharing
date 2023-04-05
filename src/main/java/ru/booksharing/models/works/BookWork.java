package ru.booksharing.models.works;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ru.booksharing.models.Book;

@Entity
@DiscriminatorValue("book")
public class BookWork extends Work {

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public BookWork() {}

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}