package academy.javaengineering.certification;

/**
 * Demonstrates Java certification preparation.
 */
public class CertificationGuide {

    public record Certification(
        String name,
        String level,
        java.util.List<String> topics,
        int questions
    ) {}

    public static java.util.List<Certification> getCertifications() {
        return java.util.List.of(
            new Certification(
                "Oracle Certified Associate: Java SE 8 Programmer",
                "Associate",
                java.util.List.of("Java basics", "OOP", "Exceptions", "Collections"),
                70
            ),
            new Certification(
                "Oracle Certified Professional: Java SE 11 Developer",
                "Professional",
                java.util.List.of("Modularity", "HTTP Client", "Local variable syntax"),
                70
            ),
            new Certification(
                "Oracle Certified Professional: Java SE 17 Developer",
                "Professional",
                java.util.List.of("Records", "Sealed classes", "Pattern matching"),
                50
            )
        );
    }
}
