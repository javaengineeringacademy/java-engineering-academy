package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Association Patterns ===\n");

        // WHY: Association models relationships between objects without ownership
        // INTERNAL: Objects reference each other but have independent lifecycles
        // ENGINEERING: Use for peer relationships, avoid bidirectional associations

        Teacher teacher = new Teacher("Dr. Smith", "Math");
        Student s1 = new Student("Alice", 1);
        Student s2 = new Student("Bob", 2);

        teacher.addStudent(s1);
        teacher.addStudent(s2);
        s1.setTeacher(teacher);

        teacher.teach();
        s1.study();

        // TRADE-OFF: Unidirectional vs bidirectional association
        // Unidirectional: simpler, less coupling
        // Bidirectional: more natural for some domains, harder to maintain
    }
}

class Teacher {
    private final String name;
    private final String subject;
    private final java.util.List<Student> students = new java.util.ArrayList<>();

    Teacher(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    public void addStudent(Student s) { students.add(s); }

    public void teach() {
        System.out.println(name + " teaching " + subject + " to " + students.size() + " students");
    }

    public String getName() { return name; }
}

class Student {
    private final String name;
    private final int id;
    private Teacher teacher;

    Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void setTeacher(Teacher t) { this.teacher = t; }

    public void study() {
        System.out.println(name + " studying with teacher " + teacher.getName());
    }
}
