# While Loop Quiz

## Questions

### Q1: What is the basic syntax of a while loop?
**Answer:** while (condition) { body }

### Q2: What happens if the condition is false initially?
**Answer:** The loop body never executes.

### Q3: How is a do-while loop different from a while loop?
**Answer:** do-while always executes at least once because the condition is checked after the body.

### Q4: When should you use a while loop over a for loop?
**Answer:** When the number of iterations is unknown or depends on a condition that changes during execution.

### Q5: What is an infinite while loop?
**Answer:** A while(true) loop that runs until explicitly broken using break or return.

### Q6: What is the role of break in a while loop?
**Answer:** break terminates the loop immediately and transfers control to the statement after the loop.

### Q7: What does continue do in a while loop?
**Answer:** It skips the rest of the current iteration and jumps to the next iteration's condition check.

### Q8: Can a while loop throw a ConcurrentModificationException?
**Answer:** Yes, if you modify a collection during iteration without using an Iterator.

### Q9: What is a common pattern with while loops?
**Answer:** Reading input: while (scanner.hasNext()) { String s = scanner.next(); }

### Q10: Is the body of a while loop executed if the condition is null?
**Answer:** A boolean condition cannot be null; it must evaluate to true or false.

## Bonus Questions

### Q11: What is the difference between while(condition); and while(condition) {}
**Answer:** The semicolon makes the loop body empty, creating an infinite loop if condition is true.

### Q12: Can you use labeled break with a while loop?
**Answer:** Yes, labeled break allows exiting from nested loops: break labelName;
