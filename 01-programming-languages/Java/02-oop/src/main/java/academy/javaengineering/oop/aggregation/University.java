package academy.javaengineering.oop.aggregation;

import java.util.ArrayList;
import java.util.List;

public class University {

    private final String name;
    private final List<Department> departments = new ArrayList<>();

    public University(String name) {
        this.name = name;
    }

    public void addDepartment(Department dept) {
        departments.add(dept);
    }

    public String getName() { return name; }
    public List<Department> getDepartments() { return List.copyOf(departments); }

    public int getTotalCourses() {
        return departments.stream()
                .mapToInt(Department::getCourseCount)
                .sum();
    }

    public String getCatalog() {
        StringBuilder sb = new StringBuilder(name + " Departments:\n");
        for (Department d : departments) {
            sb.append("  ").append(d.getName())
              .append(" (").append(d.getCourseCount()).append(" courses)\n");
        }
        return sb.toString();
    }
}
