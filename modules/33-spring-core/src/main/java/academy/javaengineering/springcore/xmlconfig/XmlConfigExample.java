package academy.javaengineering.springcore.xmlconfig;

/**
 * Demonstrates XML-based Spring configuration.
 * Traditional way of configuring Spring beans.
 */
public class XmlConfigExample {

    // Bean defined in XML
    public static class MessageService {
        private String message;
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public void printMessage() {
            System.out.println("Message: " + message);
        }
    }

    // Bean with constructor injection via XML
    public static class EmailService {
        private final String smtpHost;
        private final int port;
        
        public EmailService(String smtpHost, int port) {
            this.smtpHost = smtpHost;
            this.port = port;
        }
        
        public void sendEmail(String to, String subject) {
            System.out.printf("Sending email via %s:%d to %s - %s%n", 
                smtpHost, port, to, subject);
        }
    }

    // Bean with setter injection via XML
    public static class DatabaseService {
        private String url;
        private String username;
        private String password;
        
        public void setUrl(String url) { this.url = url; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        
        public void connect() {
            System.out.printf("Connecting to %s with user %s%n", url, username);
        }
    }
}
