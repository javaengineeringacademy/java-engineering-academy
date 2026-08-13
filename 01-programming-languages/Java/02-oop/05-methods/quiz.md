# Quiz: Methods

## Multiple Choice Questions

1. What is a method in Java?
   - A) A variable
   - B) A block of code that performs a task
   - C) A class
   - D) An object

2. What is the return type of a method that doesn't return anything?
   - A) void
   - B) null
   - C) none
   - D) empty

3. What is a parameter?
   - A) A variable defined in the method signature
   - B) A value passed to a method
   - C) The return value
   - D) A class member

4. Can two methods have the same name but different parameters?
   - A) No
   - B) Yes, this is called method overloading
   - C) Only if they return different types
   - D) Only in different classes

5. What is a static method?
   - A) A method that belongs to an instance
   - B) A method that belongs to the class
   - C) A method that cannot be overridden
   - D) A method that runs only once

## True/False Questions

6. A method can have multiple return statements.
   - True / False

7. Methods can only return primitive data types.
   - True / False

8. A method can call itself (recursion).
   - True / False

## Code Output Questions

9. What is the output of the following code?
```java
class MathUtils {
    static int square(int num) {
        return num * num;
    }
    static int cube(int num) {
        return num * num * num;
    }
}
public class Main {
    public static void main(String[] args) {
        System.out.println(MathUtils.square(4));
        System.out.println(MathUtils.cube(3));
    }
}
```

10. What is the output of the following code?
```class StringHelper {
    String reverse(String str) {
        String result = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            result += str.charAt(i);
        }
        return result;
    }
}
public class Main {
    public static void main(String[] args) {
        StringHelper sh = new StringHelper();
        System.out.println(sh.reverse("Hello"));
    }
}
```

---

## Answers

1. B) A block of code that performs a task
2. A) void
3. A) A variable defined in the method signature
4. B) Yes, this is called method overloading
5. B) A method that belongs to the class
6. True
7. False (methods can return objects, arrays, etc.)
8. True
9. 16
27
10. olleH