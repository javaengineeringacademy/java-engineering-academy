import java.util.Date;

public class DefensiveCopy {
    public static void main(String[] args) {
        Date mutableDate = new Date();

        // Without defensive copy - mutable field is exposed
        VulnerableDate vulnerable = new VulnerableDate(mutableDate);
        System.out.println("VulnerableDate internal: " + vulnerable.getDate());
        mutableDate.setTime(0); // Modify original!
        System.out.println("VulnerableDate after mutation: " + vulnerable.getDate());

        // With defensive copy - mutable field is protected
        Date freshDate = new Date();
        SecureDate secure = new SecureDate(freshDate);
        System.out.println("\nSecureDate internal: " + secure.getDate());
        freshDate.setTime(0); // Modify original
        System.out.println("SecureDate after original mutation: " + secure.getDate());

        // Getter also returns defensive copy
        Date returnedDate = secure.getDate();
        returnedDate.setTime(999999999);
        System.out.println("SecureDate after getter mutation: " + secure.getDate());
    }
}

class VulnerableDate {
    private final Date date;

    public VulnerableDate(Date date) {
        this.date = date; // No defensive copy - stores reference directly
    }

    public Date getDate() {
        return date; // Returns mutable reference directly
    }
}

final class SecureDate {
    private final Date date;

    public SecureDate(Date date) {
        this.date = new Date(date.getTime()); // Defensive copy in constructor
    }

    public Date getDate() {
        return new Date(date.getTime()); // Defensive copy in getter
    }
}
