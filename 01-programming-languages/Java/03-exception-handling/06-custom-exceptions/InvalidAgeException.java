package academy.javaengineering.exceptionhandling;

public class InvalidAgeException extends Exception {

    private final int age;

    public InvalidAgeException(String message) {
        super(message);
        this.age = -1;
    }

    public InvalidAgeException(int age, String message) {
        super(message);
        this.age = age;
    }

    public InvalidAgeException(int age, String message, Throwable cause) {
        super(message, cause);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "InvalidAgeException{age=" + age + ", message=" + getMessage() + "}";
    }
}
