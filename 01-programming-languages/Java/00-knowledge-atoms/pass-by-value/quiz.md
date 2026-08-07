# Pass by Value Quiz

Test your understanding of Java's pass-by-value mechanism.

## Question 1

What is the output of the following code?

```java
public class Quiz1 {
    public static void main(String[] args) {
        int x = 10;
        modify(x);
        System.out.println(x);
    }

    public static void modify(int value) {
        value = 20;
    }
}
```

A) 10
B) 20
C) 0
D) Compilation error

## Question 2

What is the output of the following code?

```java
public class Quiz2 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        modifyArray(arr);
        System.out.println(arr[0]);
    }

    public static void modifyArray(int[] a) {
        a[0] = 100;
    }
}
```

A) 1
B) 100
C) 0
D) Compilation error

## Question 3

What is the output of the following code?

```java
public class Quiz3 {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hello");
        modify(sb);
        System.out.println(sb);
    }

    public static void modify(StringBuilder builder) {
        builder.append(" World");
    }
}
```

A) Hello
B) Hello World
C) World
D) Compilation error

## Question 4

What is the output of the following code?

```java
public class Quiz4 {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hello");
        reassign(sb);
        System.out.println(sb);
    }

    public static void reassign(StringBuilder builder) {
        builder = new StringBuilder("Goodbye");
    }
}
```

A) Hello
B) Goodbye
C) null
D) Compilation error

## Question 5

What is the output of the following code?

```java
public class Quiz5 {
    public static void main(String[] args) {
        int a = 5;
        int b = 10;
        swap(a, b);
        System.out.println(a + " " + b);
    }

    public static void swap(int x, int y) {
        int temp = x;
        x = y;
        y = temp;
    }
}
```

A) 5 10
B) 10 5
C) 0 0
D) Compilation error

## Question 6

What is the output of the following code?

```java
public class Quiz6 {
    public static void main(String[] args) {
        String name = "Alice";
        changeName(name);
        System.out.println(name);
    }

    public static void changeName(String n) {
        n = "Bob";
    }
}
```

A) Alice
B) Bob
C) null
D) Compilation error

## Question 7

What is the output of the following code?

```java
public class Quiz7 {
    public static void main(String[] args) {
        Person p = new Person("Alice");
        modifyPerson(p);
        System.out.println(p.getName());
    }

    public static void modifyPerson(Person person) {
        person.setName("Bob");
    }
}

class Person {
    private String name;
    
    public Person(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
```

A) Alice
B) Bob
C) null
D) Compilation error

## Question 8

What is the output of the following code?

```java
public class Quiz8 {
    public static void main(String[] args) {
        Person p = new Person("Alice");
        reassignPerson(p);
        System.out.println(p.getName());
    }

    public static void reassignPerson(Person person) {
        person = new Person("Charlie");
    }
}

class Person {
    private String name;
    
    public Person(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
```

A) Alice
B) Charlie
C) null
D) Compilation error

## Question 9

What is the output of the following code?

```java
public class Quiz9 {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        swapArrays(arr1, arr2);
        System.out.println(arr1[0] + " " + arr2[0]);
    }

    public static void swapArrays(int[] a, int[] b) {
        int[] temp = a;
        a = b;
        b = temp;
    }
}
```

A) 1 4
B) 4 1
C) 0 0
D) Compilation error

## Question 10

What is the output of the following code?

```java
public class Quiz10 {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        swapArrayContents(arr1, arr2);
        System.out.println(arr1[0] + " " + arr2[0]);
    }

    public static void swapArrayContents(int[] a, int[] b) {
        int temp = a[0];
        a[0] = b[0];
        b[0] = temp;
    }
}
```

A) 1 4
B) 4 1
C) 0 0
D) Compilation error

---

## Answers

1. A) 10 - Primitives are passed by value, so the original is unchanged
2. B) 100 - Array contents can be modified through the reference
3. B) Hello World - StringBuilder object is modified through the reference
4. A) Hello - Reassigning the parameter doesn't affect the original
5. A) 5 10 - Primitive swap doesn't work
6. A) Alice - Strings are immutable, reassignment only affects local reference
7. B) Bob - Object state can be modified through the reference
8. A) Alice - Reassigning the parameter doesn't affect the original
9. A) 1 4 - Array reference swap doesn't work
10. B) 4 1 - Array contents can be modified through the reference