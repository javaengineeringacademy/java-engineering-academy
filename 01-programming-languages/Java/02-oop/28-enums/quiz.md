# Quiz: Enums

## Multiple Choice Questions

1. What is an enum in Java?
   - A) A special class that represents a group of constants
   - B) A type of loop
   - C) A method modifier
   - D) A package declaration

2. Can Java enums have fields and methods?
   - A) No
   - B) Yes
   - C) Only in Java 17+
   - D) Only static methods

3. What is the default method that returns all enum constants?
   - A) values()
   - B) all()
   - C) list()
   - D) items()

4. How do you compare enum constants?
   - A) Using ==
   - B) Using .equals() only
   - C) Using compareTo()
   - D) Both A and C

5. Can enums implement interfaces?
   - A) No
   - B) Yes
   - C) Only final enums
   - D) Only with extends

## True/False Questions

6. Enum constants are implicitly public and static final.
   - True / False

7. Enums can have constructors, but they must be private.
   - True / False

8. An enum can extend a class.
   - True / False

## Code Output Questions

9. What will this code print?
```java
enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    boolean isWeekend() { return this == SATURDAY || this == SUNDAY; }
}
class Test {
    public static void main(String[] args) {
        for (Day d : Day.values())
            System.out.println(d + " weekend=" + d.isWeekend());
    }
}
```

10. What will this code print?
```java
enum Color {
    RED(1), GREEN(2), BLUE(3);
    private int code;
    Color(int code) { this.code = code; }
    int getCode() { return code; }
}
class Test {
    public static void main(String[] args) {
        System.out.println(Color.RED.getCode());
        System.out.println(Color.valueOf("GREEN").getCode());
        System.out.println(Color.BLUE == Color.BLUE);
        System.out.println(Color.RED.getCode() + Color.BLUE.getCode());
    }
}
```

## Answers

1. A
2. B
3. A
4. D - Both == and compareTo() work for enums
5. B
6. True
7. True - Private constructors prevent external instantiation
8. False - All enums implicitly extend java.lang.Enum
9. Output:
```
MONDAY weekend=false
TUESDAY weekend=false
WEDNESDAY weekend=false
THURSDAY weekend=false
FRIDAY weekend=false
SATURDAY weekend=true
SUNDAY weekend=true
```
10. Output:
```
1
2
true
4
```
