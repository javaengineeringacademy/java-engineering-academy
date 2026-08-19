package academy.javaengineering.jvm.modules;

import java.util.ServiceLoader;

/**
 * Solution 2: Service Provider Interface
 */
public class Solution2 {

    interface MessageService {
        void send(String message);
    }

    public static void main(String[] args) {
        System.out.println("=== Service Provider Interface ===\n");

        // Show service loading
        System.out.println("Service loading with ServiceLoader:");
        System.out.println("  ServiceLoader<MessageService> loader =");
        System.out.println("      ServiceLoader.load(MessageService.class);");
        System.out.println("  for (MessageService service : loader) {");
        System.out.println("      service.send(\"Hello\");");
        System.out.println("  }");

        System.out.println("\nModule declarations:");
        System.out.println("  // Consumer module:");
        System.out.println("  module com.example.app {");
        System.out.println("      uses com.example.spi.MessageService;");
        System.out.println("  }");
        System.out.println("\n  // Provider module:");
        System.out.println("  module com.example.email {");
        System.out.println("      provides com.example.spi.MessageService");
        System.out.println("          with com.example.email.EmailService;");
        System.out.println("  }");
    }
}
