# Quiz: Sorting

## Multiple Choice Questions

1. Which interface must a class implement to be sorted using Collections.sort()?
   - A) Comparable
   - B) Runnable
   - C) Serializable
   - D) Cloneable

2. What method must be defined in the Comparable interface?
   - A) compare()
   - B) compareTo()
   - C) equals()
   - D) sort()

3. What does compareTo() return when the current object is less than the argument?
   - A) A positive number
   - B) A negative number
   - C) Zero
   - D) null

4. Which class provides a static sort() method for arrays?
   - A) Arrays
   - B) Collections
   - C) List
   - D) Object

5. What is the Comparator interface used for?
   - A) Defining natural ordering inside the class
   - B) Defining custom ordering externally
   - C) Comparing primitive types
   - D) Creating sorted sets

## True/False Questions

6. The Comparable interface has only one abstract method.
   - True / False

7. You can sort a List of objects that don't implement Comparable using Collections.sort().
   - True / False

8. Comparator can be used to sort in descending order.
   - True / False

## Code Output Questions

9. What will this code print?
```java
import java.util.*;
class Student implements Comparable<Student> {
    String name;
    int grade;
    Student(String name, int grade) { this.name = name; this.grade = grade; }
    public int compareTo(Student other) { return this.grade - other.grade; }
    public String toString() { return name + ":" + grade; }
}
class Test {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        list.add(new Student("Bob", 85));
        list.add(new Student("Alice", 92));
        list.add(new Student("Charlie", 78));
        Collections.sort(list);
        for (Student s : list) System.out.print(s + " ");
    }
}
```

10. What will this code print?
```java
import java.util.*;
class Fruit {
    String name;
    int price;
    Fruit(String name, int price) { this.name = name; this.price = price; }
    public String toString() { return name; }
}
class Test {
    public static void main(String[] args) {
        List<Fruit> list = new ArrayList<>();
        list.add(new Fruit("Mango", 100));
        list.add(new Fruit("Apple", 50));
        list.add(new Fruit("Banana", 30));
        Collections.sort(list, (a, b) -> b.price - a.price);
        for (Fruit f : list) System.out.print(f + " ");
    }
}
```

## Answers

1. A
2. B
3. B
4. A - Arrays.sort() for arrays; Collections.sort() for collections
5. B
6. True
7. False - Will throw ClassCastException
8. True - Using reversed comparator
9. Output:
```
Charlie:78 Bob:85 Alice:92
```
10. Output:
```
Mango Apple Banana
```
Sorted by price descending: 100, 50, 30
