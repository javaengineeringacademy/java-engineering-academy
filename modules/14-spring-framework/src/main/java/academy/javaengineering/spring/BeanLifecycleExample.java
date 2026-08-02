package academy.javaengineering.spring;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean Lifecycle - @PostConstruct, @PreDestroy.
 */
public class BeanLifecycleExample {

    public interface LifecycleBean {
        void init();
        void destroy();
        String getStatus();
    }

    public static class MyBean implements LifecycleBean {
        private String status = "created";
        private final List<String> events = new ArrayList<>();

        @Override
        public void init() {
            status = "initialized";
            events.add("init");
        }

        @Override
        public void destroy() {
            status = "destroyed";
            events.add("destroy");
        }

        @Override
        public String getStatus() { return status; }
        public List<String> getEvents() { return events; }
    }

    public static void main(String[] args) {
        MyBean bean = new MyBean();
        System.out.println("Status: " + bean.getStatus());
        bean.init();
        System.out.println("After init: " + bean.getStatus());
        bean.destroy();
        System.out.println("After destroy: " + bean.getStatus());
    }
}
