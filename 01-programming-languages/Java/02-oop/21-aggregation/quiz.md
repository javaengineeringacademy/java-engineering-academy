# Quiz: Aggregation

## Multiple Choice Questions

1. What is aggregation in Java?
   - A) A "has-a" relationship where the contained object can exist independently
   - B) A "is-a" relationship
   - C) Multiple inheritance
   - D) A method override

2. How does aggregation differ from composition?
   - A) Aggregation has stronger lifecycle dependency
   - B) Aggregation has weaker lifecycle dependency — contained objects can outlive the container
   - C) They are identical
   - D) Aggregation uses extends keyword

3. Which is an example of aggregation?
   - A) A library has books (books can exist without the library)
   - B) A house has rooms (rooms cannot exist without the house)
   - C) A car has an engine (engine destroyed with car)
   - D) A file has bytes

4. In aggregation, the contained object is typically:
   - A) Created inside the container
   - B) Passed to the container from outside
   - C) Always null
   - D) Static

5. Aggregation is a special case of:
   - A) Inheritance
   - B) Polymorphism
   - C) Association
   - D) Encapsulation

## True/False Questions

6. In aggregation, destroying the container also destroys the contained object.
   - True / False

7. Aggregation represents a weaker relationship than composition.
   - True / False

8. A university department having professors is an example of aggregation.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Book {
    private String title;
    Book(String title) { this.title = title; }
    String getTitle() { return title; }
}
class Library {
    private String name;
    private Book[] books;
    Library(String name, Book[] books) { this.name = name; this.books = books; }
    void listBooks() {
        System.out.println(name + " has:");
        for (Book b : books)
            System.out.println("  - " + b.getTitle());
    }
}
class Test {
    public static void main(String[] args) {
        Book b1 = new Book("Java Basics");
        Book b2 = new Book("OOP Design");
        Library lib = new Library("City Library", new Book[]{b1, b2});
        lib.listBooks();
        System.out.println("Book still exists: " + b1.getTitle());
    }
}
```

10. What will this code print?
```java
class Patient {
    private String name;
    Patient(String name) { this.name = name; }
    String getName() { return name; }
}
class Hospital {
    private String name;
    Hospital(String name) { this.name = name; }
    void treat(Patient p) {
        System.out.println(name + " is treating " + p.getName());
    }
}
class Test {
    public static void main(String[] args) {
        Patient p = new Patient("John");
        Hospital h = new Hospital("City Hospital");
        h.treat(p);
        System.out.println("Patient " + p.getName() + " leaves hospital");
        h.treat(p);
    }
}
```

## Answers

1. A
2. B
3. A - Books can exist independently of the library
4. B - Typically passed in via constructor or setter
5. C
6. False - In aggregation, contained objects can survive
7. True
8. True - Professors exist independently of the department
9. Output:
```
City Library has:
  - Java Basics
  - OOP Design
Book still exists: Java Basics
```
10. Output:
```
City Hospital is treating John
Patient John leaves hospital
City Hospital is treating John
```
