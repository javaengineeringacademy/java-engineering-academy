package academy.javaengineering.springcore.beanlifecycle;

/**
 * Demonstrates Spring Bean Lifecycle.
 */
public class BeanLifecycleExample {

    // 1. Bean with lifecycle callbacks
    public static class MyBean {
        private String name;
        
        public MyBean() {
            System.out.println("1. Constructor called");
        }
        
        public void setName(String name) {
            this.name = name;
            System.out.println("2. Setter called: " + name);
        }
        
        public void initMethod() {
            System.out.println("3. Init method called");
        }
        
        public void destroyMethod() {
            System.out.println("4. Destroy method called");
        }
        
        public String getName() { return name; }
    }

    // 2. Bean implementing Spring lifecycle interfaces
    public static class SmartBean implements 
            org.springframework.beans.factory.InitializingBean,
            org.springframework.beans.factory.DisposableBean {
        
        private String value;
        
        @Override
        public void afterPropertiesSet() {
            System.out.println("afterPropertiesSet() called");
        }
        
        @Override
        public void destroy() {
            System.out.println("destroy() called");
        }
        
        public void setValue(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    // 3. Bean with @PostConstruct and @PreDestroy
    public static class AnnotationBean {
        private String status;
        
        @jakarta.annotation.PostConstruct
        public void postConstruct() {
            System.out.println("@PostConstruct called");
            this.status = "initialized";
        }
        
        @jakarta.annotation.PreDestroy
        public void preDestroy() {
            System.out.println("@PreDestroy called");
        }
        
        public String getStatus() { return status; }
    }
}
