package academy.javaengineering.oop.objectclass;

/**
 * Student - Demonstrates proper override of Object class methods.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Student implements Cloneable {

    private final String name;
    private final int grade;

    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() { return name; }
    public int getGrade() { return grade; }

    @Override
    public String toString() {
        return "Student{name='" + name + "', grade=" + grade + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student other = (Student) obj;
        return grade == other.grade && java.util.Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, grade);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); // Shallow copy
    }
}