package ru.booksharing.models.enums;

public enum Cover {
    HARD("Твердая"),
    SOFT("Мягкая");

    final String description;

    Cover(String description) {
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