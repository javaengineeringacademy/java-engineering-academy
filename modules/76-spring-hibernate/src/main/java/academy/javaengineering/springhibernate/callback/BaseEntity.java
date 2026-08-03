package academy.javaengineering.springhibernate.callback;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity lifecycle callbacks.
 */
@EntityListeners(AuditListener.class)
public abstract class BaseEntity {

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

/**
 * Audit listener for entity events.
 */
public class AuditListener {

    @PrePersist
    public void prePersist(Object entity) {
        System.out.println("Entity being created: " + entity.getClass().getSimpleName());
    }

    @PostPersist
    public void postPersist(Object entity) {
        System.out.println("Entity created: " + entity.getClass().getSimpleName());
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        System.out.println("Entity being updated: " + entity.getClass().getSimpleName());
    }
}
