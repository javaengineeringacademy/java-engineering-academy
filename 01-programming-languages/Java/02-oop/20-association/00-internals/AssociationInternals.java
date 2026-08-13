package academy.javaengineering.oop.internals;

public class AssociationInternals {

    static class Teacher {
        String name;
        Teacher(String name) { this.name = name; }
        void teach() { System.out.println(name + " is teaching"); }
    }

    static class Student {
        String name;
        Student(String name) { this.name = name; }
        void learn() { System.out.println(name + " is learning"); }
    }

    static class Course {
        Teacher teacher; // Association
        Student student; // Association

        Course(Teacher teacher, Student student) {
            this.teacher = teacher;
            this.student = student;
        }

        void conduct() {
            teacher.teach();
            student.learn();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Association Internals ===\n");

        // 1. Association Relationship
        System.out.println("--- Association ---");
        Teacher teacher = new Teacher("Mr. Smith");
        Student student = new Student("Alice");
        Course course = new Course(teacher, student);
        course.conduct();
        System.out.println("Teacher and Student associated");

        // 2. Types of Association
        System.out.println("\n--- Types ---");
        System.out.println("1. One-to-One");
        System.out.println("2. One-to-Many");
        System.out.println("3. Many-to-One");
        System.out.println("4. Many-to-Many");

        // 3. Unidirectional vs Bidirectional
        System.out.println("\n--- Direction ---");
        System.out.println("Unidirectional: one knows other");
        System.out.println("Bidirectional: both know each other");
    }
}
