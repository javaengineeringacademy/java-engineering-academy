/*
 * Exercise: Structures in C
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Define and use structures
 *   - Understand struct member access (dot operator)
 *   - Practice nested structures
 *   - Learn about arrays of structures and pointers to structures
 */

#include <stdio.h>
#include <string.h>

/*
 * TODO 1: Define a struct called 'Point' with x and y coordinates (int)
 */

/*
 * TODO 2: Define a struct called 'Rectangle' with:
 * - Two Point structs (top_left, bottom_right)
 * - A character array for color (max 20 chars)
 */

/*
 * TODO 3: Define a struct called 'Student' with:
 * - name (char array, max 50 chars)
 * - age (int)
 * - gpa (float)
 * - id (int)
 */

/*
 * TODO 4: Write a function that creates and returns a Point struct
 * Create point at (5, 10)
 */

/*
 * TODO 5: Write a function that calculates the area of a Rectangle
 * Area = width * height (calculate from corner points)
 */

/*
 * TODO 6: Write a function that prints a Student's information
 */

/*
 * TODO 7: Write a function that compares two Students by GPA
 * Returns 1 if student1 has higher GPA, 0 if equal, -1 if lower
 */

/*
 * TODO 8: Demonstrate an array of structures
 * Create an array of 3 Students, print all their information
 */

/*
 * TODO 9: Demonstrate pointer to structure
 * Create a Student, create a pointer to it, modify and access via pointer
 */

/*
 * TODO 10: Use typedef to create a shorter name for a complex struct
 */

int main(void) {
    /* Test cases */
    /*
    printf("=== Test Cases ===\n");

    // Test Point creation
    Point p = create_point(5, 10);
    printf("Point: (%d, %d) (expected: (5, 10))\n", p.x, p.y);

    // Test Rectangle area
    Rectangle rect = {{0, 10}, {5, 0}, "blue"};
    printf("Rectangle area: %d (expected: 50)\n", calculate_area(rect));

    // Test Student
    Student s1 = {"Alice", 20, 3.8f, 1001};
    print_student(s1);

    // Test Student comparison
    Student s2 = {"Bob", 21, 3.5f, 1002};
    int result = compare_gpa(s1, s2);
    printf("GPA comparison: %d (expected: 1)\n", result);

    // Test array of students
    Student class[3] = {
        {"Charlie", 19, 3.9f, 1003},
        {"Diana", 22, 3.7f, 1004},
        {"Eve", 20, 3.6f, 1005}
    };
    printf("\nClass roster:\n");
    for (int i = 0; i < 3; i++) print_student(class[i]);

    // Test pointer to structure
    Student *sp = &s1;
    printf("Via pointer - Name: %s, GPA: %.1f\n", sp->name, sp->gpa);
    */

    return 0;
}
