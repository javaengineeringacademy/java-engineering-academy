# Quiz: Method Overloading

## Multiple Choice Questions

1. What is method overloading in Java?
   - A) Having multiple methods with the same name but different parameter lists
   - B) Having multiple methods with the same name and same parameters
   - C) Calling a method multiple times
   - D) Overriding a parent class method

2. Which of the following is NOT a valid way to overload a method `add(int a, int b)`?
   - A) `add(int a, int b, int c)`
   - B) `add(double a, double b)`
   - C) `add(int a, int b)` with different return type
   - D) `add(String a, String b)`

3. Can two methods have the same name but different return types in Java?
   - A) Yes, it's called overloading
   - B) Only if parameter lists are different
   - C) No, it will cause a compilation error if parameter lists are also same
   - D) Yes, always

4. What does the compiler use to resolve overloaded methods?
   - A) Method name only
   - B) Method signature (name + parameter types)
   - C) Return type
   - D) Access modifier

5. Which is an example of overloading via parameter order?
   - A) `method(int a, String b)` and `method(String b, int a)`
   - B) `method(int a)` and `method(int b)`
   - C) `method(int a)` and `method(int a, int b)`
   - D) Both A and C

## True/False Questions

6. Method overloading is resolved at compile time.
   - True / False

7. You can overload a method by only changing the return type.
   - True / False

8. Varargs can be used to create additional overloaded versions of a method.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Calculator {
    static int add(int a, int b) { return a + b; }
    static double add(double a, double b) { return a + b; }
    static int add(int a, int b, int c) { return a + b + c; }

    public static void main(String[] args) {
        System.out.println(add(2, 3));
        System.out.println(add(2.5, 3.5));
        System.out.println(add(1, 2, 3));
    }
}
```

10. What will this code print?
```java
class Test {
    static String display(int x) { return "int: " + x; }
    static String display(double x) { return "double: " + x; }
    static String display(String x) { return "String: " + x; }

    public static void main(String[] args) {
        System.out.println(display(5));
        System.out.println(display(5.0));
        System.out.println(display("5"));
    }
}
```

## Answers

1. A
2. C - Two methods cannot differ only by return type
3. C - If parameter lists are the same, changing only return type causes compilation error
4. B
5. D - Both changing parameter order and adding parameters are valid overloading approaches
6. True
7. False - Must also differ in parameter list
8. True
9. Output:
```
5
6.0
6
```
10. Output:
```
int: 5
double: 5.0
String: 5
```
