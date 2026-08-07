# Fundamentals Exercises

## Exercise 1: Temperature Converter
Write a program that converts temperatures between Celsius and Fahrenheit.

```c
#include <stdio.h>

int main(void) {
    float celsius, fahrenheit;
    printf("Enter temperature in Celsius: ");
    scanf("%f", &celsius);
    fahrenheit = (celsius * 9.0/5.0) + 32;
    printf("%.2f°C = %.2f°F\n", celsius, fahrenheit);
    return 0;
}
```

## Exercise 2: Factorial Calculator
Write a function that calculates factorial using recursion.

## Exercise 3: Array Statistics
Write functions to find min, max, average, and sum of an array.

## Exercise 4: String Reversal
Write a function that reverses a string in place.

## Exercise 5: Pointer Swap
Implement a swap function using pointers.

## Exercise 6: Dynamic Array
Write a program that reads N integers into a dynamically allocated array.

## Exercise 7: Matrix Multiplication
Implement matrix multiplication using 2D arrays.

## Exercise 8: String Palindrome
Check if a string is a palindrome using pointers.
