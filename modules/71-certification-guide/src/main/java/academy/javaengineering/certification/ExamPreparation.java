package academy.javaengineering.certification;

/**
 * Demonstrates exam preparation strategies.
 */
public class ExamPreparation {

    public static java.util.List<String> getStudyTips() {
        return java.util.List.of(
            "Start with official Oracle documentation",
            "Practice with mock exams",
            "Focus on weak areas",
            "Review code examples",
            "Take notes on key concepts"
        );
    }

    public static java.util.Map<String, String> getTopicWeights() {
        return java.util.Map.of(
            "Working with Java Data Types", "15%",
            "Controlling Program Flow", "18%",
            "Java Object-Oriented Approach", "22%",
            "Exception Handling", "10%",
            "Collections and Generics", "12%",
            "Functional Interfaces and Lambda Expressions", "13%"
        );
    }
}
