# Quiz: Association

## Multiple Choice Questions

1. What is association in Java?
   - A) A relationship between two classes where objects are linked
   - B) A type of inheritance
   - C) A method of creating objects
   - D) A way to access private members

2. Which is an example of a one-to-many association?
   - A) A teacher teaches many students
   - B) A student has one ID
   - C) A car has one engine
   - D) A name has one string

3. What does the arrow direction represent in a UML association?
   - A) The direction of the relationship/navigation
   - B) The access modifier
   - C) The data type
   - D) The method call

4. Association can be:
   - A) One-to-one only
   - B) One-to-many only
   - C) One-to-one, one-to-many, many-to-many
   - D) Many-to-many only

5. What distinguishes association from aggregation?
   - A) Association implies stronger ownership
   - B) Aggregation implies a weaker "has-a" without lifecycle control
   - C) They are exactly the same
   - D) Association uses inheritance

## True/False Questions

6. Association creates a direct dependency between the participating classes.
   - True / False

7. Bidirectional association means both classes know about each other.
   - True / False

8. Association always implies ownership of one class over another.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Teacher {
    private String name;
    Teacher(String name) { this.name = name; }
    String getName() { return name; }
}
class Student {
    private String name;
    private Teacher mentor;
    Student(String name, Teacher mentor) {
        this.name = name;
        this.mentor = mentor;
    }
    void showMentor() {
        System.out.println(name + "'s mentor is " + mentor.getName());
    }
}
class Test {
    public static void main(String[] args) {
        Teacher t = new Teacher("Prof. Smith");
        Student s = new Student("Alice", t);
        s.showMentor();
    }
}
```

10. What will this code print?
```java
class Person {
    private String name;
    Person(String name) { this.name = name; }
    String getName() { return name; }
}
class Department {
    private String name;
    private Person head;
    Department(String name, Person head) { this.name = name; this.head = head; }
    void showInfo() { System.out.println(name + " - Head: " + head.getName()); }
}
class Test {
    public static void main(String[] args) {
        Person p = new Person("Dr. Jones");
        Department d = new Department("CS", p);
        d.showInfo();
    }
}
```

## Answers

1. A
2. A
3. A
4. C
5. B - Aggregation is a specific type of association with weaker ownership
6. True
7. True
8. False - Association just implies a relationship, not ownership
9. Output:
```
Alice's mentor is Prof. Smith
```
10. Output:
```
CS - Head: Dr. Jones
```
