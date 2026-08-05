package academy.javaengineering.oop.enums;

public enum Season {

    SPRING("Warm"),
    SUMMER("Hot"),
    FALL("Cool"),
    WINTER("Cold");

    private final String description;

    Season(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }

    public boolean isWarm() {
        return this == SPRING || this == SUMMER;
    }
}
