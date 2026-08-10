# For Loop Quiz

## Questions

### Q1: What is the basic syntax of a for loop in Java?
**Answer:** for (initialization; condition; update) { body }

### Q2: What happens if the for loop condition is false initially?
**Answer:** The loop body never executes.

### Q3: Can you have an empty initialization in a for loop?
**Answer:** Yes, as long as variables are initialized before the loop.

### Q4: What is a common use of for loops with collections?
**Answer:** Iterating over arrays using an index variable.

### Q5: Can you have multiple update expressions in a for loop?
**Answer:** Yes, using the comma operator: for (int i = 0; i < 10; i++, j--).

### Q6: What is the difference between i++ and ++i in a for loop?
**Answer:** i++ returns the value then increments, while ++i increments then returns the value.

### Q7: Can a for loop run infinitely?
**Answer:** Yes, if the condition is always true or omitted (for (;;) creates an infinite loop).

### Q8: What is the scope of a variable declared in the initialization section?
**Answer:** It is only accessible within the for loop body.

### Q9: How do you iterate over an array using a for loop?
**Answer:** for (int i = 0; i < array.length; i++) { array[i] }

### Q10: Can for loops be nested?
**Answer:** Yes, you can nest for loops inside other for loops.

## Bonus Questions

### Q11: What is the difference between a for loop and a while loop?
**Answer:** for loop is preferred when the number of iterations is known; while loop is preferred for condition-based iteration.

### Q12: What is the traditional for loop also called?
**Answer:** The C-style for loop.

## True/False

**Q13: A for loop always executes at least once.**
Answer: False — If the condition is false initially, the loop body never executes.

**Q14: The loop variable declared in for (int i = 0; ...) is accessible outside the loop.**
Answer: False — The variable is scoped to the for loop block only.

**Q15: for (;;) creates an infinite loop.**
Answer: True — An empty condition is always true, creating an infinite loop.

**Q16: You can use break to exit a for loop early.**
Answer: True — break immediately terminates the loop.

**Q17: continue skips the rest of the loop body and moves to the next iteration.**
Answer: True — continue jumps to the update expression and then checks the condition.

## Code Output

**Q18: What does this code print?**
```java
for (int i = 0; i < 5; i++) {
    if (i == 3) break;
    System.out.print(i + " ");
}
```
Answer: 0 1 2 — Loop breaks when i equals 3.

**Q19: What does this code print?**
```java
for (int i = 0; i < 5; i++) {
    if (i % 2 == 0) continue;
    System.out.print(i + " ");
}
```
Answer: 1 3 — continue skips even numbers.

**Q20: What does this code print?**
```java
int sum = 0;
for (int i = 1; i <= 4; i++) {
    sum += i;
}
System.out.println(sum);
```
Answer: 10 — sum = 1 + 2 + 3 + 4 = 10.
