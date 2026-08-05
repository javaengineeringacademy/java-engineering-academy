import java.util.ArrayList;
import java.util.List;

public class Teacher {

    private final String name;
    private final String subject;
    private final List<Student> students = new ArrayList<>();

    public Teacher(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.addTeacher(this);
    }

    public List<Student> getStudents() {
        return List.copyOf(students);
    }

    public String getName() { return name; }
    public String getSubject() { return subject; }

    public String getRoster() {
        StringBuilder sb = new StringBuilder(name + "'s " + subject + " class:\n");
        for (Student s : students) {
            sb.append("  - ").append(s.getName()).append("\n");
        }
        return sb.toString();
    }
}