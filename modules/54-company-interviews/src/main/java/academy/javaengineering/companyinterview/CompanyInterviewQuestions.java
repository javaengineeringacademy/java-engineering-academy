package academy.javaengineering.companyinterview;

/**
 * Demonstrates company-specific interview questions.
 */
public class CompanyInterviewQuestions {

    public record InterviewQuestion(
        String company,
        String category,
        String question,
        String difficulty
    ) {}

    public static java.util.List<InterviewQuestion> getQuestions() {
        return java.util.List.of(
            new InterviewQuestion("Google", "Algorithms", "Two Sum", "Easy"),
            new InterviewQuestion("Google", "System Design", "Design Google Search", "Hard"),
            new InterviewQuestion("Amazon", "Leadership", "Tell me about a time you failed", "Medium"),
            new InterviewQuestion("Amazon", "System Design", "Design Amazon Prime", "Hard"),
            new InterviewQuestion("Meta", "Algorithms", "Valid Parentheses", "Easy"),
            new InterviewQuestion("Meta", "System Design", "Design Facebook News Feed", "Hard"),
            new InterviewQuestion("Apple", "OO Design", "Design a parking lot", "Medium"),
            new InterviewQuestion("Microsoft", "Algorithms", "Merge Two Sorted Lists", "Easy")
        );
    }
}
