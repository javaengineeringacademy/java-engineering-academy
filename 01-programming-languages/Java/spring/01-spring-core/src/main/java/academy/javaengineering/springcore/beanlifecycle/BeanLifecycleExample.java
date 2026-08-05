package academy.javaengineering.springcore.beanlifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class BeanLifecycleExample {
    private String status;

    public BeanLifecycleExample() {
        this.status = "created";
        System.out.println("Bean created via constructor");
    }

    @PostConstruct
    public void init() {
        this.status = "initialized";
        System.out.println("Bean initialized - @PostConstruct called");
    }

    public void doWork() {
        System.out.println("Bean working with status: " + status);
    }

    @PreDestroy
    public void cleanup() {
        this.status = "destroyed";
        System.out.println("Bean destroyed - @PreDestroy called");
    }

    public String getStatus() {
        return status;
    }
}
