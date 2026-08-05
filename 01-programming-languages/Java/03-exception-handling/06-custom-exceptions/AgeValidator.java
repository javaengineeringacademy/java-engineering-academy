package academy.javaengineering.exceptionhandling;

public class AgeValidator {

    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 150;
    private static final int ADULT_AGE = 18;

    public static void validate(int age) throws InvalidAgeException {
        if (age < MIN_AGE) {
            throw new InvalidAgeException(age, "Age cannot be negative: " + age);
        }
        if (age > MAX_AGE) {
            throw new InvalidAgeException(age, "Age exceeds maximum: " + age);
        }
    }

    public static boolean isAdult(int age) throws InvalidAgeException {
        validate(age);
        return age >= ADULT_AGE;
    }

    public static String getAgeCategory(int age) throws InvalidAgeException {
        validate(age);
        if (age < 13) {
            return "Child";
        } else if (age < 18) {
            return "Teenager";
        } else if (age < 65) {
            return "Adult";
        } else {
            return "Senior";
        }
    }

    public static void register(String name, int age) throws InvalidAgeException {
        validate(age);
        System.out.println("Registered: " + name + " (Age: " + age + ")");
    }

    public static void main(String[] args) {
        try {
            validate(-5);
        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Invalid age value: " + e.getAge());
        }

        try {
            boolean adult = isAdult(20);
            System.out.println("Is adult: " + adult);
        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            String category = getAgeCategory(25);
            System.out.println("Category: " + category);
        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
