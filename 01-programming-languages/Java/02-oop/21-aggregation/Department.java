import java.util.ArrayList;
import java.util.List;

public class Department {

    private final String name;
    private final List<String> courses;

    public Department(String name, List<String> courses) {
        this.name = name;
        this.courses = new ArrayList<>(courses);
    }

    public String getName() { return name; }
    public List<String> getCourses() { return List.copyOf(courses); }

    public void addCourse(String course) {
        courses.add(course);
    }

    public int getCourseCount() {
        return courses.size();
    }
}