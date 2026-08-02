package academy.javaengineering.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * AOP - @Aspect, @Before, @After, @Around.
 */
public class AOPExample {

    public interface Aspect {
        void before(String method);
        void after(String method);
    }

    public static class LoggingAspect implements Aspect {
        private final List<String> logs = new ArrayList<>();

        @Override
        public void before(String method) { logs.add("Before: " + method); }

        @Override
        public void after(String method) { logs.add("After: " + method); }

        public List<String> getLogs() { return logs; }
    }

    public static class UserService {
        private final LoggingAspect aspect;

        public UserService(LoggingAspect aspect) { this.aspect = aspect; }

        public String createUser(String name) {
            aspect.before("createUser");
            String result = "User: " + name;
            aspect.after("createUser");
            return result;
        }
    }

    public static void main(String[] args) {
        LoggingAspect aspect = new LoggingAspect();
        UserService service = new UserService(aspect);
        System.out.println(service.createUser("John"));
        System.out.println("Logs: " + aspect.getLogs());
    }
}
