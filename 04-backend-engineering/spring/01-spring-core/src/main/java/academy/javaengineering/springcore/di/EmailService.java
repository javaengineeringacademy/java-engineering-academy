package academy.javaengineering.springcore.di;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final java.util.List<String> sentEmails = new java.util.ArrayList<>();

    public void send(String message) {
        sentEmails.add(message);
        System.out.println("Email sent: " + message);
    }

    public java.util.List<String> getSentEmails() {
        return java.util.Collections.unmodifiableList(sentEmails);
    }

    public int getSentCount() {
        return sentEmails.size();
    }

    public void clearHistory() {
        sentEmails.clear();
    }
}
