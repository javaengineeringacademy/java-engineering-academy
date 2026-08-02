package academy.javaengineering.spring;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Fundamentals - IoC, DI, ApplicationContext.
 */
public class SpringFundamentalsExample {

    public interface BeanFactory {
        Object getBean(String name);
        <T> T getBean(Class<T> clazz);
    }

    public static class ApplicationContext implements BeanFactory {
        private final Map<String, Object> beans = new HashMap<>();

        public void registerBean(String name, Object bean) { beans.put(name, bean); }

        @Override
        public Object getBean(String name) { return beans.get(name); }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getBean(Class<T> clazz) {
            return beans.values().stream()
                    .filter(clazz::isInstance)
                    .map(clazz::cast)
                    .findFirst()
                    .orElse(null);
        }
    }

    public interface MessageService {
        String getMessage();
    }

    public static class HelloService implements MessageService {
        @Override
        public String getMessage() { return "Hello, Spring!"; }
    }

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        context.registerBean("helloService", new HelloService());
        MessageService service = context.getBean(MessageService.class);
        System.out.println("Message: " + service.getMessage());
    }
}
