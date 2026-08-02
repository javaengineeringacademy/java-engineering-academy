package academy.javaengineering.interview;

import java.util.*;

/**
 * Spring Interview Questions - Spring/Spring Boot interview prep.
 */
public class SpringInterviewQuestions {

    public interface BeanFactory {
        Object getBean(String name);
        void registerBean(String name, Object bean);
    }

    public static class ApplicationContext implements BeanFactory {
        private final Map<String, Object> beans = new HashMap<>();
        private final List<String> lifecycle = new ArrayList<>();

        @Override
        public void registerBean(String name, Object bean) {
            beans.put(name, bean);
            lifecycle.add("register:" + name);
        }

        @Override
        public Object getBean(String name) {
            lifecycle.add("get:" + name);
            return beans.get(name);
        }

        public List<String> getLifecycle() { return lifecycle; }
    }

    public static class ServiceA {
        private final ServiceB serviceB;
        public ServiceA(ServiceB serviceB) { this.serviceB = serviceB; }
        public String process() { return "A->" + serviceB.process(); }
    }

    public static class ServiceB {
        public String process() { return "B"; }
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ApplicationContext();
        ctx.registerBean("serviceB", new ServiceB());
        ctx.registerBean("serviceA", new ServiceA((ServiceB) ctx.getBean("serviceB")));
        ServiceA serviceA = (ServiceA) ctx.getBean("serviceA");
        System.out.println("Result: " + serviceA.process());
        System.out.println("Lifecycle: " + ctx.getLifecycle());
    }
}
