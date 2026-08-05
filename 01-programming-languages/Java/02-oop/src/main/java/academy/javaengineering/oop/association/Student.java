package academy.javaengineering.oop.association;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private final String name;
    private final String id;
    private final List<Teacher> teachers = new ArrayList<>();

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void addTeacher(Teacher teacher) {
        if (!teachers.contains(teacher)) {
            teachers.add(teacher);
        }
    }

    public List<Teacher> getTeachers() {
        return List.copyOf(teachers);
    }

    public String getName() { return name; }
    public String getId() { return id; }

    public String getSchedule() {
        StringBuilder sb = new StringBuilder(name + "'s classes:\n");
        for (Teacher t : teachers) {
            sb.append("  - ").append(t.getSubject())
              .append(" (").append(t.getName()).append(")\n");
        }
        return sb.toString();
    }
}
