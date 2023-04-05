package ru.booksharing.models.enums;

public enum WorkKind {
    PACKING_IN_STORAGE("Упаковка в хранилище"),
    DELIVERY_TO_THE_CLIENT("Доставка к клиенту"),
    DELIVERY_TO_STORAGE("Доставка в хранилище"),
    SORTING_IN_STORAGE("Сортировка в хранилище"),
    ADDING_BOOK("Добавление книги"),
    ADDING_AUTHOR("Добавление автора"),
    ADDING_GENRE("Добавление жанра"),
    ADDING_LANGUAGE("Добавление языка"),
    ADDING_PUBLISHING_HOUSE("Добавление издательства"),
    ADDING_TRANSLATOR("Добавление переводчика"),
    ADDING_STORAGE("Добавление хранилища"),
    APPOINT_USER("Назначение пользователем"),
    APPOINT_DB_MANAGER("Назначение менеджером БД"),
    APPOINT_PACKER("Назначение специалистом по упаковке"),
    APPOINT_DELIVERYMAN("Назначение специалистом по доставке"),
    APPOINT_ADMIN("Назначение администратором");

    final String description;

    WorkKind(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}