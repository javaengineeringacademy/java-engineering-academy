package academy.javaengineering.testing;

import java.util.Objects;

/**
 * Test Design - Given-When-Then, Test Data Builders.
 */
public class TestDesignExample {

    public static class User {
        private final String name;
        private final String email;
        private final int age;

        private User(String name, String email, int age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public int getAge() { return age; }

        public static class Builder {
            private String name = "Default";
            private String email = "default@test.com";
            private int age = 25;

            public Builder name(String name) { this.name = name; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder age(int age) { this.age = age; return this; }
            public User build() { return new User(name, email, age); }
        }
    }

    public boolean validateEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public boolean canVote(User user) {
        return user != null && user.getAge() >= 18;
    }

    public static void main(String[] args) {
        TestDesignExample example = new TestDesignExample();
        User user = new User.Builder().name("John").email("john@test.com").age(25).build();
        System.out.println("User: " + user.getName());
        System.out.println("Valid Email: " + example.validateEmail("test@test.com"));
        System.out.println("Can Vote: " + example.canVote(user));
    }
}
