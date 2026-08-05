package academy.javaengineering.springcore.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DIExamples {
    private final String appName;

    @Autowired
    private EmailService emailService;

    public DIExamples(@Value("${app.name:DefaultApp}") String appName) {
        this.appName = appName;
        System.out.println("Constructor injection: " + appName);
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        System.out.println("Setter injection");
        this.emailService = emailService;
    }

    public void sendNotification(String message) {
        emailService.send(message);
    }

    public String getAppName() {
        return appName;
    }
}
