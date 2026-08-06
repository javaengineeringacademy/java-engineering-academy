import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

public class RegexPatterns {
    public static void main(String[] args) {
        emailValidation();
        phoneNumberExtraction();
        urlParsing();
        dateParsing();
        passwordValidation();
        logFileParsing();
        csvParsing();
        htmlTagExtraction();
    }

    // 1. Email Validation
    static void emailValidation() {
        System.out.println("=== Email Validation ===");

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String[] emails = {
            "user@example.com",
            "firstname.lastname@company.co.uk",
            "invalid@com",
            "@missing.com",
            "no-at-sign.com",
            "user@.invalid.com"
        };

        Pattern pattern = Pattern.compile(emailRegex);

        for (String email : emails) {
            boolean isValid = pattern.matcher(email).matches();
            System.out.printf("%-35s %s%n", email, isValid ? "Valid" : "Invalid");
        }
        System.out.println();
    }

    // 2. Phone Number Extraction
    static void phoneNumberExtraction() {
        System.out.println("=== Phone Number Extraction ===");

        String text = "Contact us at 555-123-4567 or 555-987-6543. " +
                      "International: +1-555-111-2222. " +
                      "Office: (555) 333-4444";

        // Match US phone numbers
        String phoneRegex = "\\(?(\\d{3})\\)?[-. ]?(\\d{3})[-. ]?(\\d{4})";

        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(text);

        System.out.println("Phone numbers found:");
        while (matcher.find()) {
            String areaCode = matcher.group(1);
            String exchange = matcher.group(2);
            String number = matcher.group(3);
            System.out.printf("  %s-%s-%s (Full: %s)%n",
                areaCode, exchange, number, matcher.group());
        }
        System.out.println();
    }

    // 3. URL Parsing
    static void urlParsing() {
        System.out.println("=== URL Parsing ===");

        String text = "Visit https://www.example.com/page?q=test or " +
                      "http://blog.example.org/article?lang=en#section";

        String urlRegex = "(https?|ftp)://([^/]+)(/[^?#]*)?(\\?[^#]*)?(#.*)?";

        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(text);

        System.out.println("URLs found:");
        while (matcher.find()) {
            String protocol = matcher.group(1);
            String host = matcher.group(2);
            String path = matcher.group(3) != null ? matcher.group(3) : "/";
            String query = matcher.group(4) != null ? matcher.group(4) : "";
            String fragment = matcher.group(5) != null ? matcher.group(5) : "";

            System.out.printf("  Protocol: %s%n", protocol);
            System.out.printf("  Host:     %s%n", host);
            System.out.printf("  Path:     %s%n", path);
            System.out.printf("  Query:    %s%n", query);
            System.out.printf("  Fragment: %s%n", fragment);
            System.out.println();
        }
    }

    // 4. Date Parsing
    static void dateParsing() {
        System.out.println("=== Date Parsing ===");

        String text = "Events: 2024-01-15, 12/25/2024, Jan 5 2024, 5th March 2024";

        // ISO date format
        String isoDateRegex = "(\\d{4})-(\\d{2})-(\\d{2})";
        Pattern isoPattern = Pattern.compile(isoDateRegex);
        Matcher isoMatcher = isoPattern.matcher(text);

        System.out.println("ISO dates (YYYY-MM-DD):");
        while (isoMatcher.find()) {
            System.out.printf("  %s -> Year: %s, Month: %s, Day: %s%n",
                isoMatcher.group(),
                isoMatcher.group(1),
                isoMatcher.group(2),
                isoMatcher.group(3));
        }

        // US date format
        String usDateRegex = "(\\d{1,2})/(\\d{1,2})/(\\d{4})";
        Pattern usPattern = Pattern.compile(usDateRegex);
        Matcher usMatcher = usPattern.matcher(text);

        System.out.println("\nUS dates (MM/DD/YYYY):");
        while (usMatcher.find()) {
            System.out.printf("  %s -> Month: %s, Day: %s, Year: %s%n",
                usMatcher.group(),
                usMatcher.group(1),
                usMatcher.group(2),
                usMatcher.group(3));
        }
        System.out.println();
    }

    // 5. Password Validation
    static void passwordValidation() {
        System.out.println("=== Password Validation ===");

        // At least 8 chars, one uppercase, one lowercase, one digit, one special char
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$";

        String[] passwords = {
            "Passw0rd!",
            "weak",
            "NoSpecial1",
            "nouppercase1!",
            "12345678!"
        };

        Pattern pattern = Pattern.compile(passwordRegex);

        for (String password : passwords) {
            boolean isValid = pattern.matcher(password).matches();
            System.out.printf("%-20s %s%n", password, isValid ? "Strong" : "Weak");
        }
        System.out.println();
    }

    // 6. Log File Parsing
    static void logFileParsing() {
        System.out.println("=== Log File Parsing ===");

        String logData = "2024-01-15 10:30:45 [INFO] User logged in: admin\n" +
                         "2024-01-15 10:31:02 [ERROR] Database connection failed\n" +
                         "2024-01-15 10:31:15 [WARNING] High memory usage: 85%\n" +
                         "2024-01-15 10:32:00 [INFO] Request processed: /api/users";

        String logRegex = "(\\d{4}-\\d{2}-\\d{2})\\s(\\d{2}:\\d{2}:\\d{2})\\s\\[(\\w+)\\]\\s(.+)";

        Pattern pattern = Pattern.compile(logRegex);
        Matcher matcher = pattern.matcher(logData);

        System.out.println("Parsed log entries:");
        while (matcher.find()) {
            String date = matcher.group(1);
            String time = matcher.group(2);
            String level = matcher.group(3);
            String message = matcher.group(4);

            System.out.printf("  Date: %s | Time: %s | Level: %-7s | %s%n",
                date, time, level, message);
        }
        System.out.println();
    }

    // 7. CSV Parsing
    static void csvParsing() {
        System.out.println("=== CSV Parsing ===");

        String csvData = "name,age,city\n" +
                         "John,25,New York\n" +
                         "Jane,30,San Francisco\n" +
                         "Bob,35,\"New York, NY\"";

        String csvRegex = "(?:^|,)(?:\"([^\"]*\"|\"[^\"]*\")*\"|([^,]*))";

        Pattern pattern = Pattern.compile(csvRegex);
        String[] lines = csvData.split("\n");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            System.out.println("Row:");
            while (matcher.find()) {
                String field = matcher.group(1) != null ?
                    matcher.group(1).replace("\"", "") :
                    matcher.group(2);
                System.out.print("    " + field);
            }
            System.out.println();
        }
        System.out.println();
    }

    // 8. HTML Tag Extraction
    static void htmlTagExtraction() {
        System.out.println("=== HTML Tag Extraction ===");

        String html = "<html><body>" +
                      "<h1>Title</h1>" +
                      "<p class=\"intro\">Paragraph 1</p>" +
                      "<p>Paragraph 2</p>" +
                      "<a href=\"https://example.com\">Link</a>" +
                      "<img src=\"image.jpg\" alt=\"Photo\">" +
                      "</body></html>";

        // Extract opening tags with attributes
        String openTagRegex = "<(\\w+)(\\s[^>]*)?>";

        Pattern pattern = Pattern.compile(openTagRegex);
        Matcher matcher = pattern.matcher(html);

        System.out.println("HTML tags found:");
        while (matcher.find()) {
            String tagName = matcher.group(1);
            String attributes = matcher.group(2);
            System.out.printf("  Tag: %-10s Attributes: %s%n",
                tagName, attributes != null ? attributes.trim() : "none");
        }

        // Extract specific tags
        String pTagRegex = "<p[^>]*>(.*?)</p>";
        Pattern pPattern = Pattern.compile(pTagRegex, Pattern.DOTALL);
        Matcher pMatcher = pPattern.matcher(html);

        System.out.println("\nParagraph content:");
        while (pMatcher.find()) {
            System.out.println("  " + pMatcher.group(1));
        }
    }
}
