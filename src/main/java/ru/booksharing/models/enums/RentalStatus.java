package ru.booksharing.models.enums;

public enum RentalStatus {
    IS_PROCESSED("Оформлен") ,
    BEING_PREPARED("Подготавливается"),
    READY_FOR_DELIVERY_TO_THE_CLIENT("Готов для доставки к клиенту"),
    DELIVERED_TO_THE_CLIENT("Доставляется к клиенту"),
    AT_THE_CLIENT("У клиента"),
    READY_FOR_RETURN("Готов для возврата"),
    DELIVERED_TO_STORAGE("Доставляется в хранилище"),
    READY_FOR_STORAGE("Готов для хранилища"),
    IN_STORAGE_SORT("В сортировке хранилища"),
    COMPLETED("Завершен");

    final String description;

    RentalStatus(String description) {
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