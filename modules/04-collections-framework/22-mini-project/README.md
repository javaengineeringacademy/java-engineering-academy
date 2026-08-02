# Mini Project: Student Management System

## Introduction

Build a complete Student Management System demonstrating all Collections Framework concepts learned in this module.

## Learning Objectives

- Apply all collection types in a real project
- Choose appropriate collections for different scenarios
- Implement sorting and searching
- Create efficient data structures

## Prerequisites

- All previous topics in this module
- Understanding of OOP principles
- Basic file I/O operations

## Project Description

Create a Student Management System that manages students, courses, and grades using various collection types.

### Requirements

1. **Data Structures**
   ```java
   // Student storage - LinkedHashMap for insertion order
   Map<String, Student> students = new LinkedHashMap<>();

   // Course enrollment - HashSet for unique enrollments
   Map<String, Set<String>> courseEnrollments = new HashMap<>();

   // Grade tracking - TreeMap for sorted grades
   Map<String, Map<String, Double>> grades = new TreeMap<>();

   // Search indices - HashSet for fast lookup
   Set<String> emailIndex = new HashSet<>();
   Map<String, List<String>> nameIndex = new HashMap<>();
   ```

2. **Core Classes**
   ```java
   public class Student {
       private String id;
       private String name;
       private String email;
       private LocalDate enrollDate;

       // equals() and hashCode() based on id
   }

   public class Course {
       private String code;
       private String name;
       private int capacity;
       private Set<String> enrolledStudents = new HashSet<>();
   }

   public class GradeBook {
       private Map<String, Map<String, Double>> grades = new TreeMap<>();

       public void addGrade(String studentId, String courseCode, double grade) {
           grades.computeIfAbsent(studentId, k -> new TreeMap<>())
                 .put(courseCode, grade);
       }

       public double getAverage(String studentId) {
           Map<String, Double> studentGrades = grades.get(studentId);
           if (studentGrades == null || studentGrades.isEmpty()) return 0;

           return studentGrades.values().stream()
               .mapToDouble(Double::doubleValue)
               .average()
               .orElse(0);
       }
   }
   ```

3. **Management System**
   ```java
   public class StudentManagementSystem {
       private Map<String, Student> students = new LinkedHashMap<>();
       private Map<String, Course> courses = new HashMap<>();
       private GradeBook gradeBook = new GradeBook();

       public void addStudent(Student student) {
           students.put(student.getId(), student);
       }

       public void enrollStudent(String studentId, String courseCode) {
           Student student = students.get(studentId);
           Course course = courses.get(courseCode);

           if (student == null || course == null) {
               throw new IllegalArgumentException("Invalid student or course");
           }

           if (course.getEnrolledStudents().size() >= course.getCapacity()) {
               throw new CourseFullException("Course is full");
           }

           course.enrollStudent(studentId);
       }

       public List<Student> searchByName(String name) {
           return students.values().stream()
               .filter(s -> s.getName().contains(name))
               .collect(Collectors.toList());
       }

       public Map<String, Double> getLeaderboard() {
           return students.keySet().stream()
               .collect(Collectors.toMap(
                   id -> id,
                   id -> gradeBook.getAverage(id),
                   (a, b) -> a,
                   TreeMap::new
               ));
       }
   }
   ```

## Implementation Steps

1. Create the Student and Course classes with proper equals/hashCode
2. Implement the GradeBook with TreeMap for sorted grades
3. Create the StudentManagementSystem with all operations
4. Add search functionality using HashMap and HashSet
5. Implement sorting and ranking features
6. Create a command-line interface
7. Add file persistence for data storage
8. Test all functionality

## Exercises

1. Complete the Student and Course classes with validation
2. Implement the GradeBook with all required methods
3. Create the StudentManagementSystem with enrollment logic
4. Add search functionality by name and email
5. Implement the leaderboard feature
6. Create the command-line interface

## Interview Questions

- Why did you choose LinkedHashMap for student storage?
- How would you scale this system for millions of students?
- What other data structures could improve performance?

## Common Pitfalls

- Not implementing equals() and hashCode() correctly
- Choosing wrong collection types for the use case
- Not considering performance for large datasets

## Best Practices

1. Use appropriate collection types for each scenario
2. Implement proper equals() and hashCode()
3. Consider time and space complexity
4. Use generics for type safety
5. Handle edge cases properly
6. Document collection choices

## Real World Applications

- School management systems
- University enrollment systems
- Online learning platforms
- Employee management systems

## References

- [Java Collections Framework](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/collections/)
- [Effective Java - Collections](https://www.oreilly.com/library/view/effective-java/9780134686097/)

## Summary

You have completed the Collections Framework module by building a complete Student Management System. This project demonstrates proper collection usage, sorting, searching, and data structure design. Apply these patterns in your future projects.
