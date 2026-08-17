# Java Fundamentals Practices

Hands-on exercises covering core Java fundamentals: variables, operators, control flow, methods, arrays, and strings.

---

## How to Use

1. Open the exercise file for the topic you want to practice
2. Read the problem description in each method's Javadoc
3. Implement the solution in the method body
4. Run the `main()` method to test your solutions
5. Check the `solutions/` folder if you get stuck

## Exercises Overview

### Variables (5 Exercises)
- `VariableExercises.java` — Primitive types, casting, naming conventions

### Operators (6 Exercises)
- `OperatorExercises.java` — Arithmetic, bitwise, ternary, logical operators

### Control Flow (6 Exercises)
- `ControlFlowExercises.java` — FizzBuzz, prime checker, password validation, patterns

### Methods (6 Exercises)
- `MethodExercises.java` — Overloading, recursion, varargs, functional interfaces

### Arrays (3 Exercises)
- `ArraysExercises.java` — Matrix transpose, rotation, missing number

### Strings (4 Exercises)
- `StringsExercises.java` — Reverse words, anagram, compression, first non-repeating

## Running Exercises

```bash
# Navigate to the practices folder
cd practices/

# Compile and run any exercise
javac VariableExercises.java
java VariableExercises

# Or run with Maven from project root
mvn compile exec:java -Dexec.mainClass="academy.javaengineering.exercises.VariableExercises"
```

## Difficulty Levels

| Level | Description |
|-------|-------------|
| Beginner | Direct application of concepts |
| Intermediate | Combines multiple concepts |
| Advanced | Requires optimization or edge case handling |

## Solutions

All solutions are in the `solutions/` folder. Try to solve each exercise before looking at the solution!

## Tips

- Start with the easier exercises and work your way up
- Test your code with different inputs
- Think about edge cases (empty arrays, null values, boundary conditions)
- Use the README.md in each topic folder for reference
