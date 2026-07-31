package academy.javaengineering.oop.bank;

/**
 * Address value object.
 */
public record Address(
    String street,
    String city,
    String state,
    String zipCode,
    String country
) {
    public Address {
        Objects.requireNonNull(street);
        Objects.requireNonNull(city);
        Objects.requireNonNull(state);
        Objects.requireNonNull(zipCode);
        Objects.requireNonNull(country);
    }

    @Override
    public String toString() {
        return "%s, %s, %s %s, %s".formatted(street, city, state, zipCode, country);
    }
}