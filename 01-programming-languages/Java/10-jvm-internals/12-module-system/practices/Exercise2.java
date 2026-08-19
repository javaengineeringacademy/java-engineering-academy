package academy.javaengineering.jvm.modules;

/**
 * Exercise 2: Service Provider Interface
 *
 * Task: Implement a service provider interface with module support.
 */
public class Exercise2 {

    public static void main(String[] args) {
        System.out.println("=== Service Provider Interface ===\n");

        // TODO: Create a service interface
        // TODO: Create a service provider
        // TODO: Declare uses and provides in module-info.java

        System.out.println("Service module-info.java:");
        System.out.println("  module com.example.app {");
        System.out.println("      uses com.example.spi.MessageService;");
        System.out.println("  }");
        System.out.println("\nProvider module-info.java:");
        System.out.println("  module com.example.email {");
        System.out.println("      provides com.example.spi.MessageService");
        System.out.println("          with com.example.email.EmailService;");
        System.out.println("  }");
    }
}
