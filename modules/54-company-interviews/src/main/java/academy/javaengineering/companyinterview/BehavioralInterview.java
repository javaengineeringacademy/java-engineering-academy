package academy.javaengineering.companyinterview;

/**
 * Demonstrates behavioral interview preparation.
 */
public class BehavioralInterview {

    public record STARResponse(
        String situation,
        String task,
        String action,
        String result
    ) {}

    public static STARResponse createSTARResponse(
            String situation, String task, String action, String result) {
        return new STARResponse(situation, task, action, result);
    }

    public static void printSTARResponse(STARResponse response) {
        System.out.println("Situation: " + response.situation());
        System.out.println("Task: " + response.task());
        System.out.println("Action: " + response.action());
        System.out.println("Result: " + response.result());
    }

    public static java.util.List<String> getCommonQuestions() {
        return java.util.List.of(
            "Tell me about yourself",
            "Why do you want to work here?",
            "Describe a challenging project",
            "How do you handle conflict?",
            "What are your strengths and weaknesses?"
        );
    }
}
