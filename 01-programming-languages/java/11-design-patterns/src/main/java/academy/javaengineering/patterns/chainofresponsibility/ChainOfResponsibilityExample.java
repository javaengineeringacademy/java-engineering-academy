package academy.javaengineering.patterns.chainofresponsibility;

// Handler
abstract class SupportHandler {
    protected SupportHandler nextHandler;
    
    public SupportHandler setNext(SupportHandler next) {
        this.nextHandler = next;
        return next;
    }
    
    public void handle(String issue) {
        if (nextHandler != null) {
            nextHandler.handle(issue);
        } else {
            System.out.println("No handler could process: " + issue);
        }
    }
}

// Concrete Handlers
class BasicSupport extends SupportHandler {
    @Override
    public void handle(String issue) {
        if (issue.contains("password") || issue.contains("login")) {
            System.out.println("BasicSupport: Handling account issue - " + issue);
        } else {
            super.handle(issue);
        }
    }
}

class TechnicalSupport extends SupportHandler {
    @Override
    public void handle(String issue) {
        if (issue.contains("bug") || issue.contains("error") || issue.contains("crash")) {
            System.out.println("TechnicalSupport: Handling technical issue - " + issue);
        } else {
            super.handle(issue);
        }
    }
}

class BillingSupport extends SupportHandler {
    @Override
    public void handle(String issue) {
        if (issue.contains("payment") || issue.contains("refund") || issue.contains("billing")) {
            System.out.println("BillingSupport: Handling billing issue - " + issue);
        } else {
            super.handle(issue);
        }
    }
}

class ManagerSupport extends SupportHandler {
    @Override
    public void handle(String issue) {
        System.out.println("ManagerSupport: Escalating to management - " + issue);
    }
}

public class ChainOfResponsibilityExample {
    public static void main(String[] args) {
        System.out.println("=== Chain of Responsibility Pattern ===\n");
        
        SupportHandler basic = new BasicSupport();
        SupportHandler technical = new TechnicalSupport();
        SupportHandler billing = new BillingSupport();
        SupportHandler manager = new ManagerSupport();
        
        basic.setNext(technical).setNext(billing).setNext(manager);
        
        String[] issues = {
            "I can't login to my account",
            "There's a bug in the application",
            "I need a refund for my payment",
            "I want to speak to a manager"
        };
        
        for (String issue : issues) {
            System.out.println("Issue: " + issue);
            basic.handle(issue);
            System.out.println();
        }
    }
}
