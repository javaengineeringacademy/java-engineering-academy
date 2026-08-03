package academy.javaengineering.springcore.beanlifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Demonstrates Bean Scopes in Spring.
 */
@Configuration
public class BeanScopeExamples {

    // 1. Singleton (default) - One instance per Spring container
    @Bean
    public SingletonBean singletonBean() {
        return new SingletonBean();
    }

    // 2. Prototype - New instance each time requested
    @Bean
    @org.springframework.context.annotation.Scope("prototype")
    public PrototypeBean prototypeBean() {
        return new PrototypeBean();
    }

    // 3. Request - One instance per HTTP request
    @Bean
    @org.springframework.web.context.annotation.RequestScope
    public RequestBean requestBean() {
        return new RequestBean();
    }

    // 4. Session - One instance per HTTP session
    @Bean
    @org.springframework.web.context.annotation.SessionScope
    public SessionBean sessionBean() {
        return new SessionBean();
    }

    public static class SingletonBean {
        private final String id = java.util.UUID.randomUUID().toString();
        public String getId() { return id; }
    }

    public static class PrototypeBean {
        private final String id = java.util.UUID.randomUUID().toString();
        public String getId() { return id; }
    }

    public static class RequestBean {
        private final String id = java.util.UUID.randomUUID().toString();
        public String getId() { return id; }
    }

    public static class SessionBean {
        private final String id = java.util.UUID.randomUUID().toString();
        public String getId() { return id; }
    }
}
