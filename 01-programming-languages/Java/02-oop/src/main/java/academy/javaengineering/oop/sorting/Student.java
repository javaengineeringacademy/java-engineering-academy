package academy.javaengineering.oop.sorting;

/**
 * Student class implementing Comparable for natural ordering by name.
 * Natural ordering means that a collection of Student objects will be
 * sorted alphabetically by name when using Collections.sort() or Arrays.sort()
 * without providing a custom Comparator.
 */
public class Student implements Comparable<Student> {

    private String name;
    private int age;
    private String grade;
    private double gpa;

    // Constructor
    public Student(String name, int age, String grade, double gpa) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.gpa = gpa;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGrade() {
        return grade;
    }

    public double getGpa() {
        return gpa;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    /**
     * compareTo method implements natural ordering by name.
     *
     * Contract:
     * - Returns negative integer if this.name is lexicographically less than other.name
     * - Returns zero if this.name is equal to other.name
     * - Returns positive integer if this.name is lexicographically greater than other.name
     *
     * This implements the Comparable interface contract:
     * - Reflexive: x.compareTo(x) == 0
     * - Antisymmetric: if x.compareTo(y) < 0, then y.compareTo(x) > 0
     * - Transitive: if x.compareTo(y) < 0 && y.compareTo(z) < 0, then x.compareTo(z) < 0
     * - Consistent with equals (recommended but not required): x.compareTo(y) == 0 iff x.equals(y)
     */
    @Override
    public int compareTo(Student other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare to null");
        }
        // Use String's natural ordering for name comparison
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return age == student.age &&
               Double.compare(student.gpa, gpa) == 0 &&
               name.equals(student.name) &&
               grade.equals(student.grade);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + age;
        result = 31 * result + grade.hashCode();
        result = 31 * result + Double.hashCode(gpa);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Student{name='%s', age=%d, grade='%s', gpa=%.2f}",
                name, age, grade, gpa);
    }
}
