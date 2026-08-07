import java.util.Objects;

public final class Solution1 {
    public static void main(String[] args) {
        Person alice = new Person("Alice", 30, "alice@example.com");
        Person olderAlice = alice.withAge(31);

        System.out.println("Original: " + alice);
        System.out.println("With new age: " + olderAlice);
        System.out.println("Same reference? " + (alice == olderAlice));
        System.out.println("Same name? " + alice.getName().equals(olderAlice.getName()));
    }
}

final class Person {
    private final String name;
    private final int age;
    private final String email;

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    public Person withAge(int newAge) {
        return new Person(this.name, newAge, this.email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return age == person.age &&
               Objects.equals(name, person.name) &&
               Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }
}
