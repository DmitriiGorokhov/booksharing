package ru.booksharing.models.enums;

public enum AgeLimit {
    UNDER_THE_AGE_OF_6_YEARS("0+"),
    OVER_THE_AGE_OF_6_YEARS("6+"),
    OVER_THE_AGE_OF_12_YEARS("12+"),
    OVER_THE_AGE_OF_16_YEARS("16+"),
    OVER_THE_AGE_OF_18_YEARS("18+");

    final String description;

    AgeLimit(String description) {
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