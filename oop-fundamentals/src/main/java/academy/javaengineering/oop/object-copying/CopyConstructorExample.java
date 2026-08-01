package academy.javaengineering.oop.object-copying;

/**
 * Demonstrates copy constructor pattern.
 *
 * <p>A copy constructor takes an instance of the same class and creates a new
 * instance by copying the fields. This provides explicit control over whether
 * the copy is shallow or deep.</p>
 *
 * <p>Expected output:</p>
 * <pre>
 * === Copy Constructor Pattern ===
 * Original: Student{name='Alice', grade=95, scores=[90, 95, 100]}
 * Copy constructed: Student{name='Alice', grade=95, scores=[90, 95, 100]}
 * original == copy: false
 * original.scores == copy.scores: false
 * After copy.scores().add(85):
 * Original scores: [90, 95, 100]
 * Copy scores: [90, 95, 100, 85]
 * </pre>
 */
public class CopyConstructorExample {

  public static void main(String[] args) {
    System.out.println("=== Copy Constructor Pattern ===");

    Student original = new Student("Alice", 95, java.util.List.of(90, 95, 100));
    Student copy = new Student(original);

    System.out.println("Original: " + original);
    System.out.println("Copy constructed: " + copy);
    System.out.println("original == copy: " + (original == copy));
    System.out.println("original.scores == copy.scores: " + (original.scores() == copy.scores()));

    copy.scores().add(85);

    System.out.println("After copy.scores().add(85):");
    System.out.println("Original scores: " + original.scores());
    System.out.println("Copy scores: " + copy.scores());
  }

  static class Student {
    private final String name;
    private final int grade;
    private final java.util.List<Integer> scores;

    Student(String name, int grade, java.util.List<Integer> scores) {
      this.name = name;
      this.grade = grade;
      this.scores = new java.util.ArrayList<>(scores);
    }

    /** Copy constructor that performs a deep copy. */
    Student(Student other) {
      this.name = other.name;
      this.grade = other.grade;
      this.scores = new java.util.ArrayList<>(other.scores);
    }

    String name() {
      return name;
    }

    int grade() {
      return grade;
    }

    java.util.List<Integer> scores() {
      return scores;
    }

    @Override
    public String toString() {
      return "Student{name='" + name + "', grade=" + grade + ", scores=" + scores + "}";
    }
  }
}
