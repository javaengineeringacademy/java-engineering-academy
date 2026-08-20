package academy.javaengineering.testing.unit.practices;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Testing Stateful Objects
 *
 * Tasks:
 * 1. Test a task management system
 * 2. Test state transitions
 * 3. Test error conditions
 * 4. Test collection operations
 */
class Exercise2StatefulObject {

    enum TaskStatus { PENDING, IN_PROGRESS, COMPLETED }

    static class Task {
        private final String id;
        private String title;
        private TaskStatus status;
        private final List<String> comments = new ArrayList<>();

        Task(String id, String title) {
            this.id = id;
            this.title = title;
            this.status = TaskStatus.PENDING;
        }

        void start() {
            if (status != TaskStatus.PENDING) throw new IllegalStateException("Cannot start from " + status);
            status = TaskStatus.IN_PROGRESS;
        }

        void complete() {
            if (status != TaskStatus.IN_PROGRESS) throw new IllegalStateException("Cannot complete from " + status);
            status = TaskStatus.COMPLETED;
        }

        void addComment(String comment) { comments.add(comment); }
        String getId() { return id; }
        TaskStatus getStatus() { return status; }
        List<String> getComments() { return Collections.unmodifiableList(comments); }
    }

    // TODO: Write tests for Task state transitions
    @Test
    @DisplayName("should create task in PENDING state")
    void shouldCreateTask() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should transition from PENDING to IN_PROGRESS")
    void shouldStartTask() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should not start completed task")
    void shouldNotStartCompletedTask() {
        // Arrange, Act, Assert
    }
}
